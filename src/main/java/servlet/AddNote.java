/**
 * @author Ivan.Kozub
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import dao.DBHelper;

public class AddNote extends javax.servlet.http.HttpServlet {
    @Override
    protected void doPost(final javax.servlet.http.HttpServletRequest request,
                          final javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        try {
            DBHelper.addNote(request.getParameter("header"),
                    (String) request.getSession().getAttribute("firstname"),
                    request.getParameter("description"),
                    (String) request.getSession().getAttribute("email"));
        } catch (SQLException ex) {
            System.out.println("something went wrong at insert stage");
            ex.printStackTrace();
        }
        response.sendRedirect("Main");
    }
    @Override
    protected void doGet(final javax.servlet.http.HttpServletRequest request,
                         final javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <title>AddNote</title>\n"
                + "<link rel=\"stylesheet\" href=\"First.css\">"
                + "</head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + " <tr>\n"
                + " <td><a href=\"\\Main\"> Назад</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"AddNote\">\n"
                + "    <p>header <input type=\"text\", name=\"header\"></p>\n"
                + "    <p>description <input type=\"text\", name=\"description\"></p>\n"
                + "    <p><input type=\"submit\" value=\"Submit!\"></p>\n"
                + "</form>");
        pw.write("</div>\n"
                + "</body>\n"
                + "</html>");
    }

}
