package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class FirstServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        /*String name = request.getParameter("name");
        String age = request.getParameter("age");*/
        PrintWriter pw = response.getWriter();
        pw.write("<!DOCTYPE html>");
        pw.write("<html>");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Не найден класс");
            e.printStackTrace();
        }
        String url = "jdbc:postgresql://localhost:5432/notebookdb";
        String login = "postgres";
        String password = "0000";
        //pw.write("<head>");
        pw.write("  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "<link rel=\"stylesheet\" href=\"First.css\">" +
                "    <title>Notebook</title>\n" +
                               "  </head>\n");
        //pw.write("<title>Блокнот</title>");
        //pw.write("</head>");
        //pw.write("<header>\n");
        pw.write("<body>");
        pw.write("<ul class=\"hr>\">");
        pw.write("<li> <a href=\"\\Signin\"> sign in</a></li>");
        pw.write("<li> <a href=\"\\Login\">log in</a></li>");
        pw.write("</ul>\n");
        //pw.write("</header>");
        try {

            Connection con = DriverManager.getConnection(url, login, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM notebook");
            while (rs.next()) {
                pw.write("<h2>" + rs.getString("Id")+ "</h2>" + "<h1>" + rs.getString("Name")
                        + "</h1>" + "<h1>" + rs.getString("Header") + "</h1>");
                pw.write("<p>" + rs.getString("description") + "</p>");
            }
            pw.write("</body>");
        } catch (SQLException throwables) {
            System.out.println("Не запустилось");
            throwables.printStackTrace();
        }
        finally {

        }

        //pw.write("<h1>Hello there</h1>");
        pw.write("</html>");
    }
}
