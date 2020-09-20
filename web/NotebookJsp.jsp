<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 25.07.2020
  Time: 20:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notebook</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="First.css">
</head>

<body>

<%@page import="java.sql.*" %>
<%
    String email = (String)session.getAttribute("email");
    /*System.out.println(email);
    email = email.trim();*/
    if (email== null)
    {
        out.println("<table cellspacing=\"10\">\n" +
                "    <tr>\n" +
                "        <td><h1><a href=\"\\Signin\"> Регистрация ds</a></h1></td>\n" +
                "        <td><h1><a href=\"\\Login\"> Вход</a></h1></td>\n" +
                "    </tr>\n" +
                "</table>\n");
    }
    else {
        out.println("<h1>Welcome, " + session.getAttribute("firstname") + "</h1>");
        out.println("<table cellspacing=\"10\">\n" +
                "    <tr>\n" +
                "        <td><a href=\"\\Logout\"> Выйти</a></td>\n" +
                "    </tr>\n" +
                "</table>\n");
        out.println("<div id=\"sign\", align=\"center\">");
        out.println("<form method=\"GET\", action=\"AddNote\">\n");
        out.println("    <p><input type=\"submit\" value=\"Добавить запись\"></p>\n" +
                "</form></div>");
    }

    /*try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        System.out.println("Не найден класс");
        e.printStackTrace();
    }*/
%>
<div id="notes", align="center">
<%
    try {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/notebookdb", "postgres", "0000");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM notebook");
        while (rs.next()) {
            out.println("<h2>" + rs.getString("Id")+ "</h2>" + "<h1>" + rs.getString("Name") +
                    "<h1>" + rs.getString("email") + "</h1>"
                    + "</h1>" + "<h1>" + rs.getString("Header") + "</h1>");
            out.println("<p>" + rs.getString("description") + "</p>");
        }
    } catch (SQLException throwables) {
        System.out.println("Не запустилось");
        throwables.printStackTrace();
    }
%>
</div>
<div>
    <%
        //String FirstName = request.getParameter("FirstName");

        System.out.println("<p>" + email + "</p>");
    %>
</div>
</body>
</html>
