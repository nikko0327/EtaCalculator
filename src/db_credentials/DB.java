package db_credentials;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DB {

    public static Connection getConnection() throws SQLException, NamingException {
//        Context initContext = new InitialContext();
//        Context envContext = (Context) initContext.lookup("java:comp/env");
//        DataSource ds = (DataSource) envContext.lookup("jdbc/EtaCalculatorDB");
//        Connection conn = ds.getConnection();
//        return conn;
        return null;
    }
}