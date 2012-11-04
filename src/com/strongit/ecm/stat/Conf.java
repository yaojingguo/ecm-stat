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

//  public static Conf loadJson() {
//    ObjectMapper mapper = new ObjectMapper();
//    InputStream in = null;
//    try {
//      in = Conf.class.getResourceAsStream("/ecm-stat.json");
//      Conf conf = (Conf) mapper.readValue(in, Conf.class);
//      return conf;
//    } catch (Exception e) {
//      Closeables.closeQuietly(in);
//      throw new RuntimeException(e);
//    }
//  }
  public static Conf loadYaml() {
    InputStream in = Conf.class.getResourceAsStream("/ecm-stat.yaml");
    Constructor constructor = new Constructor(Conf.class);
    Yaml yaml = new Yaml(constructor);
    Conf conf = (Conf) yaml.load(in);
    return conf;
  }
}
