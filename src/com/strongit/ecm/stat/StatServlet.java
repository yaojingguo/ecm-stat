package com.strongit.ecm.stat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    System.out.println("beginDate: " + beginDate + ", endDate: " + endDate + ", chartType:" + chartType);
  }
}
