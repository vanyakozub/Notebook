package servlet;

import dao.DBHelper;
import domain.Note;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteNoteServletTest {

    @Before
    public void setUp() throws Exception {
        DBHelper.init();
    }

    @Test
    public void whenExecuteGetShouldDeleteNote() throws SQLException, ServletException, IOException {
        DeleteNoteServlet delete = new DeleteNoteServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        List<Note> list = DBHelper.getAllNotes();
        for(Note n: list) {
            if(n.getDescription().equals("Junit test")){
                when(request.getParameter("id")).thenReturn(Integer.toString(n.getId()));
                delete.doGet(request,response);
                break;
            }
        }
        assertEquals(DBHelper.getAllNotes().size(), list.size() - 1);
    }
}