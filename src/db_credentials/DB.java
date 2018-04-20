package db_credentials;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DB implements mysql_credentials {
    private static final boolean PRINT_STATUS = true;

    public static Connection getConnection() {

        Connection c = null;
        // Try using the DataSource first
        try {

            InitialContext init = new InitialContext();
            Context envContext = (Context) init.lookup("java:comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/EtaCalculatorDB");
            c = ds.getConnection();

            System.out.println("Connection via DataSource successful.");

        } catch (SQLException sql) {

            System.out.println("DataSource cannot retrieve a connection, trying DriverManager instead.");

            try {
                Class.forName("com.mysql.jdbc.Driver");
                c = DriverManager.getConnection(db_url, user_name, password);
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println("Both DataSource and DriverManager have failed to return a connection.");
                System.out.println("--- DataSource error: ---");
                sql.printStackTrace();
                System.out.println("--- DriveManager error: ---");
                ex.printStackTrace();
            } catch (Exception e) {
                System.out.println("Some other error has occurred with the DriverManager.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("getC");
            System.out.println("Some other error has occurred with the DataSource.");
            e.printStackTrace();
        } finally {

        }

        return c;
    }

    public static boolean closeResources(AutoCloseable... resources) {
        boolean allClosed = true;

        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception sql) {
                    sql.printStackTrace();
                    allClosed = false;
                }
            }
        }

        if (PRINT_STATUS) {
            if (allClosed) {
                // System.out.println("All DB resources closed successfully.");
            } else {
                System.out.println("Failed to close some DB resources.");
            }
        }

        return allClosed;
    }
}