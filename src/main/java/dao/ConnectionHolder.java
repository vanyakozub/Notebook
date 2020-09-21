package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class ConnectionHolder {
    Connection connection;
    PreparedStatement noteStatement;
    PreparedStatement deleteNoteStatement;
    PreparedStatement addNoteStatement;
    PreparedStatement userStatement;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getNoteStatement() {
        return noteStatement;
    }

    public void setNoteStatement(PreparedStatement noteStatement) {
        this.noteStatement = noteStatement;
    }

    public PreparedStatement getDeleteNoteStatement() {
        return deleteNoteStatement;
    }

    public void setDeleteNoteStatement(PreparedStatement deleteNoteStatement) {
        this.deleteNoteStatement = deleteNoteStatement;
    }

    public PreparedStatement getAddNoteStatement() {
        return addNoteStatement;
    }

    public void setAddNoteStatement(PreparedStatement addNoteStatement) {
        this.addNoteStatement = addNoteStatement;
    }

    public PreparedStatement getUserStatement() {
        return userStatement;
    }

    public void setUserStatement(PreparedStatement userStatement) {
        this.userStatement = userStatement;
    }
}
