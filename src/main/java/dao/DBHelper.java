package dao;

import domain.Note;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class DBHelper {
    static Connection conn;

    public static Connection getInitialConnection() {
        return initialConnection;
    }

    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/notebookdb";
    //static final String DATABASE_CREATE_URL = DATABASE_URL + ";create=true";

    static final int NUMBER_OF_CONNECTIONS = 30;
    static volatile int currentConnection = 0;

    private static CacheLoader<Integer, ConnectionHolder> loader;
    private static LoadingCache<Integer, ConnectionHolder> cache;
    private static RemovalListener<Integer, ConnectionHolder> listener;

    private static Connection initialConnection;

    static final String NOTEBOOK = "notebook";
    static final String NOTE_HEADER = "header";
    static final String NOTE_OWNER_NAME = "name";
    static final String NOTE_DESCRIPTION = "description";
    static final String ID = "id";
    static final String USERS = "users";
    static final String FIRSTNAME = "firstname";
    static final String LASTNAME = "lastname";
    static final String EMAIL = "email";
    static final String SELECT_NOTE_SQL = "SELECT " + NOTE_HEADER + ",  " + NOTE_OWNER_NAME + ",  " + NOTE_DESCRIPTION + ",  " + EMAIL + " FROM " + NOTEBOOK;
    static final String DELETE_NOTE_SQL = "DELETE FROM " + NOTEBOOK + " WHERE " + ID + " = ?";
    static final String SELECT_USER_SQL = "SELECT " + FIRSTNAME + ",  " + LASTNAME + ",  " + EMAIL +" FROM " + USERS + " WHERE " + EMAIL + " = ?";
    static final String ADD_NOTE_SQL = "INSERT INTO " + NOTEBOOK + " (" + NOTE_HEADER + ", " + NOTE_OWNER_NAME +", " + NOTE_DESCRIPTION + ", " + EMAIL + ") VALUES (?,?,?,?)";
    public static void init() throws IOException, SQLException {
        System.out.println("init - Instead of @PostConstruct");
        //Init cache loader. Code is used to create new connections and statements
        loader = new CacheLoader<Integer, ConnectionHolder>() {
            @Override
            public ConnectionHolder load(Integer key) {
                try {
                    ConnectionHolder connectionHolder = new ConnectionHolder();
                    System.out.println("Creating new ConnectionHolder for key " + key);
                    connectionHolder.setConnection(DriverManager.getConnection(DATABASE_URL, "postgres", "0000"));
                    connectionHolder.getConnection().setAutoCommit(false);

                    connectionHolder.setAddNoteStatement(connectionHolder.getConnection().prepareStatement(ADD_NOTE_SQL, new String[]{"id"}));


                    return connectionHolder;
                } catch (SQLException e) {
                    System.out.println("Exception getting connection to database " + e);
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
                            System.out.println("Closing old connection for key " + n.getKey());
                            n.getValue().getNoteStatement().close();
                            n.getValue().getConnection().close();
                        }
                    } catch (SQLException ex) {
                        System.out.println("Exception closing connection to database " + ex);
                    }
                }
            }
        };

        cache = CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).removalListener(listener).build(loader);
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
                    System.out.println("Insert into Notebookad executed");
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
}
