package servlet;
import dao.DBHelper;
import domain.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doPost(final javax.servlet.http.HttpServletRequest request,
                          final javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        String email = request.getParameter("e-mail");
        String password = request.getParameter("password");
        StringBuffer error = new StringBuffer();
        String hashPassword = new String();
        User user = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
            messageDigest.update(password.getBytes());
            byte[] hashBytes = messageDigest.digest();
            hashPassword = String.format("%040x", new BigInteger(1, hashBytes));
            user = DBHelper.getUserByEmail(email);
        } catch(NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
        }
        if (!email.contentEquals(user.getEmail()) || !hashPassword.contentEquals(user.getPassword())) {
            error.append("Wrong email or password. ");
        }
        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <title>Login</title>\n"
                + "<link rel=\"stylesheet\" href=\"First.css\">"
                + "  </head>");
        pw.write(getInputForm());
        if (!error.toString().isEmpty()) {
            pw.write("    <div id=\"error\">Error: " + error + "</div>");
        }
        else {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(-1);
            session.setAttribute("email", user.getEmail());
            session.setAttribute("firstname", user.getFirstName());
            response.sendRedirect("Main");
        }
        pw.write("</div>\n</body>\n</html>");
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
                + "    <title>Login</title>\n"
                + "<link rel=\"stylesheet\" href=\"First.css\">"
                + "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Main\"> Назад</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"Login\">\n"
                + "    <p>e-mail <input type=\"text\", name=\"e-mail\"></p>\n"
                + "    <p>password <input type=\"password\", name=\"password\"></p>\n"
                + "    <p><input type=\"submit\" value=\"Submit!\"></p>\n"
                + "</form>");
        pw.write("</div>\n</body>\n</html>");
    }
        public static String getInputForm() {
            StringBuffer sb = new StringBuffer();
            sb.append("<body>\n");
            sb.append("\n");
            sb.append("<table cellspacing=\"10\">\n");
            sb.append("    <tr>\n");
            sb.append("        <td><a href=\"Main\"> Назад</a></td>");
            sb.append("    </tr>");
            sb.append("</table>");
            sb.append("<div id=\"sign\", align=\"center\">");
            sb.append("<form method=\"POST\\\", action=\"Login\">");
            sb.append("<p>e-mail <input type=\"text\", name=\"e-mail\"></p>");
            sb.append("<p>password <input type=\"password\", name=\"password\"></p>");
            sb.append("<p><input type=\"submit\" value=\"Submit!\"></p>");
            sb.append("</form>");
            return sb.toString();
        }
}
