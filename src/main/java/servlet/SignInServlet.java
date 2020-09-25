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
                + "    <meta charset=\"utf-8\">\n"
                + "    <title>Sign in</title>\n"
                + "<link rel=\"stylesheet\" href=\"FirstCSS.css\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css\" />"
                + "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Main\"> Back</a></td>\n"
                + "        <td><a href=\"\\Login\"> Login</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write(this.getInputForm());
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
                + "    <meta charset=\"utf-8\">\n"
                + "    <title>Sign in</title>\n"
                + "<link rel=\"stylesheet\" href=\"FirstCSS.css\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css\" />"
                + "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Main\"> Back</a></td>\n"
                + "        <td><a href=\"\\Login\"> Login</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write(this.getInputForm());
        pw.write("</div>\n</body>\n</html>");
    }
    public String getInputForm() {
        return new String("<div class=\"p-x-1 p-y-3\">\n"
                + " <form class=\"card card-block m-x-auto bg-faded form-width\" method=\"POST\", action=\"Signin\">\n"
                + " <legend class=\"m-b-1 text-xs-center\">Registration</legend>\n"
                + " <div class=\"form-group input-group\">\n"
                + " <span class=\"has-float-label\">\n"
                + " <input class=\"form-control\" id=\"first\" type=\"text\" placeholder=\"First name\" name='FirstName'/>\n"
                + " <label for=\"first\">First name</label>\n"
                + " </span>\n"
                + " <span class=\"has-float-label\">\n"
                + " <input class=\"form-control\" id=\"last\" type=\"text\" placeholder=\"Last name\" name='LastName'/>\n"
                + " <label for=\"last\">Last name</label>\n"
                + " </span>\n"
                + " </div>\n"
                + " <div class=\"form-group input-group\">\n"
                + " <span class=\"input-group-addon\">@</span>\n"
                + " <span class=\"has-float-label\">\n"
                + " <input class=\"form-control\" id=\"email\" type=\"email\" placeholder=\"name@example.com\" name='e-mail'/>\n"
                + " <label for=\"email\">E-mail</label>\n"
                + " </span>\n"
                + " </div>\n"
                + " <div class=\"form-group has-float-label\">\n"
                + " <input class=\"form-control\" id=\"password\" type=\"password\" placeholder=\"********\" name='password'/>\n"
                + " <label for=\"password\">Password</label>\n"
                + " </div>\n"
                + " <div class=\"form-group has-float-label\">\n"
                + " <input class=\"form-control\" id=\"password\" type=\"password\" placeholder=\"********\" name='password again'/>\n"
                + " <label for=\"password\">Password again</label>\n"
                + " </div>\n"
                + " <div class=\"text-xs-center\">\n"
                + " <button class=\"btn btn-block btn-primary\" type=\"submit\">Registration</button>\n"
                + " </div>\n"
                + " </form>\n"
                + "</div>");
    }
}
