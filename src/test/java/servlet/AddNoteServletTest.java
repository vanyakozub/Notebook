package servlet;

import dao.DBHelper;
import domain.Note;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddNoteServletTest {

@Before
public void setUp() throws Exception {
    DBHelper.init();
}

    @Test
    public void whenExecutePostShouldCreateNote() throws ServletException, IOException, SQLException {
        Note note = new Note(34, "Test", "James", "Junit test", "Cameron@gmail.com");
        AddNoteServlet addNoteServlet = new AddNoteServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("header")).thenReturn(note.getHeader());
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("firstname")).thenReturn(note.getName());
        when(request.getParameter("description")).thenReturn(note.getDescription());
        when(request.getSession().getAttribute("email")).thenReturn(note.getEmail());
        addNoteServlet.doPost(request, response);
        assertTrue(DBHelper.getAllNotes().contains(note));
    }
}