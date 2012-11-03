package com.strongit.ecm.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class JacksonTest {
  @Test
  public void testBuild() {
    System.out.println(buildData());
  }
  
  public static String buildData() {
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
      
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(data);
      return json;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  
}
