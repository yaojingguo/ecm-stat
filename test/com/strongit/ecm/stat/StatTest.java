package com.strongit.ecm.stat;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.google.common.io.Closeables;

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
  public void testDate() {
    Assert.assertEquals("2012-10-05 00:00:00", Stat.addSuffix("2012-10-05"));
    Assert.assertEquals("2012-09-01 00:00:00", Stat.addDaySuffix("2012-09"));

    Assert.assertEquals(false, Stat.hasDay("2012-10"));
    Assert.assertEquals(true, Stat.hasDay("2012-10-12"));
    try {
      Assert.assertEquals(true, Stat.hasDay("2012"));
      Assert.fail();
    } catch (IllegalArgumentException e) {
    }
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
