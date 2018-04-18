package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class updateCurrentProject extends HttpServlet {
    private static final long serialVersionUID = 1L;
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

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateCurrentProject() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
     * response)
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

        JSONObject json = new JSONObject();

        if (updateDriveAndAddToHistory()) {
            json.put("result", "success");
        } else
            json.put("result", eMessage);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
        response.flushBuffer();
    }

    public boolean updateDriveAndAddToHistory() {

        boolean result = false;
        Connection connect = null;
        try {
            connect = dataSource.getConnection();

            Date currentDatetime = new Date();
            Timestamp sqlTime = new Timestamp(currentDatetime.getTime());

            //Query to count appliances START
            String query_appliance = ""; //Empty variable for appliance count query
            query_appliance = "select * from appliance_assignment"; //Setting query for appliance count
            System.out.println("Search drive: " + query_appliance); //Test run
            PreparedStatement prepSearchDriveStmtAppliance = connect.prepareStatement(query_appliance); //Statement to execute
            ResultSet rsAppliance = prepSearchDriveStmtAppliance.executeQuery(); //Execute statement
            int counter = 0;
            while (rsAppliance.next()) { //Go through every row and add 1 to counter
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
            while (rsCount.next()) { //Go through every row and add 1 to counter
                int applianceCount = Integer.parseInt(rsCount.getString("appliance_count"));
                counterUsed = counterUsed + applianceCount;
            }
            System.out.println("Counter USED: " + counterUsed); //Test run
            //Counting appliances in used END

            int counterAvailable = counter - counterUsed;
            System.out.println("Counter Available: " + counterAvailable);

            String query_createSprint;

            int checkLimit = counterAvailable + Integer.parseInt(appliance_count);
            System.out.println("CHECK SUM: " + checkLimit);


            if (Integer.parseInt(appliance_count) <= counterAvailable) {

                String query_updateDrive = "update current_project set customer = ?, jira = ?, dc = ?, data_size = ?, import_engr = ?, tem = ?, current_stage = ?, created_date = ?, notes = ?, appliance_count = ?, is_completed = ? " +
                        "where customer = '" + customer + "'";

                PreparedStatement prepUpdateDriveStmt = connect.prepareStatement(query_updateDrive);
                prepUpdateDriveStmt.setString(1, customer);
                prepUpdateDriveStmt.setString(2, jira);
                prepUpdateDriveStmt.setString(3, dc);
                prepUpdateDriveStmt.setString(4, data_size);
                prepUpdateDriveStmt.setString(5, import_engr);
                prepUpdateDriveStmt.setString(6, tem);
                prepUpdateDriveStmt.setString(7, current_stage);
                prepUpdateDriveStmt.setString(8, created_date);
                prepUpdateDriveStmt.setString(9, notes);
                prepUpdateDriveStmt.setString(10, appliance_count);
                prepUpdateDriveStmt.setString(11, is_completed);

                int updateRes = prepUpdateDriveStmt.executeUpdate();

                System.out.println("Update Assignment: " + query_updateDrive);

                String query_selectDriveById = "select * from current_project where customer = '" + customer + "'";
                PreparedStatement prepSelectDriveStmt = connect.prepareStatement(query_selectDriveById);
                ResultSet selectDriveRes = prepSelectDriveStmt.executeQuery();

                result = true;

                prepSelectDriveStmt.close();
                prepUpdateDriveStmt.close();
            } else {
                System.out.println("Can't Add More!!!");
            }

        } catch (SQLException e) {
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

        return result;
    }


}





