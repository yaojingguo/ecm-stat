package com.strongit.ecm.stat;

import java.beans.PropertyVetoException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.strongit.ecm.stat.Conf.JdbcConnection;

public class DbUtil {
  public static SimpleJdbcTemplate jdbcTmpl;
  
  static {
    JdbcConnection jdbc = Conf.conf.jdbcConnection;
    jdbcTmpl = createJdbcTemplate(jdbc.driverClass, jdbc.jdbcUrl,
        jdbc.user, jdbc.password);
  }

  private static SimpleJdbcTemplate createJdbcTemplate(String driverClass,
      String jdbcUrl, String user, String password) {
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
