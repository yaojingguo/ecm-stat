package com.strongit.ecm.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class StatTest {
  @Test
  public void testBuild() {
    System.out.println(buildSampleData());
  }
  
  public static String buildSampleData() {
    try {
      Map data = new HashMap();
      List x = new ArrayList();
      List y = new ArrayList();
      x.add("2012-1");
      x.add("2012-2");
      x.add("2012-3");
      x.add("2012-4");
      x.add("2012-5");
      y.add(100);
      y.add(200);
      y.add(78);
      y.add(100);
      y.add(300);
      data.put("x", x);
      data.put("y", y);
      data.put("chartType", 0);
      
      String json = Stat.toJson(data);
      return json;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @Test
  public void testDb() {
    try {
    Stat.queryData("", "", 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
