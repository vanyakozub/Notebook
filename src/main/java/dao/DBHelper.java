package dao;

import domain.Note;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import domain.User;

public class DBHelper {
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/notebookdb";

    public static Connection getInitialConnection() {
        return initialConnection;
    }

    private static Connection initialConnection;

    static final int NUMBER_OF_CONNECTIONS = 30;
    static volatile int currentConnection = 0;

    private static CacheLoader<Integer, ConnectionHolder> loader;
    private static LoadingCache<Integer, ConnectionHolder> cache;
    private static RemovalListener<Integer, ConnectionHolder> listener;


    static final String NOTEBOOK = "notebook";
    static final String NOTE_HEADER = "header";
    static final String NOTE_OWNER_NAME = "name";
    static final String NOTE_DESCRIPTION = "description";
    static final String ID = "id";
    static final String USERS = "users";
    static final String FIRSTNAME = "firstname";
    static final String LASTNAME = "lastname";
    static final String EMAIL = "email";
    static final String PASSWORD = "password";
    static final String SELECT_NOTE_SQL = "SELECT " +ID +", " + NOTE_HEADER + ",  " + NOTE_OWNER_NAME + ",  " + NOTE_DESCRIPTION + ",  " + EMAIL + " FROM " + NOTEBOOK;
    static final String DELETE_NOTE_SQL = "DELETE FROM " + NOTEBOOK + " WHERE " + ID + " = ?";
    static final String SELECT_USER_SQL = "SELECT " + FIRSTNAME + ",  " + LASTNAME + ",  " + EMAIL+", " + PASSWORD + " FROM " + USERS + " WHERE " + EMAIL + " = ?";
    static final String ADD_NOTE_SQL = "INSERT INTO " + NOTEBOOK + " (" + NOTE_HEADER + ", " + NOTE_OWNER_NAME +", " + NOTE_DESCRIPTION + ", " + EMAIL + ") VALUES (?,?,?,?)";
    static final String CREATE_USER_SQL = "INSERT INTO " +USERS+ " ("+ FIRSTNAME + ",  " + LASTNAME + ",  " + EMAIL + ", password) VALUES (?,?,?,?)";
    static final String DELETE_USER_SQL = "DELETE FROM " + USERS + " WHERE " + EMAIL + " = ?";
    static final String SELECT_ALL_USERS = "SELECT * FROM " + USERS;
    public static void init() throws IOException, SQLException {
        System.out.println("init - Instead of @PostConstruct");
        //Init cache loader. Code is used to create new connections and statements
        loader = new CacheLoader<Integer, ConnectionHolder>() {
            @Override
            public ConnectionHolder load(Integer key) {
                try {
                    ConnectionHolder connectionHolder = new ConnectionHolder();
                    //System.out.println("Creating new ConnectionHolder for key " + key);
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
                    //System.out.println("Exception getting connection to database " + e);
                    return null;
                }
            }
        };

        //Eviction listener. Code is used to close expired connections
        listener = new RemovalListener<Integer, ConnectionHolder>() {
            @Override
            public void onRemoval(RemovalNotification<Integer, ConnectionHolder> n) {
                if (n.wasEvicted()) {
                    try {
                        synchronized (n.getValue().getConnection()) {
                            //System.out.println("Closing old connection for key " + n.getKey());
                            n.getValue().getNoteStatement().close();
                            n.getValue().getConnection().close();
                        }
                    } catch (SQLException ex) {
                        //System.out.println("Exception closing connection to database " + ex);
                    }
                }
            }
        };
        cache = CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).removalListener(listener).build(loader);
    }

    private static PreparedStatement getNoteStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getNoteStatement();
    }

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

    private static PreparedStatement getDeleteNoteStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getDeleteNoteStatement();
    }

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

        //System.out.println("Note with id= " + id + " has been deleted");
    }
    private static PreparedStatement getDeleteUserStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getDeleteUserStatement();
    }

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

        //System.out.println("Note with id= " + id + " has been deleted");
    }

    private static PreparedStatement getAddNoteStatement() throws SQLException {
        return cache.getUnchecked(currentConnection).getAddNoteStatement();
    }

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
                    //System.out.println("Insert into Notebook executed");
                    rs.close();
                    stmt.getConnection().commit();
                    return new Note(id, header, firstName,description,email);
                }
            } catch (Exception e) {
                stmt.getConnection().rollback();
                throw e;
            }
        }
    }
    private static PreparedStatement getCreateUserStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getCreateUserStatement();
    }
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
                /*try (ResultSet rs = rs1) {
                    rs.next();
                    Integer id = rs.getInt(1);
                    System.out.println("Insert into User executed");



                }*/
            } catch (Exception e) {
                stmt.getConnection().rollback();
                throw e;
            }
        }
    }
    private static PreparedStatement getUserStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getGetUserStatement();
    }
    public static User getUserByEmail (String email) throws SQLException {
        PreparedStatement stmt = getUserStatement();
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String firstName = rs.getString(FIRSTNAME);
        String lastName = rs.getString(LASTNAME);
        String mail = rs.getString(EMAIL);
        String password = rs.getString(PASSWORD);
        return  new User(firstName, lastName, mail, password);
    }

    private static PreparedStatement getAllUsersStatement() throws SQLException {
        currentConnection = (currentConnection + 1) % NUMBER_OF_CONNECTIONS;
        return cache.getUnchecked(currentConnection).getUserStatement();
    }

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
        }
        return result;
    }
}
