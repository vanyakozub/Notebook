package servlet;

import dao.DBHelper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.sql.SQLException;
@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            //start database
            Class.forName("org.postgresql.Driver");
            DBHelper.init();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        try {
            //stop database
            if (DBHelper.getInitialConnection() != null) {
                DBHelper.getInitialConnection().close();
            }
        } catch (SQLException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
