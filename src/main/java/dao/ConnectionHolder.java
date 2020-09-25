package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class ConnectionHolder {
    /**
     * User's connection to DB.
     */
    private Connection connection;
    /**
     * User's prepared statement for retrieve all notes.
     */
    private PreparedStatement noteStatement;
    /**
     * User's prepared statement for delete note.
     */
    private PreparedStatement deleteNoteStatement;
    /**
     * User's prepared statement for add note.
     */
    private PreparedStatement addNoteStatement;
    /**
     * User's prepared statement for retrieve all users.
     */
    private PreparedStatement userStatement;
    /**
     * User's prepared statement for create new user.
     */
    private PreparedStatement createUserStatement;
    /**
     * User's prepared statement for delete the user.
     */
    private PreparedStatement deleteUserStatement;
    /**
     * User's prepared statement for retrieve user.
     */
    private PreparedStatement getUserStatement;

    /**
     *
     * @return prepared statement for retrieve all users
     */
    public PreparedStatement getGetUserStatement() {
        return getUserStatement;
    }

    /**
     *
     * @param getUserStatement prepared statement to set
     */
    public void setGetUserStatement(final PreparedStatement getUserStatement) {
        this.getUserStatement = getUserStatement;
    }

    /**
     *
     * @return prepared statement for delete user
     */
    public PreparedStatement getDeleteUserStatement() {
        return deleteUserStatement;
    }

    /**
     *
     * @param deleteUserStatement prepared statement to set
     */
    public void setDeleteUserStatement(final PreparedStatement deleteUserStatement) {
        this.deleteUserStatement = deleteUserStatement;
    }

    /**
     *
     * @return prepared statement for create user
     */
    public PreparedStatement getCreateUserStatement() {
        return createUserStatement;
    }

    /**
     *
     * @param createUserStatement prepared statement to set
     */
    public void setCreateUserStatement(final PreparedStatement createUserStatement) {
        this.createUserStatement = createUserStatement;
    }

    /**
     *
     * @return connection to DB
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     *
     * @param connection connection to set
     */
    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    /**
     *
     * @return prepared statement for retrieve all notes
     */
    public PreparedStatement getNoteStatement() {
        return noteStatement;
    }

    /**
     *
     * @param noteStatement prepared statement to set
     */
    public void setNoteStatement(final PreparedStatement noteStatement) {
        this.noteStatement = noteStatement;
    }

    /**
     *
     * @return prepared statement for delete note
     */
    public PreparedStatement getDeleteNoteStatement() {
        return deleteNoteStatement;
    }

    /**
     *
     * @param deleteNoteStatement prepared statement to set
     */
    public void setDeleteNoteStatement(final PreparedStatement deleteNoteStatement) {
        this.deleteNoteStatement = deleteNoteStatement;
    }

    /**
     *
     * @return prepared statement for add note
     */
    public PreparedStatement getAddNoteStatement() {
        return addNoteStatement;
    }

    /**
     *
     * @param addNoteStatement prepared statement to set
     */
    public void setAddNoteStatement(final PreparedStatement addNoteStatement) {
        this.addNoteStatement = addNoteStatement;
    }

    /**
     *
     * @return prepared statement for retrieve user
     */
    public PreparedStatement getUserStatement() {
        return userStatement;
    }

    /**
     *
     * @param userStatement prepared statement to set
     */
    public void setUserStatement(final PreparedStatement userStatement) {
        this.userStatement = userStatement;
    }
}
