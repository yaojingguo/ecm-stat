package com.strongit.ecm.stat;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.Closeables;
import com.strongit.ecm.stat.Stat.Query;

public class StatTest {
  private void verifyList(List<String> ls, String... strs) {
    Assert.assertArrayEquals(strs, ls.toArray());
  }

  @Test
  public void testRange() {
    List<String> ls;
    
    // Month
    ls = Stat.range("2011-01","2011-01", Stat.YM_LEN);
    verifyList(ls, "2011-01");
    ls = Stat.range("2011-01", "2011-03", Stat.YM_LEN);
    verifyList(ls, "2011-01", "2011-02", "2011-03");
    ls = Stat.range("2011-01", "2011-04", Stat.YM_LEN);
    verifyList(ls, "2011-01", "2011-02", "2011-03", "2011-04");
    
    // Day
    ls = Stat.range("2011-01-01", "2011-01-03", Stat.YMD_LEN);
    verifyList(ls, "2011-01-01", "2011-01-02", "2011-01-03");
    ls = Stat.range("2011-01-30", "2011-02-01", Stat.YMD_LEN);
    verifyList(ls, "2011-01-30", "2011-01-31", "2011-02-01");
    
    // Hour
    ls = Stat.range("2011-01-01 00", "2011-01-01 02", Stat.YMDH_LEN);
    verifyList(ls, "2011-01-01 00", "2011-01-01 01", "2011-01-01 02");
  }
  
  @Test
  public void testFill() {
    // Month
    List<List> table = new ArrayList<List>();
    List a11 = new ArrayList();
    a11.add("2011-01");
    a11.add(90);
    List a12 = new ArrayList();
    a12.add("2011-03");
    a12.add(67);
    List a13 = new ArrayList();
    a13.add("2011-06");
    a13.add(67);
    table.add(a11);
    table.add(a12);
    table.add(a13);
    
    List newTable = Stat.fill(table, Stat.YM_LEN);
    System.out.println(newTable);
    
    // Day
    
    // Hour
    
  }
  
  @Test
  public void testBuildQueryResult() {
    System.out.println(buildSampleData());
  }

  @Test
  public void testBuildJson() {
    try {
      System.out.println(Stat.buildJson("2011-01", "2013-01", 0));
      System.out.println(Stat.buildJson("2011-01-01", "2013-01-02", 0));
      System.out.println(Stat.buildJson("2011-01-01 00", "2013-01-01 23", 1));
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
    // print(Query.plusOneMonth("2012-00-01 00:00:00"));

    print(Query.plusOneDay("2012-12-01 00:00:00"));
    print(Query.plusOneDay("2012-12-31 00:00:00"));
    // print(Query.plusOneDay("2012-12-00 00:00:00"));

    print(Query.plusOneHour("2012-12-01 00:00:00"));
    print(Query.plusOneHour("2012-12-01 23:00:00"));
    // print(Query.plusOneHour("2012-12-01 24:00:00"));
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
