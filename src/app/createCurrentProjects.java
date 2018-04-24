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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/uploadServletCurrent")

public class createCurrentProjects extends HttpServlet {
    //    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer;
    private String jira;
    private String dc;
    private int data_size;
    private String import_engr;
    private String tem;
    private String current_stage;
    private Date created_date;
    private String notes;
    private int appliance_count;
    private boolean is_completed;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    InputStream inputStream;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createCurrentProjects() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

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
            is_completed = false;

            String latestDrive;

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (createNewCurrentProject()) {
                latestDrive = searchCurrentProjects();

                response.getWriter().write(latestDrive);
                //System.out.println("latest drive = " + latestDrive);
            } else {
                JSONObject json = new JSONObject();
                json.put("message", eMessage);
                response.getWriter().write(json.toString());
            }

            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {

        }
    }

    private boolean createNewCurrentProject() {
        boolean result = false;
        Connection connect = null;
        PreparedStatement psNewCurrentProject = null;

        try {
            java.sql.Date now = now();

            connect = dataSource.getConnection();

            // Query to count appliances START
            int counter = db_credentials.DB.getApplianceCount(connect);
            System.out.println("Appliance Count: " + counter); //Test run
            //Query to count appliances END

            //Counting appliances in use START
            int counterUsed = db_credentials.DB.getAppliancesInUseCount(connect);
            System.out.println("Appliances in use: " + counter); //Test run
            //Counting appliances in used END

            int counterAvailable = counter - counterUsed;
            System.out.println("Counter Available: " + counterAvailable);

            String query_createSprint;

            int checkLimit = counterAvailable + appliance_count;
            System.out.println("CHECK SUM: " + checkLimit);

            if (appliance_count <= counterAvailable) {
                query_createSprint = "insert into current_project (customer, jira, dc, data_size, import_engr, tem, current_stage, created_date, notes, appliance_count, is_completed) values (?,?,?,?,?,?,?,?,?,?,?);";

                psNewCurrentProject = connect.prepareStatement(query_createSprint);

                psNewCurrentProject.setString(1, customer);
                psNewCurrentProject.setString(2, jira);
                psNewCurrentProject.setString(3, dc);
                psNewCurrentProject.setInt(4, data_size);
                psNewCurrentProject.setString(5, import_engr);
                psNewCurrentProject.setString(6, tem);
                psNewCurrentProject.setString(7, current_stage);
                psNewCurrentProject.setDate(8, created_date);
                psNewCurrentProject.setString(9, notes);
                psNewCurrentProject.setInt(10, appliance_count);
                psNewCurrentProject.setBoolean(11, is_completed);

                psNewCurrentProject.executeUpdate();

                System.out.println("Create drive: " + query_createSprint);

                result = true;
            } else {
                System.out.println("Can't Add More!!!");
                eMessage = "Appliance count used for this project exceeds available appliances available.";
            }
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psNewCurrentProject);
        }

        return result;
    }

    private String searchCurrentProjects() {

        JSONObject json = new JSONObject();
        Connection connect = null;
        PreparedStatement psSearchCurrentProjects = null;
        ResultSet rs = null;

        try {
            connect = dataSource.getConnection();

            String query_getDriveById = "select * from current_project where customer ='" + customer + "';";

            psSearchCurrentProjects = connect.prepareStatement(query_getDriveById);
            rs = psSearchCurrentProjects.executeQuery();

            while (rs.next()) {
                json.put("customer", customer);
                json.put("jira", jira);
                json.put("dc", dc);
                json.put("data_size", "" + data_size);
                json.put("import_engr", import_engr);
                json.put("tem", tem);
                json.put("current_stage", current_stage);
                json.put("created_date", now().toString());
                json.put("notes", notes);
                json.put("appliance_count", "" + appliance_count);
                json.put("is_completed", (is_completed) ? "Yes" : "No");
            }
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psSearchCurrentProjects, rs);
        }

        return json.toString();
    }

    public Date stringToDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(format.parse(date).getTime());
    }

    public static Date now() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlNow = new Date(now.getTime());
        return sqlNow;
    }
}