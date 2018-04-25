package app;

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

public class createApplianceAssignment extends HttpServlet {
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

        System.out.println("--- createApplianceAssignment ---");

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
        PreparedStatement prepCreateSprintStmt = null;
        try {
            java.util.Date currentDatetime = new java.util.Date();
            java.sql.Timestamp sqlTime = new Timestamp(currentDatetime.getTime());

            connect = dataSource.getConnection();

            String query_createSprint;

            query_createSprint = "insert into appliance_assignment (appliance, current, previous) values (?,?,?);";

            prepCreateSprintStmt = connect.prepareStatement(query_createSprint);

            prepCreateSprintStmt.setString(1, appliance);
            prepCreateSprintStmt.setString(2, current);
            prepCreateSprintStmt.setString(3, previous);

            int createSprintStmtRes = prepCreateSprintStmt.executeUpdate();

            System.out.println("Create drive: " + query_createSprint);

            result = true;
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
                if (prepCreateSprintStmt != null) {
                    prepCreateSprintStmt.close();
                }
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
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            connect = dataSource.getConnection();

            String query_getDriveById = "select * from appliance_assignment where appliance ='" + appliance + "';";

            prepStmt = connect.prepareStatement(query_getDriveById);
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                json.put("appliance", appliance);
                json.put("current", current);
                json.put("previous", previous);
            }
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) {
                    connect.close();
                }
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException se) {
                eMessage = se.getMessage();
                se.printStackTrace();
            }
        }

        return json.toString();
    }
}