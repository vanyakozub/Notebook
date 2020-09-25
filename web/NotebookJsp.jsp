<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notebook</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="First.css">
</head>

<body>
<%@page import="java.sql.*" %>
<%@ page import="dao.DBHelper" %>
<%@ page import="domain.Note" %>
<%@ page import="java.util.List" %>
<%
    String email = (String)session.getAttribute("email");
    if (email== null)
    {
        out.println("<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><h1><a href=\"\\Signin\"> Регистрация</a></h1></td>\n"
                + "        <td><h1><a href=\"\\Login\"> Вход</a></h1></td>\n"
                + "    </tr>\n"
                + "</table>\n");
    }
    else {
        out.println("<h1>Welcome, " + session.getAttribute("firstname") + "</h1>");
        out.println("<table cellspacing=\"10\">\n"
                + "    <tr>\n" + "        <td><a href=\"\\Logout\"> Выйти</a></td>\n"
                + "    </tr>\n"
                + "</table>\n");
        out.println("<div id=\"sign\", align=\"center\">");
        out.println("<form method=\"GET\", action=\"AddNote\">\n");
        out.println("    <p><input type=\"submit\" value=\"Добавить запись\"></p>\n"
                + "</form></div>");
    }
%>
<div id="notes", align="center">
    <%
        try {
            List<Note> noteList = DBHelper.getAllNotes();
            for (Note note: noteList){
                if (note.getEmail().equals(email)) {
                    out.println("<div id=\"delete\", align=\"center\">");
                    out.println("<form method=\"GET\", action=\"DeleteNote\">\n");
                    out.println("<p><input type=\"hidden\" name=\"id\" value=\"" + note.getId() +"\">"
                            + "   <p><input type=\"submit\" value=\"Удалить\"></p>");
                }
                out.println("<h1>" + note.getName()
                        + "<h1>" +note.getEmail() + "</h1>"
                        + "</h1>" + "<h1>" + note.getHeader() + "</h1>");
                out.println("<p>" + note.getDescription() + "</p>");
            }
        } catch (SQLException throwables) {
            System.out.println("Не запустилось");
            throwables.printStackTrace();
        }
    %>
</div>
</body>
</html>
