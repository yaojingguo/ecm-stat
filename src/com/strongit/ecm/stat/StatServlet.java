package com.strongit.ecm.stat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class StatServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String beginDate = request.getParameter("beginDate");
    String endDate = request.getParameter("endDate");
    int chartType = Integer.parseInt(request.getParameter("chartType"));
    int timeDimType = Integer.parseInt(request.getParameter("timeDimType"));
    System.out.println("beginDate: " + beginDate + ", endDate: " + endDate
        + ", chartType:" + chartType);

    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    PrintWriter pw = response.getWriter();
    String json = StatTest.buildSampleData();
    System.out.println("data json string: " + json);
    pw.print(json);
    pw.close();
  }


}
