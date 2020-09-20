package servlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AddNote extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден класс");
            e.printStackTrace();
        }
        Connection con = null;
        Statement stmt = null;
        HttpSession session = request.getSession();
        System.out.println(request.getParameter("header"));
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/notebookdb", "postgres", "0000");
            stmt = con.createStatement();
            stmt.executeQuery("INSERT INTO notebook (header, name, description, email) VALUES (\'"+ request.getParameter("header") +"\', "
                    +"\'"+ session.getAttribute("firstname") +"\', " + "\'"+ request.getParameter("description") +"\', "+"\'"+ session.getAttribute("email") +"\')");
        }
        catch (SQLException throwables) {
            System.out.println("something went wrong at insert stage");
            throwables.printStackTrace();
        }
        response.sendRedirect("Main");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>AddNote</title>\n" +
                "<link rel=\"stylesheet\" href=\"First.css\">" +
                "  </head>");
        pw.write("<body>\n" +
                "\n" +
                "<table cellspacing=\"10\">\n" +
                "    <tr>\n" +
                "        <td><a href=\"\\Main\"> Назад</a></td>\n" +
                "    </tr>\n" +
                "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"AddNote\">\n" +
                "    <p>header <input type=\"text\", name=\"header\"></p>\n" +
                "    <p>description <input type=\"text\", name=\"description\"></p>\n" +
                "    <p><input type=\"submit\" value=\"Submit!\"></p>\n" +
                "</form>");
        pw.write("</div>\n" +
                "</body>\n" +
                "</html>");
    }

}
