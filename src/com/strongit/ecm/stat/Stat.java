package com.strongit.ecm.stat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

public class Stat {
  private static int MONTH = 0;
  private static int DAY = 1;
  
  private static String monthSQl = "select count(*) as login_count, year, month" + 
      " from login_log" + 
      " where ? <= login_date and login_date < ?" +
      " group by year, month" +
      " order by year, month ASC;";
  
  private static ObjectMapper mapper = new ObjectMapper();

  public static String toJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private static RowMapper monthMapper = new RowMapper() {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      List ls = new ArrayList();
      int login_count = rs.getInt("login_count");
      int year = rs.getInt("year");
      int month = rs.getInt("month");
      ls.add(login_count);
      ls.add(year);
      ls.add(month);
      System.out.println("login_count: " + login_count + ", year: " + year + ", month: " + month);
      return ls;
    }
    
  };

  public static void queryData(String beginDate, String endDate, int timeDimType) {
    List table = DbUtil.jdbcTmpl.query(monthSQl, monthMapper, "2011-01-01 00:00:00", "2012-10-01 00:00:00");
    System.out.println(table);
  }

}
