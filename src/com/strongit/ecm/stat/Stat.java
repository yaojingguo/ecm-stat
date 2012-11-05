package com.strongit.ecm.stat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.RowMapper;

public class Stat {
  public static DateTimeFormatter ymd = DateTimeFormat.forPattern("yyyy-MM-dd");
  public static DateTimeFormatter ym = DateTimeFormat.forPattern("yyyy-MM");

  static String X_KEY = "x";
  static String Y_KEY = "y";
  static String CHARTTYPE_KEY = "chartType";

  private static int YM_LEN = 7;
  private static int YMD_LEN = 10;
  private static int YMDH_LEN = 13;

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Return the JSON string for the given object
   */
  public static String toJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /***
   * Database query and convert the result into a map.
   */
  public static Map queryData(String beginDate, String endDate) {
    Query q = new Query(beginDate, endDate);
    List table = DbUtil.jdbcTmpl.query(q.getSql(),
                                       q.getMapper(),
                                       q.getBeginDate(),
                                       q.getEndDate());

    Map map = transpose(table);
    return map;
  }

  public static String buildJson(String beginDate, String endDate, int chartType) {
    Map map = queryData(beginDate, endDate);
    map.put(CHARTTYPE_KEY, chartType);
    return toJson(map);
  }

  public static Workbook buildExcel(String beginDate, String endDate) {
    Map map = queryData(beginDate, endDate);
    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("系统访问情况统计报表");
    // header
    Row header = sheet.createRow(0);
    Cell dateLabel = header.createCell(0);
    dateLabel.setCellValue("日期");
    Cell loginCountLabel = header.createCell(1);
    loginCountLabel.setCellValue("登录次数");
    // data
    List x = (List) map.get(X_KEY);
    List y = (List) map.get(Y_KEY);
    int dateRowCount = x.size();
    for (int i = 0; i < dateRowCount; i++) {
      Row data = sheet.createRow(i + 1);
      String date = (String) x.get(i);
      String loginCount = ((Integer) y.get(i)).toString();
      data.createCell(0).setCellValue(date);
      data.createCell(1).setCellValue(loginCount);
    }
    return wb;
  }

  static Map transpose(List table) {
    Map map = new HashMap();
    List x = new ArrayList();
    List y = new ArrayList();
    for (Object o : table) {
      List row = (List) o;
      x.add(row.get(0));
      y.add(row.get(1));
    }
    map.put(X_KEY, x);
    map.put(Y_KEY, y);
    return map;
  }

  static String createDate(int year, int month) {
    DateTime d = create(year, month, 1);
    return ym.print(d);
  }

  static String createDate(int year, int month, int day) {
    DateTime d = create(year, month, day);
    return ymd.print(d);
  }

  private static DateTime create(int year, int month, int day) {
    return new DateTime(year, month, day, 0, 0, 0, 0);
  }

  private static class StatMapper implements RowMapper {
    private int len;
    public StatMapper(int len) {
      this.len = len;
    }
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      List ls = new ArrayList();
      int login_count = rs.getInt("login_count");
      java.sql.Timestamp login_date = rs.getTimestamp("login_date");
      System.out.println("login_date: " + login_date);
      String login_year_month = login_date.toString().substring(0, len);
      ls.add(login_year_month);
      ls.add(login_count);
      return ls;
    }
  };

  static class Query extends BaseObject {
    //@off
    private static String monthSQl = "select login_date, count(*) as login_count  "
        + " from login_log"
        + " where ? <= login_date and login_date < ?"
        + " group by year, month" 
        + " order by year, month ASC;";
    private static String daySQl = "select login_date, count(*) as login_count  "
        + " from login_log"
        + " where ? <= login_date and login_date < ?"
        + " group by year, month, day" 
        + " order by year, month, day ASC;";
    private static String hourSQl = "select login_date, count(*) as login_count  "
        + " from login_log"
        + " where ? <= login_date and login_date < ?"
        + " group by year, month, day, hour"
        + " order by year, month, day, hour ASC;";
    //@on
    /**
     * <tt>MM</tt> begins with 1. <tt>dd</tt> begins with 1. <tt>HH</tt>'s range
     * is from 0 to 23.
     */
    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private RowMapper mapper;
    private String sql;
    private String beginDate;
    private String endDate;

    public Query(String beginDate, String endDate) {
      this.beginDate = beginDate;
      this.endDate = endDate;
      build();
    }

    private void build() {
      int len = beginDate.length();
      if (len == YM_LEN) {
        beginDate += "-01 00:00:00";
        endDate += "-01 00:00:00";
        endDate = plusOneMonth(endDate);
        sql = monthSQl;
      } else if (len == YMD_LEN) {
        beginDate += " 00:00:00";
        endDate += " 00:00:00";
        endDate = plusOneDay(endDate);
        sql = daySQl;
      } else if (len == YMDH_LEN) {
        beginDate += ":00:00";
        endDate += ":00:00";
        endDate = plusOneHour(endDate);
        sql = hourSQl;
      } else {
        throw new IllegalArgumentException("'" + beginDate
            + "' has a wrong length " + len);
      }
      mapper = new StatMapper(len);
    }
    /*
     * To specify a date range for SQL query, endDate needs to be inceased at
     * the least part. For example, 2012-01 needs to be increased to 2012-02.
     */
    static String plusOneMonth(String dateStr) {
      DateTime dt = fmt.parseDateTime(dateStr);
      dt = dt.plusMonths(1);
      return fmt.print(dt);
    }

    static String plusOneDay(String dateStr) {
      DateTime dt = fmt.parseDateTime(dateStr);
      dt = dt.plusDays(1);
      return fmt.print(dt);
    }

    static String plusOneHour(String dateStr) {
      DateTime dt = fmt.parseDateTime(dateStr);
      dt = dt.plusHours(1);
      return fmt.print(dt);
    }

    public String getBeginDate() {
      return beginDate;
    }
    public String getEndDate() {
      return endDate;
    }
    public RowMapper getMapper() {
      return mapper;
    }
    public String getSql() {
      return sql;
    }
  }
}
