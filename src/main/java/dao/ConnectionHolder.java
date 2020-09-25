package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class ConnectionHolder {
    Connection connection;
    PreparedStatement noteStatement;
    PreparedStatement deleteNoteStatement;
    PreparedStatement addNoteStatement;
    PreparedStatement userStatement;
    PreparedStatement createUserStatement;
    PreparedStatement deleteUserStatement;
    PreparedStatement getUserStatement;

    public PreparedStatement getGetUserStatement() {
        return getUserStatement;
    }

    public void setGetUserStatement(PreparedStatement getUserStatement) {
        this.getUserStatement = getUserStatement;
    }

    public PreparedStatement getDeleteUserStatement() {
        return deleteUserStatement;
    }

    public void setDeleteUserStatement(PreparedStatement deleteUserStatement) {
        this.deleteUserStatement = deleteUserStatement;
    }

    public PreparedStatement getCreateUserStatement() {
        return createUserStatement;
    }

    public void setCreateUserStatement(PreparedStatement createUserStatement) {
        this.createUserStatement = createUserStatement;
    }

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
