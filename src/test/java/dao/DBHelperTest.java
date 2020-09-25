package dao;

import domain.Note;
import domain.User;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class DBHelperTest {

    @Before
    public void setUp() throws Exception {
        DBHelper.init();
    }

    @Test
    public void whenAddNoteReturnNewNote() {
        try {
            Note note = new Note(34,"Test", "James", "Junit test", "Cameron@gmail.com");
            assertEquals(note, DBHelper.addNote(note.getHeader(), note.getName(), note.getDescription(), note.getEmail()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Test
    public void whenGetAllNotesReturnListWithNotes() {
        try {
            assertTrue(!DBHelper.getAllNotes().isEmpty());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void whenDeleteNoteDBSizeDecrease() {
        try {
            List<Note> list = DBHelper.getAllNotes();
            int size = list.size() - 1;
            DBHelper.deleteNote(list.get(size).getId());
            assertEquals(size, DBHelper.getAllNotes().size());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void whenCreateUserReturnNewUser() {
        try {
            User user = new User("Ivan", "Kozub", "vanyakozub@mail.ru", "password");
            assertEquals(user, DBHelper.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void whenDeleteUserDBSizeDecrease() {
        try {
            List<User> list = DBHelper.getAllUsers();
            int size = list.size() - 1;
            DBHelper.deleteUser(list.get(size).getEmail());
            assertEquals(size, DBHelper.getAllUsers().size());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
