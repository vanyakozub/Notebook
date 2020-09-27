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
        if (email != null || user.getEmail() != null) {
            if (!email.contentEquals(user.getEmail()) || !hashPassword.contentEquals(user.getPassword())) {
                error.append("Wrong email or password. ");
            }
        }
        else {
            error.append("No such e-mail. ");
        }

        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <title>Login</title>\n"
                + "<link rel=\"stylesheet\" href=\"FirstCSS.css\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css\" />"
                + "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Main\">Back</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
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
                + "<link rel=\"stylesheet\" href=\"FirstCSS.css\">"
                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css\" />"
                + "  </head>");
        pw.write("<body>\n"
                + "\n"
                + "<table cellspacing=\"10\">\n"
                + "    <tr>\n"
                + "        <td><a href=\"\\Main\">Back</a></td>\n"
                + "    </tr>\n"
                + "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write(this.getInputForm());
        pw.write("</div>\n</body>\n</html>");
    }
    public static String getInputForm() {
        return new String("<div class=\"p-x-1 p-y-3\">\n"
                + " <form class=\"card card-block m-x-auto bg-faded form-width\" method=\"POST\", action=\"Login\">\n"
                + " <legend class=\"m-b-1 text-xs-center\">Login</legend>\n"
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
                + " <div class=\"text-xs-center\">\n"
                + " <button class=\"btn btn-block btn-primary\" type=\"submit\">Login</button>\n"
                + " </div>\n"
                + " </form>\n"
                + "</div>");
    }

}
