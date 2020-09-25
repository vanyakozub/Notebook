package servlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.*;
import java.security.MessageDigest;





public class SignInServlet extends javax.servlet.http.HttpServlet{

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String firstName = request.getParameter("FirstName");
        String lastName = request.getParameter("LastName");
        String email = request.getParameter("e-mail");
        String password = request.getParameter("password");
        String passwordAgain = request.getParameter("password again");
        StringBuffer error = new StringBuffer();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден класс");
            e.printStackTrace();
        }
        String algo = "SHA-256";
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
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/notebookdb", "postgres", "0000");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT email FROM users where email = \'" + email + "\'");
            System.out.println(rs.getString("email"));
            error.append("This email is already taken");
        }
        catch (SQLException throwables) {
            System.out.println("No such email");
            //throwables.printStackTrace();
        }
        if (firstName.isEmpty() || lastName.isEmpty()||email.isEmpty()||password.isEmpty()||passwordAgain.isEmpty()) {
            error.append("all fields are required. ");
        }
        if (!password.matches(passwordAgain))
        {
            error.append("Passwords does not matches. ");
        }
        if (!email.contains("@") && !email.contains("."))
        {
            error.append("Incorrect email.");
        }
        /*if(FirstName.isEmpty()) {
            System.out.println("something went wrong!");
        }
        PrintWriter pw = response.getWriter();
        pw.write("Wrong!!!");*/
        PrintWriter pw = response.getWriter();

        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        pw.write("  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Sign in</title>\n" +
                "<link rel=\"stylesheet\" href=\"First.css\">" +
                "  </head>");
        pw.write("<body>\n" +
                "\n" +
                "<table cellspacing=\"10\">\n" +
                "    <tr>\n" +
                "        <td><a href=\"\\Signin\"> Регистрация</a></td>\n" +
                "        <td><a href=\"\\Login\"> Вход</a></td>\n" +
                "    </tr>\n" +
                "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"Signin\">\n" +
                "    <p>First Name <input type=\"text\", name=\"FirstName\", value=\"" + firstName +"\"></p>\n" +
                "    <p>Last Name <input type=\"text\", name=\"LastName\", value=\"" + lastName +"\"></p>\n" +
                "    <p>e-mail <input type=\"text\", name=\"e-mail\", value=\"" + email +"\"></p>\n" +
                "    <p>password <input type=\"password\", name=\"password\"></p>\n" +
                "    <p>password again <input type=\"password\", name=\"password again\"></p>\n" +
                "    <p><input type=\"submit\" value=\"Submit!\"></p>\n" +
                "</form>");
        if (!error.toString().isEmpty()) {
            pw.write("    <div id=\"error\">Error: " + error + "</div>" );
        }
        else {
            try {
                stmt.executeQuery("INSERT INTO users (firstname, lastname, email, password) VALUES (\'"+ firstName +"\', "
                        +"\'"+ lastName +"\', " + "\'"+ email +"\', "+"\'"+ hashPassword +"\')");
            }
            catch (SQLException throwables) {
                System.out.println("something went wrong at insert stage");
                //throwables.printStackTrace();
            }

            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(180);
            session.setAttribute("email", email);
            session.setAttribute("firstname", firstName);

            response.sendRedirect("Main");
        }

        //response.sendRedirect("Signin");
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
                "    <title>Sign in</title>\n" +
                "<link rel=\"stylesheet\" href=\"First.css\">" +
                "  </head>");
        pw.write("<body>\n" +
                "\n" +
                "<table cellspacing=\"10\">\n" +
                "    <tr>\n" +
                "        <td><a href=\"\\Signin\"> Регистрация</a></td>\n" +
                "        <td><a href=\"\\Login\"> Вход</a></td>\n" +
                "    </tr>\n" +
                "</table>");
        pw.write("<div id=\"sign\", align=\"center\">");
        pw.write("<form method=\"POST\", action=\"Signin\">\n" +
                "    <p>First Name <input type=\"text\", name=\"FirstName\"></p>\n" +
                "    <p>Last Name <input type=\"text\", name=\"LastName\"></p>\n" +
                "    <p>e-mail <input type=\"text\", name=\"e-mail\"></p>\n" +
                "    <p>password <input type=\"password\", name=\"password\"></p>\n" +
                "    <p>password again <input type=\"password\", name=\"password again\"></p>\n" +
                "    <p><input type=\"submit\" value=\"Submit!\"></p>\n" +
                "</form>");

        //response.sendRedirect("Signin");
        pw.write("</div>\n" +
                "</body>\n" +
                "</html>");
    }
}
