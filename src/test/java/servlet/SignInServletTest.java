package servlet;

import dao.DBHelper;
import domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignInServletTest {

    @Before
    public void setUp() throws Exception {
        DBHelper.init();
    }

    @Test
    public void whenExecutePostShouldRegisterUser() throws SQLException, ServletException, IOException {
        User user = new User("Ivan", "Kozub", "vanyakozub@mail.ru", "password");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("FirstName")).thenReturn(user.getFirstName());
        when(request.getParameter("LastName")).thenReturn(user.getLastName());
        when(request.getParameter("e-mail")).thenReturn(user.getEmail());
        when(request.getParameter("password")).thenReturn(user.getPassword());
        when(request.getParameter("password again")).thenReturn(user.getPassword());
        PrintWriter pw = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(pw);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        SignInServlet signInServlet = new SignInServlet();
        if (DBHelper.getAllUsers().contains(user)) {
            DBHelper.deleteUser(user.getEmail());
        }
        signInServlet.doPost(request, response);
        User hashUser = DBHelper.getUserByEmail(user.getEmail());
        assertTrue(DBHelper.getAllUsers().contains(hashUser));
    }
}