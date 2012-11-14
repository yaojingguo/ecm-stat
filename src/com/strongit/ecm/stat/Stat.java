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
  public static int YM_LEN = 7;
  public static int YMD_LEN = 10;
  public static int YMDH_LEN = 13;

  public static DateTimeFormatter ym = DateTimeFormat.forPattern("yyyy-MM");
  public static DateTimeFormatter ymd = DateTimeFormat.forPattern("yyyy-MM-dd");
  public static DateTimeFormatter ymdh = DateTimeFormat.forPattern("yyyy-MM-dd HH");

  static String X_KEY = "x";
  static String Y_KEY = "y";
  static String CHARTTYPE_KEY = "chartType";

  private static Map<Integer, DateTimeFormatter> fmt_map = new HashMap<Integer, DateTimeFormatter>();

  static {
    fmt_map.put(YM_LEN, ym);
    fmt_map.put(YMD_LEN, ymd);
    fmt_map.put(YMDH_LEN, ymdh);
  }

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

  static List<String> range(String beginStr, String endStr, int len) {
    DateTimeFormatter fmt = fmt_map.get(len);
    DateTime begin = fmt.parseDateTime(beginStr);
    DateTime end = fmt.parseDateTime(endStr);

    List<DateTime> dates = new ArrayList<DateTime>();
    while (begin.isBefore(end) || begin.isEqual(end)) {
      dates.add(begin);
      if (len == YM_LEN) {
        begin = begin.plusMonths(1);
      } else if (len == YMD_LEN) {
        begin = begin.plusDays(1);
      } else if (len == YMDH_LEN) {
        begin = begin.plusHours(1);
      } else {
        throw new IllegalStateException();
      }
    }

    List<String> strs = new ArrayList<String>();
    for (DateTime d : dates) {
      strs.add(fmt.print(d));
    }
    return strs;
  }

  static List<List> fill(List<List> oldTable, int len) {
    List<List> newTable = new ArrayList<List>();

    int size = oldTable.size();
    if (size < 2)
      return oldTable;
    for (int i = 0; i < size - 1; i++) {
      List begin = oldTable.get(i);
      List end = oldTable.get(i + 1);
      List<String> xRange = range((String) begin.get(0),
                                  (String) end.get(0),
                                  len);
      newTable.add(begin);
      for (int j = 1; j < xRange.size() - 1; j++) {
        List zeroEntry = new ArrayList();
        zeroEntry.add(xRange.get(j));
        zeroEntry.add(0);
        newTable.add(zeroEntry);
      }
    }
    newTable.add(oldTable.get(size-1));
    return newTable;
  }

  /***
   * Database query and convert the result into a map.
   */
  public static Map queryData(String beginDate, String endDate) {
    Query q = new Query(beginDate, endDate);
    StatMapper mapper = q.getMapper();
    List<List> table = DbUtil.jdbcTmpl.query(q.getSql(),
                                             mapper,
                                             q.getBeginDate(),
                                             q.getEndDate());
    addBeginEndIfMissing(beginDate, endDate, table);
    // //////////////////////////////////////////////////////
    int len = mapper.getLen();
    table = fill(table, len);
    // //////////////////////////////////////////////////////
    Map map = transpose(table);
    return map;
  }

  private static void addBeginEndIfMissing(String beginDate,
                                           String endDate,
                                           List<List> table) {
    System.out.println("table from database: " + table);
    if (table.size() == 0 || !table.get(0).get(0).equals(beginDate)) {
      List begin = new ArrayList();
      begin.add(beginDate);
      begin.add(0);
      table.add(0, begin);
    }
    if (!endDate.equals(beginDate)
        && !table.get(table.size() - 1).get(0).equals(endDate)) {
      List end = new ArrayList();
      end.add(endDate);
      end.add(0);
      table.add(end);
    }
    System.out.println("table after adding missing begin and end: " + table);
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

  // static String createDate(int year, int month) {
  // DateTime d = create(year, month, 1);
  // return ym.print(d);
  // }
  //
  // static String createDate(int year, int month, int day) {
  // DateTime d = create(year, month, day);
  // return ymd.print(d);
  // }
  //
  // private static DateTime create(int year, int month, int day) {
  // return new DateTime(year, month, day, 0, 0, 0, 0);
  // }

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
      String loginDateStr = login_date.toString().substring(0, len);
      ls.add(loginDateStr);
      ls.add(login_count);
      return ls;
    }
    public int getLen() {
      return len;
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

    private StatMapper mapper;
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
    public StatMapper getMapper() {
      return mapper;
    }
    public String getSql() {
      return sql;
    }
  }
}
