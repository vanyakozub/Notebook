package servlet;
import dao.DBHelper;
import domain.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.SQLException;
import java.security.MessageDigest;





public class SignInServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doPost(final javax.servlet.http.HttpServletRequest request,
                          final javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        String firstName = request.getParameter("FirstName");
        String lastName = request.getParameter("LastName");
        String email = request.getParameter("e-mail");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("password again");
        StringBuffer error = new StringBuffer();
        String hashPassword = new String();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] hashBytes = messageDigest.digest();
            hashPassword = String.format("%040x", new BigInteger(1, hashBytes));
            User userExist = DBHelper.getUserByEmail(email);
            if (userExist == null) {
                error.append("This email is already taken");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || password.isEmpty() || passwordAgain.isEmpty()) {
            error.append("all fields must be filled. ");
        }
        if (!password.matches(passwordAgain)) {
            error.append("Passwords does not matches. ");
        }
        if (!email.contains("@") && !email.contains(".")) {
            error.append("Incorrect email.");
        }
        PrintWriter pw = response.getWriter();

        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n"
                +
                "    <meta charset=\"utf-8\">\n"
                +
                "    <title>Sign in</title>\n"
                +
                "<link rel=\"stylesheet\" href=\"First.css\">"
                +
                "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Signin\"> Регистрация</a></td>\n"
                + "        <td><a href=\"\\Login\"> Вход</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"Signin\">\n"
                + "    <p>First Name <input type=\"text\", name=\"FirstName\", value=\"" + firstName +"\"></p>\n"
                + "    <p>Last Name <input type=\"text\", name=\"LastName\", value=\"" + lastName +"\"></p>\n"
                + "    <p>e-mail <input type=\"text\", name=\"e-mail\", value=\"" + email +"\"></p>\n"
                + "    <p>password <input type=\"password\", name=\"password\"></p>\n"
                + "    <p>password again <input type=\"password\", name=\"password again\"></p>\n"
                + "    <p><input type=\"submit\" value=\"Submit!\"></p>\n"
                + "</form>");
        if (!error.toString().isEmpty()) {
            pw.write("    <div id=\"error\">Error: " + error + "</div>");
        } else {
            try {
                DBHelper.createUser(firstName, lastName, email, hashPassword);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(-1);
            session.setAttribute("email", email);
            session.setAttribute("firstname", firstName);
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
                +
                "    <meta charset=\"utf-8\">\n"
                +
                "    <title>Sign in</title>\n"
                +
                "<link rel=\"stylesheet\" href=\"First.css\">"
                +
                "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Signin\"> Регистрация</a></td>\n"
                + "        <td><a href=\"\\Login\"> Вход</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"Signin\">\n"
                + "    <p>First Name <input type=\"text\", name=\"FirstName\"></p>\n"
                + "    <p>Last Name <input type=\"text\", name=\"LastName\"></p>\n"
                + "    <p>e-mail <input type=\"text\", name=\"e-mail\"></p>\n"
                + "    <p>password <input type=\"password\", name=\"password\"></p>\n"
                + "    <p>password again <input type=\"password\", name=\"password again\"></p>\n"
                + "    <p><input type=\"submit\" value=\"Submit!\"></p>\n"
                + "</form>");
        pw.write("</div>\n</body>\n</html>");
    }
}
