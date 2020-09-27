/**
 * @author Ivan.Kozub
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import dao.DBHelper;

public class AddNoteServlet extends javax.servlet.http.HttpServlet {
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
                + "<link rel=\"stylesheet\" href=\"FirstCSS.css\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css\" />"
                + "</head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + " <tr>\n"
                + " <td><a href=\"\\Main\"> Back </a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write(this.getInputForm());
        pw.write("</div>\n"
                + "</body>\n"
                + "</html>");
    }
    public String getInputForm() {
        return new String("<div class=\"p-x-1 p-y-3\">\n"
                + " <form class=\"card card-block m-x-auto bg-faded form-width\" method=\"POST\", action=\"AddNote\">\n"
                + " <legend class=\"m-b-1 text-xs-center\">Note</legend>\n"
                + " <div class=\"form-group input-group\">\n"
                + " <span class=\"has-float-label\">\n"
                + " <input class=\"form-control\" id=\"Header\" type=\"text\" placeholder=\"Header\" name='header'/>\n"
                + " <label for=\"Header\">Header</label>\n"
                + " </span>\n"
                + " </div>\n"
                + " <div class=\"form-group has-float-label\">\n"
                + " <input class=\"form-control\" id=\"description\" type=\"text\" placeholder=\"Description\" name='description'/>\n"
                + " <label for=\"Description\">Description</label>\n"
                + " </div>\n"
                + " <div class=\"text-xs-center\">\n"
                + " <button class=\"btn btn-block btn-primary\" type=\"submit\">Add note</button>\n"
                + " </div>\n"
                + " </form>\n"
                + "</div>");
    }
}
