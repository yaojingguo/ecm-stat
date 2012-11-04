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
  private static String SUFFIX = " 00:00:00";
  public static DateTimeFormatter ymd = DateTimeFormat.forPattern("YYYY-MM-DD");
  public static DateTimeFormatter ym = DateTimeFormat.forPattern("YYYY-MM");

  static String X_KEY = "x";
  static String Y_KEY = "y";
  static String CHARTTYPE_KEY = "chartType";

  private static int YMD_LEN = 10;
  private static int YM_LEN = 7;

  private static String monthSQl = "select login_date, count(*) as login_count  "
      + " from login_log"
      + " where ? <= login_date and login_date < ?"
      + " group by year, month" + " order by year, month ASC;";
  private static String daySQl = "select login_date, count(*) as login_count  "
      + " from login_log" + " where ? <= login_date and login_date < ?"
      + " group by year, month, day" + " order by year, month ASC;";

  private static ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static RowMapper monthMapper = new StatMapper(YM_LEN);
  private static RowMapper dayMapper = new StatMapper(YMD_LEN);

  private static class StatMapper implements RowMapper {
    private int len;
    public StatMapper(int len) {
      this.len = len;
    }
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      List ls = new ArrayList();
      int login_count = rs.getInt("login_count");
      String login_year_month = rs.getDate("login_date")
                                  .toString()
                                  .substring(0, len);
      ls.add(login_year_month);
      ls.add(login_count);
      return ls;
    }
  };

  public static Map queryData(String beginDate, String endDate) {
    List table;
    if (hasDay(beginDate)) {
      beginDate = addSuffix(beginDate);
      endDate = addSuffix(endDate);
      table = null;
      table = DbUtil.jdbcTmpl.query(daySQl, dayMapper, beginDate, endDate);
    } else {
      beginDate = addDaySuffix(beginDate);
      endDate = addDaySuffix(endDate);
      table = DbUtil.jdbcTmpl.query(monthSQl, monthMapper, beginDate, endDate);
    }
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
    map.put("x", x);
    map.put("y", y);
    return map;
  }

  static String format(int year, int month) {
    DateTime d = create(year, month, 1);
    return ym.print(d);
  }

  static String format(int year, int month, int day) {
    DateTime d = create(year, month, day);
    return ymd.print(d);
  }

  static String addDaySuffix(String dateStr) {
    return addSuffix(dateStr + "-01");
  }

  static String addSuffix(String dateStr) {
    return dateStr + SUFFIX;
  }

  static boolean hasDay(String dateStr) {
    int len = dateStr.length();
    switch (len) {
      case 10 : // YMD_LEN
        return true;
      case 7 : // YM_LEN
        return false;
      default :
        throw new IllegalArgumentException("'" + dateStr
            + "' has a wrong length " + len);
    }
  }

  private static DateTime create(int year, int month, int day) {
    return new DateTime(year, month, day, 0, 0, 0, 0);
  }

}
