package com.strongit.ecm.stat;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.google.common.io.Closeables;
import com.strongit.ecm.stat.Stat.Query;

public class StatTest {
  @Test
  public void testBuildQueryResult() {
    System.out.println(buildSampleData());
  }

  @Test
  public void testBuildJson() {
    try {
      System.out.println(Stat.buildJson("2011-01", "2013-01", 0));
      System.out.println(Stat.buildJson("2011-01-01", "2013-01-02", 0));
      System.out.println(Stat.buildJson("2011-01-01 00", "2013-01-02 23", 1));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testBuildExcel() {
    FileOutputStream fileOut = null;
    try {
      fileOut = new FileOutputStream("系统访问情况统计报表.xls");
      Workbook wb = Stat.buildExcel("2011-01", "2013-01");
      wb.write(fileOut);
    } catch (Exception e) {
      e.printStackTrace();
      Closeables.closeQuietly(fileOut);
    }
  }
  
  @Test
  public void testPlusDate() {
    print(Query.plusOneMonth("2012-12-01 00:00:00"));
    print(Query.plusOneMonth("2012-01-01 00:00:00"));
//    print(Query.plusOneMonth("2012-00-01 00:00:00"));
    
    print(Query.plusOneDay("2012-12-01 00:00:00"));
    print(Query.plusOneDay("2012-12-31 00:00:00"));
//    print(Query.plusOneDay("2012-12-00 00:00:00"));
    
    print(Query.plusOneHour("2012-12-01 00:00:00"));
    print(Query.plusOneHour("2012-12-01 23:00:00"));
//    print(Query.plusOneHour("2012-12-01 24:00:00"));
  }

  private static void print(Object o) {
    System.out.println(o);
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
      data.put(Stat.X_KEY, x);
      data.put(Stat.Y_KEY, y);
      data.put(Stat.CHARTTYPE_KEY, 0);

      String json = Stat.toJson(data);
      return json;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
