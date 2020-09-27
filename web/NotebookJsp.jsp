<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notebook</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="FirstCSS.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css" />
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
                + "        <td><a href=\"\\Signin\"> Registration </a></td>\n"
                + "        <td><a href=\"\\Login\"> Login</a></td>\n"
                + "    </tr>\n"
                + "</table>");
    }
    else {
        out.println("<h1>Welcome, " + session.getAttribute("firstname") + "</h1>");
        out.println("<table cellspacing=\"10\">\n"
                + "    <tr>\n" + "        <td><a href=\"\\Logout\"> Logout</a></td>\n"
                + "    </tr>\n"
                + "</table>\n");
        out.println("<div id=\"sign\", align=\"center\">");
        out.println("<form method=\"GET\", action=\"AddNote\">\n");
        out.println("<input type=\"submit\" value=\"Add note\">\n"
                + "</form></div>");
    }
%>
<div id="notes">
    <%
        try {
            List<Note> noteList = DBHelper.getAllNotes();
            out.println("<div class=\"container\">\n<div class=\"row\">");
            for (Note note: noteList){
                out.println("<div class=\"col-md-3 col-sm-6\">\n"
                        + " <div class=\"our-team\">\n"
                        + " <div class=\"pic\">");
                out.println("<h4 class=\"title\">" + note.getHeader() + "</h4>" + " </div>");
                out.println("<div class=\"team-content\">");
                out.println("<h5>" + note.getDescription() + "</h5>");
                out.println("<span class=\"post\">" + note.getName() + ", " + "</span>");
                out.println("<span class=\"post\">" + note.getEmail() + "</span>");
                if (note.getEmail().equals(email)) {
                    out.println("<div id=\"delete\", align=\"center\">");
                    out.println("<form method=\"GET\", action=\"DeleteNote\">\n");
                    out.println("<p><input type=\"hidden\" name=\"id\" value=\"" + note.getId() +"\">"
                            + "   <p><input type=\"submit\" value=\"Delete\"></p>\n</form>\n</div>");
                }
                out.println("</div>\n" + " </div>\n" + " </div>");
            }
            out.println("</div></div>");
        } catch (SQLException throwables) {
            System.out.println("Не запустилось");
            throwables.printStackTrace();
        }
    %>
</div>
</body>
</html>
