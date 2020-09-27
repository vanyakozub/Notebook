package dao;

import domain.Note;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import domain.User;

public class DBHelper {
    /**
     * Constant. URL address of DB.
     */
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/notebookdb";
    /**
     * Constant. Initial connection.
     */
    private static Connection initialConnection;
    /**
     * Constant. Number of connections DB.
     */
    static final int NUMBER_OF_CONNECTIONS = 30;
    /**
     * Number of current connection.
     */
    static volatile int currentConnection = 0;
    /**
     * Cache loader.
     */
    private static CacheLoader<Integer, ConnectionHolder> loader;
    /**
     * Loading cache.
     */
    private static LoadingCache<Integer, ConnectionHolder> cache;
    /**
     * Removal Listener.
     */
    private static RemovalListener<Integer, ConnectionHolder> listener;
    /**
     * Constant. Name of table in DB.
     */
    static final String NOTEBOOK = "notebook";
    /**
     * Constant. Field in notebook.
     */
    static final String NOTE_HEADER = "header";
    /**
     * Constant. Field in notebook.
     */
    static final String NOTE_OWNER_NAME = "name";
    /**
     * Constant. Field in notebook.
     */
    static final String NOTE_DESCRIPTION = "description";
    /**
     * Constant. Field in notebook.
     */
    static final String ID = "id";
    /**
     * Constant. Name of table in DB.
     */
    static final String USERS = "users";
    /**
     * Constant. Field in users.
     */
    static final String FIRSTNAME = "firstname";
    /**
     * Constant. Field in users.
     */
    static final String LASTNAME = "lastname";
    /**
     * Constant. Field in users.
     */
    static final String EMAIL = "email";
    /**
     * Constant. Field in users.
     */
    static final String PASSWORD = "password";
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String SELECT_NOTE_SQL = "SELECT " + ID + ", " + NOTE_HEADER + ",  " + NOTE_OWNER_NAME + ",  "
            + NOTE_DESCRIPTION + ",  " + EMAIL + " FROM " + NOTEBOOK;
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String DELETE_NOTE_SQL = "DELETE FROM " + NOTEBOOK + " WHERE " + ID + " = ?";
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String SELECT_USER_SQL = "SELECT " + FIRSTNAME + ",  " + LASTNAME + ",  " + EMAIL +", "
            + PASSWORD + " FROM " + USERS + " WHERE " + EMAIL + " = ?";
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String ADD_NOTE_SQL = "INSERT INTO " + NOTEBOOK + " (" + NOTE_HEADER + ", "
            + NOTE_OWNER_NAME + ", " + NOTE_DESCRIPTION + ", " + EMAIL + ") VALUES (?,?,?,?)";
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String CREATE_USER_SQL = "INSERT INTO " + USERS + " ("+ FIRSTNAME + ",  " + LASTNAME + ",  "
            + EMAIL + ", password) VALUES (?,?,?,?)";
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String DELETE_USER_SQL = "DELETE FROM " + USERS + " WHERE " + EMAIL + " = ?";
    /**
     * Constant. Command for PreparedStatement.
     */
    static final String SELECT_ALL_USERS = "SELECT * FROM " + USERS;

    /**
     *
     * @return Initial connection
     */
    public static Connection getInitialConnection() {
        return initialConnection;
    }

