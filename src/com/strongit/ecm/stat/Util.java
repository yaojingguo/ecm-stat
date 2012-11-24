package com.strongit.ecm.stat;

import java.beans.PropertyVetoException;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.strongit.ecm.stat.Conf.JdbcConnection;

public class Util {
  /**
   * <tt>MM</tt> begins with 1. <tt>dd</tt> begins with 1. <tt>HH</tt>'s range
   * is from 0 to 23.
   */
  public static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
  public static SimpleJdbcTemplate jdbcTmpl;

  static {
    JdbcConnection jdbc = Conf.conf.jdbcConnection;
    jdbcTmpl = createJdbcTemplate(jdbc.driverClass,
                                  jdbc.jdbcUrl,
                                  jdbc.user,
                                  jdbc.password);
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

  private static SimpleJdbcTemplate createJdbcTemplate(String driverClass,
                                                       String jdbcUrl,
                                                       String user,
                                                       String password) {
    ComboPooledDataSource cp = new ComboPooledDataSource();
    try {
      cp.setDriverClass(driverClass);
    } catch (PropertyVetoException pve) {
      throw new RuntimeException(pve);
    }
    cp.setJdbcUrl(jdbcUrl);
    cp.setUser(user);
    cp.setPassword(password);
    cp.setTestConnectionOnCheckout(true);
    JdbcTemplate template = new JdbcTemplate(cp);
    template.setFetchSize(1000);
    return new SimpleJdbcTemplate(template);
  }

}
