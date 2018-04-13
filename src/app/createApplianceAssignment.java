package app;

import db_credentials.mysql_credentials;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

@WebServlet("/uploadServlet12")

public class createApplianceAssignment extends HttpServlet implements mysql_credentials {
    //    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String appliance;
    private String current;
    private String previous;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    InputStream inputStream;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createApplianceAssignment() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        appliance = request.getParameter("appliance");
        current = request.getParameter("current");
        previous = request.getParameter("previous");

        String latestDrive;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (createDriveAndHistory()) {
            latestDrive = getCreatedDrive();

            response.getWriter().write(latestDrive);
            //System.out.println("latest drive = " + latestDrive);
        } else {
            JSONObject json = new JSONObject();
            json.put("message", eMessage);
            response.getWriter().write(json.toString());
        }

        response.flushBuffer();
    }

    private boolean createDriveAndHistory() {

        boolean result = false;
        Connection connect = null;
        try {
            java.util.Date currentDatetime = new java.util.Date();
            java.sql.Timestamp sqlTime = new Timestamp(currentDatetime.getTime());

//            Class.forName("com.mysql.jdbc.Driver");
//            connect = DriverManager.getConnection(db_url, user_name, password);
            connect = dataSource.getConnection();

            String query_createSprint;

            query_createSprint = "insert into appliance_assignment (appliance, current, previous) values (?,?,?);";


            PreparedStatement prepCreateSprintStmt = connect.prepareStatement(query_createSprint);

            prepCreateSprintStmt.setString(1, appliance);
            prepCreateSprintStmt.setString(2, current);
            prepCreateSprintStmt.setString(3, previous);

            int createSprintStmtRes = prepCreateSprintStmt.executeUpdate();

            System.out.println("Create drive: " + query_createSprint);

            result = true;

            prepCreateSprintStmt.close();

        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            eMessage = e.getMessage();
//            e.printStackTrace();
//        }
        finally {
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                eMessage = se.getMessage();
                se.printStackTrace();
            }
        }

        return result;
    }

    private String getCreatedDrive() {

        JSONObject json = new JSONObject();
        Connection connect = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(db_url, user_name, password);

            String query_getDriveById = "select * from appliance_assignment where appliance ='" + appliance + "';";

            PreparedStatement prepStmt = connect
                    .prepareStatement(query_getDriveById);
            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) {
                json.put("appliance", appliance);
                json.put("current", current);
                json.put("previous", previous);
            }
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                eMessage = se.getMessage();
                se.printStackTrace();
            }
        }

        return json.toString();
    }
}