package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class ConnectionHolder {
    Connection connection;
    PreparedStatement noteStatement;
    PreparedStatement deleteNoteStatement;
    PreparedStatement addNoteStatement;
    PreparedStatement userStatement;
}
