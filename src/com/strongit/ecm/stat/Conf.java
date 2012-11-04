package com.strongit.ecm.stat;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Conf extends BaseObject {

  public JdbcConnection jdbcConnection;

  public static class JdbcConnection extends BaseObject {
    public String driverClass;
    public String jdbcUrl;
    public String user;
    public String password;
  }
  
  public static Conf conf;
  
  static {
    conf = loadYaml();
  }

  public static Conf loadYaml() {
    InputStream in = Conf.class.getResourceAsStream("/ecm-stat.yaml");
    Constructor constructor = new Constructor(Conf.class);
    Yaml yaml = new Yaml(constructor);
    Conf conf = (Conf) yaml.load(in);
    return conf;
  }
}
