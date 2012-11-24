package com.strongit.ecm.stat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

public class QueryServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String beginDate = req.getParameter("beginDate");
    String endDate = req.getParameter("endDate");
    String userName = req.getParameter("userName");
    System.out.println("Request: beginDate: " + beginDate + ", endDate: "
        + endDate + ", userName: " + userName);

    Map map = new HashMap();
    map.put("aoColumns", buildHeader());
    map.put("aaData", buildData());

    resp.setContentType("application/json; charset=UTF-8");
    PrintWriter pw = resp.getWriter();
 

    String json = Util.toJson(map);
    System.out.println("Query result: " + json);
    pw.print(json);
  };

  List buildHeader() {
    List aoColumns = new ArrayList();
    Map userName = new HashMap();
    userName.put("sTitle", "用户名");
    Map loginTime = new HashMap();
    loginTime.put("sTitle", "登录时间");
    aoColumns.add(userName);
    aoColumns.add(loginTime);
    return aoColumns;
  }

  List buildData() {
    List aaData = new ArrayList();
    String userNames[] = {"李惠", "王明", "刘云"};
    String dateStr = "2012-10-11 12:05:23";
    DateTime date = Util.fmt.parseDateTime(dateStr);
    
    for (int i = 0; i < 100; i++) {
      aaData.add(r(userNames[i % userNames.length], Util.fmt.print(date)));
      date = date.plusDays(1);
    }
    
    return aaData;
  }

  List<String> r(String... cells) {
    return Arrays.asList(cells);
  }
}
