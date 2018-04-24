package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class updateCurrentProject extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer;
    private String jira;
    private String dc;
    private int data_size;
    private String import_engr;
    private String tem;
    private String current_stage;
    private java.sql.Date created_date;
    private String notes;
    private int appliance_count;
    private boolean is_completed;

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
                          HttpServletResponse response) throws ServletException {

        try {
            customer = request.getParameter("customer");
            jira = request.getParameter("jira");
            dc = request.getParameter("dc");
            data_size = Integer.parseInt(request.getParameter("data_size"));
            import_engr = request.getParameter("import_engr");
            tem = request.getParameter("tem");
            current_stage = request.getParameter("current_stage");
            created_date = stringToDate(request.getParameter("created_date"));
            notes = request.getParameter("notes");
            appliance_count = Integer.parseInt(request.getParameter("appliance_count"));
            is_completed = (request.getParameter("is_completed").equals("Yes")) ? true : false;

            JSONObject json = new JSONObject();


            if (updateCurrentProject()) {
                json.put("result", "success");
            } else
                json.put("result", eMessage);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public boolean updateCurrentProject() {

        boolean result = false;
        Connection connect = null;
        PreparedStatement psUpdateCurrentProject = null;

        try {
            connect = dataSource.getConnection();

            //Date now = now();

            //Query to count appliances
            int counter = db_credentials.DB.getApplianceCount(connect);
            System.out.println("Appliances total: " + counter);
            // ./Query to count appliances

            //Counting appliances in use
            int counterUsed = db_credentials.DB.getAppliancesInUseCount(connect);
            System.out.println("Appliances in use: " + counterUsed); //Test run
            // ./Counting appliances in used
            //Query to count appliances END

            int counterAvailable = counter - counterUsed;
            System.out.println("Counter Available: " + counterAvailable);

            int checkLimit = counterAvailable + appliance_count;
            System.out.println("CHECK SUM: " + checkLimit);

            if (appliance_count <= counterAvailable) {

                String query_updateDrive = "update current_project set customer = ?, jira = ?, dc = ?, data_size = ?, import_engr = ?, tem = ?, current_stage = ?, created_date = ?, notes = ?, appliance_count = ?, is_completed = ? " +
                        "where customer = '" + customer + "'";

                psUpdateCurrentProject = connect.prepareStatement(query_updateDrive);
                psUpdateCurrentProject.setString(1, customer);
                psUpdateCurrentProject.setString(2, jira);
                psUpdateCurrentProject.setString(3, dc);
                psUpdateCurrentProject.setInt(4, data_size);
                psUpdateCurrentProject.setString(5, import_engr);
                psUpdateCurrentProject.setString(6, tem);
                psUpdateCurrentProject.setString(7, current_stage);
                psUpdateCurrentProject.setDate(8, created_date);
                psUpdateCurrentProject.setString(9, notes);
                psUpdateCurrentProject.setInt(10, appliance_count);
                psUpdateCurrentProject.setBoolean(11, is_completed);

                psUpdateCurrentProject.executeUpdate();

                System.out.println("Update Assignment: " + query_updateDrive);

//                String query_selectDriveById = "select * from current_project where customer = '" + customer + "'";
//                PreparedStatement prepSelectDriveStmt = connect.prepareStatement(query_selectDriveById);
//                rs = prepSelectDriveStmt.executeQuery();

                result = true;
            } else {
                System.out.println("Can't add more!!!");
                eMessage = "Appliance count used for this project exceeds available appliances available.";
            }
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psUpdateCurrentProject);
        }

        return result;
    }

    public java.sql.Date stringToDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new java.sql.Date(format.parse(date).getTime());
    }

    public Date now() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlNow = new Date(now.getTime());
        return sqlNow;
    }
}





