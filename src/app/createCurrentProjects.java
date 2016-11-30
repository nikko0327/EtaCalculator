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
@WebServlet("/uploadServletCurrent")

public class createCurrentProjects extends HttpServlet implements mysql_credentials {
    //    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer;
    private String jira;
    private String dc;
    private String data_size;
    private String import_engr;
    private String tem;
    private String current_stage;
    private String created_date;
    private String notes;
    private String appliance_count;
    private String is_completed;


    InputStream inputStream;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createCurrentProjects() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        customer = request.getParameter("customer");
        jira = request.getParameter("jira");
        dc = request.getParameter("dc");
        data_size = request.getParameter("data_size");
        import_engr = request.getParameter("import_engr");
        tem = request.getParameter("tem");
        current_stage = request.getParameter("current_stage");
        created_date = request.getParameter("created_date");
        notes = request.getParameter("notes");
        appliance_count = request.getParameter("appliance_count");
        is_completed = request.getParameter("is_completed");

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

            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(db_url, user_name, password);

            //Query to count appliances START
            String query_appliance = ""; //Empty variable for appliance count query
            query_appliance = "select * from appliance_assignment"; //Setting query for appliance count
            System.out.println("Search drive: " + query_appliance); //Test run
            PreparedStatement prepSearchDriveStmtAppliance = connect.prepareStatement(query_appliance); //Statement to execute
            ResultSet rsAppliance = prepSearchDriveStmtAppliance.executeQuery(); //Execute statement
            int counter = 0;
            while(rsAppliance.next()){ //Go through every row and add 1 to counter
                counter++;
            }
            System.out.println("Appliance Count: " + counter); //Test run
            //Query to count appliances END

            //Counting appliances in use START
            String query_count = ""; //Empty variable for appliance count query
            query_count = "select * from current_project"; //Setting query for appliance count
            System.out.println("Search drive: " + query_count); //Test run
            PreparedStatement prepSearchDriveStmtCount = connect.prepareStatement(query_count); //Statement to execute
            ResultSet rsCount = prepSearchDriveStmtCount.executeQuery(); //Execute statement
            int counterUsed = 0;
            while(rsCount.next()){ //Go through every row and add 1 to counter
                int applianceCount = Integer.parseInt(rsCount.getString("appliance_count"));
                counterUsed = counterUsed + applianceCount;
            }
            System.out.println("Counter USED: " + counterUsed); //Test run
            //Counting appliances in used END

            int counterAvailable = counter - counterUsed;
            System.out.println("Counter Available: "  + counterAvailable);

            String query_createSprint;

            int checkLimit = counterAvailable + Integer.parseInt(appliance_count);
            System.out.println("CHECK SUM: " + checkLimit);

            if(Integer.parseInt(appliance_count) <= counterAvailable){
                query_createSprint = "insert into current_project (customer, jira, dc, data_size, import_engr, tem, current_stage, created_date, notes, appliance_count, is_completed) values (?,?,?,?,?,?,?,?,?,?,?);";


                PreparedStatement prepCreateSprintStmt = connect.prepareStatement(query_createSprint);

                prepCreateSprintStmt.setString(1, customer);
                prepCreateSprintStmt.setString(2, jira);
                prepCreateSprintStmt.setString(3, dc);
                prepCreateSprintStmt.setString(4, data_size);
                prepCreateSprintStmt.setString(5, import_engr);
                prepCreateSprintStmt.setString(6, tem);
                prepCreateSprintStmt.setString(7, current_stage);
                prepCreateSprintStmt.setString(8, created_date);
                prepCreateSprintStmt.setString(9, notes);
                prepCreateSprintStmt.setString(10, appliance_count);
                prepCreateSprintStmt.setString(11, is_completed);

                int createSprintStmtRes = prepCreateSprintStmt.executeUpdate();

                System.out.println("Create drive: " + query_createSprint);

                result = true;

                prepCreateSprintStmt.close();
            } else {
                System.out.println("Can't Add More!!!");
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

        return result;
    }

    private String getCreatedDrive() {

        JSONObject json = new JSONObject();
        Connection connect = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(db_url, user_name, password);

            String query_getDriveById = "select * from current_project where customer ='" + customer + "';";

            PreparedStatement prepStmt = connect
                    .prepareStatement(query_getDriveById);
            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) {
                json.put("customer", customer);
                json.put("jira", jira);
                json.put("dc", dc);
                json.put("data_size", data_size);
                json.put("import_engr", import_engr);
                json.put("tem", tem);
                json.put("current_stage", current_stage);
                json.put("created_date", created_date);
                json.put("notes", notes);
                json.put("appliance_count", appliance_count);
                json.put("is_completed", is_completed);
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