    /**
     * Initialization of DBHelper
     * @throws SQLException
     */
    public static void init() throws SQLException {
        loader = new CacheLoader<Integer, ConnectionHolder>() {
            @Override
            public ConnectionHolder load(Integer key) {
                try {
                    ConnectionHolder connectionHolder = new ConnectionHolder();
                    connectionHolder.setConnection(DriverManager.getConnection(DATABASE_URL, "postgres", "0000"));
                    connectionHolder.getConnection().setAutoCommit(false);
                    connectionHolder.setNoteStatement(connectionHolder.getConnection().prepareStatement(SELECT_NOTE_SQL));
                    connectionHolder.setDeleteNoteStatement(connectionHolder.getConnection().prepareStatement(DELETE_NOTE_SQL));
                    connectionHolder.setAddNoteStatement(connectionHolder.getConnection().prepareStatement(ADD_NOTE_SQL, new String[]{"id"}));
                    connectionHolder.setCreateUserStatement(connectionHolder.getConnection().prepareStatement(CREATE_USER_SQL));
                    connectionHolder.setDeleteUserStatement(connectionHolder.getConnection().prepareStatement(DELETE_USER_SQL));
                    connectionHolder.setUserStatement(connectionHolder.getConnection().prepareStatement(SELECT_ALL_USERS));
                    connectionHolder.setGetUserStatement(connectionHolder.getConnection().prepareStatement(SELECT_USER_SQL));
                    return connectionHolder;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        listener = new RemovalListener<Integer, ConnectionHolder>() {
            @Override
            public void onRemoval(RemovalNotification<Integer, ConnectionHolder> n) {
                if (n.wasEvicted()) {
                    try {
                        synchronized (n.getValue().getConnection()) {
                            n.getValue().getNoteStatement().close();
                            n.getValue().getConnection().close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        cache = CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).removalListener(listener).build(loader);
    }

    /**
     *
     * @return PreparedStatement to get all notes
     * @throws SQLException
     */
    private static PreparedStatement getNoteStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getNoteStatement();
    }

    /**
     *
     * @return List of notes
     * @throws SQLException
     */
    public static List<Note> getAllNotes() throws SQLException {
        List<Note> result = new LinkedList<>();
        PreparedStatement stmt = getNoteStatement();
        synchronized (stmt.getConnection()) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(ID);
                    String header = rs.getString(NOTE_HEADER);
                    String name = rs.getString(NOTE_OWNER_NAME);
                    String description = rs.getString(NOTE_DESCRIPTION);
                    String email = rs.getString(EMAIL);
                    Note tempNote = new Note(id, header, name, description, email);
                    result.add(tempNote);
                }
            }
            return result;
        }
    }

    /**
     *
     * @return PreparedStatement to delete all notes
     * @throws SQLException
     */
    private static PreparedStatement getDeleteNoteStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getDeleteNoteStatement();
    }

    /**
     *
     * @param id Note's id which need to be deleted
     * @throws SQLException
     */
    public static void deleteNote(Integer id) throws SQLException {
        PreparedStatement stmt = getDeleteNoteStatement();
        synchronized (stmt.getConnection()) {
            try {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.getConnection().commit();
            } catch (Exception e) {
                stmt.getConnection().rollback();
                throw e;
            }
        }
    }

    /**
     *
     * @return PreparedStatement to delete user
     * @throws SQLException
     */
    private static PreparedStatement getDeleteUserStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getDeleteUserStatement();
    }

    /**
     *
     * @param email User's email which need to be deleted
     * @throws SQLException
     */
    public static void deleteUser(String email) throws SQLException {

        PreparedStatement stmt = getDeleteUserStatement();

        synchronized (stmt.getConnection()) {
            try {
                stmt.setString(1, email);
                stmt.executeUpdate();
                stmt.getConnection().commit();
            } catch (Exception e) {
                stmt.getConnection().rollback();
                throw e;
            }
        }
    }

    /**
     *
     * @return PreparedStatement to get add note
     * @throws SQLException
     */
    private static PreparedStatement getAddNoteStatement() throws SQLException {
        return cache.getUnchecked(currentConnection).getAddNoteStatement();
    }

    /**
     *
     * @param header Note's header to add
     * @param firstName Note's creator name
     * @param description Note' description to add
     * @param email Note's creator email
     * @return Note
     * @throws SQLException
     */
    public static Note addNote(String header, String firstName, String description, String email) throws SQLException {

        PreparedStatement stmt = getAddNoteStatement();
        ResultSet rs1;
        synchronized (stmt.getConnection()) {
            try {
                stmt.setString(1, header);
                stmt.setString(2, firstName);
                stmt.setString(3, description);
                stmt.setString(4, email);
                stmt.executeUpdate();
                rs1 = stmt.getGeneratedKeys();

                try (ResultSet rs = rs1) {
                    rs.next();
                    Integer id = rs.getInt(1);
                    rs.close();
                    stmt.getConnection().commit();
                    return new Note(id, header, firstName, description, email);
                }
            } catch (Exception e) {
                stmt.getConnection().rollback();
                throw e;
            }
        }
    }

    /**
     *
     * @return PreparedStatement to create user
     * @throws SQLException
     */
    private static PreparedStatement getCreateUserStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getCreateUserStatement();
    }

    /**
     *
     * @param name user's name to create
     * @param lastName user's last name to create
     * @param email user's email to create
     * @param password user's password to create
     * @return user
     * @throws SQLException
     */
    public static User createUser(String name, String lastName, String email, String password) throws SQLException {
        PreparedStatement stmt = getCreateUserStatement();
        ResultSet rs1;
        synchronized (stmt.getConnection()) {
            try {
                stmt.setString(1, name);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.executeUpdate();
                rs1 = stmt.getGeneratedKeys();
                rs1.close();
                stmt.getConnection().commit();
                return new User(name, lastName, email, password);
            } catch (Exception e) {
                stmt.getConnection().rollback();
                throw e;
            }
        }
    }

    /**
     *
     * @return PreparedStatement to get user by email
     * @throws SQLException
     */
    private static PreparedStatement getUserStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getGetUserStatement();
    }

    /**
     *
     * @param email user's email
     * @return user
     * @throws SQLException
     */
    public static User getUserByEmail (String email) throws SQLException {
        PreparedStatement stmt = getUserStatement();
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        String firstName = new String();
        String lastName = new String();
        String mail = new String();
        String password = new String();
        while (rs.next()) {
            firstName = rs.getString(FIRSTNAME);
            lastName = rs.getString(LASTNAME);
            mail = rs.getString(EMAIL);
            password = rs.getString(PASSWORD);
        }
        rs.close();
        return  new User(firstName, lastName, mail, password);
    }

    /**
     *
     * @return PreparedStatement to get all users
     * @throws SQLException
     */
    private static PreparedStatement getAllUsersStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getUserStatement();
    }

    /**
     *
     * @return list of users
     * @throws SQLException
     */
    public static List<User> getAllUsers () throws SQLException {
        List<User> result = new LinkedList<>();
        PreparedStatement stmt = getAllUsersStatement();
        ResultSet rs;
        synchronized (stmt.getConnection()) {
            rs = stmt.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString(FIRSTNAME);
                String lastName = rs.getString(LASTNAME);
                String email = rs.getString(EMAIL);
                String password = rs.getString(PASSWORD);
                User user = new User(firstName, lastName, email, password);
                result.add(user);
            }
            rs.close();
        }
        return result;
    }
}
