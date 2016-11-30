package app;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import db_credentials.mysql_credentials;
@WebServlet("/createUpcomingProject")
public class createUpcomingProjects extends HttpServlet implements mysql_credentials {
    //    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer_name;
    private String sow_created_date;
    private String expected;
    private String estimated_size;
    private String jira;
    private String dc;
    private String tem;
    private String notes;
    private String expected_start_month;
    private String expected_end_month;
    private String updated_date;
    private String apps_needed;
    private String lastUpdated;


    InputStream inputStream;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createUpcomingProjects() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        customer_name = request.getParameter("customer_name");
        sow_created_date = request.getParameter("sow_created_date");
        expected = request.getParameter("expected");
        estimated_size = request.getParameter("estimated_size");
        jira = request.getParameter("jira");
        dc = request.getParameter("dc");
        tem = request.getParameter("tem");
        notes = request.getParameter("notes");
        expected_start_month = request.getParameter("expected_start_month");
        expected_end_month = request.getParameter("expected_end_month");
        updated_date = request.getParameter("updated_date");
        apps_needed = request.getParameter("apps_needed");


        String latestDrive;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (createDriveAndHistory()) {
            latestDrive = getCreatedDrive();

            response.getWriter().write(latestDrive);
            //System.out.println("latest drive = " + latestDrive);
        }
        else {
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
            lastUpdated = sqlTime.toString();

            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(db_url, user_name, password);

            String query_createSprint;

            query_createSprint = "insert into upcoming_sow (customer_name, sow_created_date, estimated_size, jira, dc, tem, notes, expected_start_month, expected_end_month, updated_date, apps_needed) values (?,?,?,?,?,?,?,?,?,?,?);";


            PreparedStatement prepCreateSprintStmt = connect.prepareStatement(query_createSprint);

            prepCreateSprintStmt.setString(1, customer_name);
            prepCreateSprintStmt.setTimestamp(2, sqlTime);
            prepCreateSprintStmt.setString(3, estimated_size);
            prepCreateSprintStmt.setString(4, jira);
            prepCreateSprintStmt.setString(5, dc);
            prepCreateSprintStmt.setString(6, tem);
            prepCreateSprintStmt.setString(7, notes);
            prepCreateSprintStmt.setString(8, expected_start_month);
            prepCreateSprintStmt.setString(9, expected_end_month);
            prepCreateSprintStmt.setTimestamp(10, sqlTime);
            prepCreateSprintStmt.setString(11, apps_needed);

            int createSprintStmtRes = prepCreateSprintStmt.executeUpdate();

            System.out.println("Create drive: " + query_createSprint);


            result = true;

            prepCreateSprintStmt.close();
//            prepCreateHistoryStmt.close();

//            sendEmailNotification();

        } catch(SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if(connect != null)
                    connect.close();
            } catch(SQLException se) {
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

            java.util.Date currentDatetime = new java.util.Date();
            java.sql.Timestamp sqlTime = new Timestamp(currentDatetime.getTime());
            lastUpdated = sqlTime.toString();

            String query_getDriveById = "select * from upcoming_sow where customer_name ='" + customer_name + "';";

            PreparedStatement prepStmt = connect
                    .prepareStatement(query_getDriveById);
            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) {
                json.put("customer_name", customer_name);
                json.put("sow_created_date", sqlTime);
                json.put("estimated_size", estimated_size);
                json.put("jira", jira);
                json.put("dc", dc);
                json.put("tem", tem);
                json.put("notes", notes);
                json.put("expected_start_month", expected_start_month);
                json.put("expected_end_month", expected_end_month);
                json.put("updated_date", sqlTime);
                json.put("apps_needed", apps_needed);
            }
        } catch(SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if(connect != null)
                    connect.close();
            } catch(SQLException se) {
                eMessage = se.getMessage();
                se.printStackTrace();
            }
        }

        return json.toString();
    }
}