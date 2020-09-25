package servlet;

import dao.DBHelper;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteNoteServlet extends javax.servlet.http.HttpServlet {
    @Override
    protected void doPost(final javax.servlet.http.HttpServletRequest request,
                          final javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {

    }
    @Override
    protected void doGet(final javax.servlet.http.HttpServletRequest request,
                         final javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        try {
            DBHelper.deleteNote((Integer.parseInt(request.getParameter("id"))));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        response.sendRedirect("Main");
    }
}
