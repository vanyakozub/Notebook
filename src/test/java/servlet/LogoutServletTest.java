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
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogoutServletTest {

    @Before
    public void setUp() throws Exception {
        DBHelper.init();
    }


    @Test
    public void whenExecuteGetShouldLogoutUser() throws SQLException, ServletException, IOException {
        User user = new User("Ivan", "Kozub", "vanyakozub@mail.ru", "password");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        request.getSession().setAttribute("email", user.getEmail());
        request.getSession().setAttribute("firstname", user.getFirstName());
        LogoutServlet logoutServlet = new LogoutServlet();
        logoutServlet.doGet(request, response);
        assertNull(request.getSession().getAttribute("email"));
    }
}