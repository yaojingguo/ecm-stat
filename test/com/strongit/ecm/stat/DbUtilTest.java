package com.strongit.ecm.stat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import com.strongit.ecm.stat.Conf.JdbcConnection;

public class DbUtilTest {

  @Test
  public void testJdbc() {
    try {
      JdbcConnection jc = Conf.conf.jdbcConnection;
      Class.forName(jc.driverClass);
      Connection con = DriverManager.getConnection(jc.jdbcUrl,
                                                   jc.user,
                                                   jc.password);
      Statement stmt = con.createStatement();
      String query = "select distinct year from login_log";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        System.out.println(rs.getInt(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
