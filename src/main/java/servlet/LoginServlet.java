package servlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

public class LoginServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String email = request.getParameter("e-mail");
        String password = request.getParameter("password");
        StringBuffer error = new StringBuffer();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден класс");
            e.printStackTrace();
        }
        String algo = "sha-256";
        MessageDigest messageDigest = null;
        byte[] hashBytes = null;
        String hashPassword = new String();
        try
        {
            messageDigest = MessageDigest.getInstance(algo);
            messageDigest.update( password.getBytes() );
            hashBytes = messageDigest.digest();
            hashPassword = String.format("%040x", new BigInteger(1, hashBytes));
            System.out.println (hashPassword);
            System.out.println (hashPassword.length());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Connection con = null;
        Statement stmt = null;
        System.out.println(email);
        System.out.println(email.length());
        String passwordFromDB = new String();
        String emailFromDB = new String();
        String firstName = new String();
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/notebookdb", "postgres", "0000");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users where email = \'" + email + "\'");
            rs.next();
            emailFromDB = rs.getString("email");
            System.out.println(emailFromDB);
            System.out.println(emailFromDB.length());
            passwordFromDB = rs.getString("password");
            System.out.println(passwordFromDB);
            System.out.println(passwordFromDB.length());
            firstName = rs.getString("firstname");
            //error.append("This email is already taken");
        }
        catch (SQLException throwables) {
            System.out.println("No such email");
            throwables.printStackTrace();
        }
        if(!email.contentEquals(emailFromDB)||!hashPassword.contentEquals(passwordFromDB)) {
            error.append("Wrong email or password. ");
        }

        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Login</title>\n" +
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
        pw.write("<form method=\"POST\", action=\"Login\">\n" +
                "    <p>e-mail <input type=\"text\", name=\"e-mail\"></p>\n" +
                "    <p>password <input type=\"password\", name=\"password\"></p>\n" +
                "    <p><input type=\"submit\" value=\"Submit!\"></p>\n" +
                "</form>");
        if (!error.toString().isEmpty()) {
            pw.write("    <div id=\"error\">Error: " + error + "</div>" );
        }
        else {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(-1);
            session.setAttribute("email", email);
            session.setAttribute("firstname",firstName );

            response.sendRedirect("Main");
        }
        pw.write("</div>\n" +
                "</body>\n" +
                "</html>");
    }
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Login</title>\n" +
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
        pw.write("<form method=\"POST\", action=\"Login\">\n" +
                "    <p>e-mail <input type=\"text\", name=\"e-mail\"></p>\n" +
                "    <p>password <input type=\"password\", name=\"password\"></p>\n" +
                "    <p><input type=\"submit\" value=\"Submit!\"></p>\n" +
                "</form>");
        pw.write("</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
