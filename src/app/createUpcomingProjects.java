package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/createUpcomingProject")
public class createUpcomingProjects extends HttpServlet {
    // private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer_name;
    private Date sow_created_date;
    // private Date expected; // never used
    private int estimated_size;
    private String jira;
    private String dc;
    private String tem;
    private String notes;
    private Date expected_start_month;
    private Date expected_end_month;
    private String updated_date; // not used, local time generator used.
    private int apps_needed;
    // private String lastUpdated;
    private JSONObject newestCreatedUpcomingProjectJSON;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    InputStream inputStream;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createUpcomingProjects() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException {

        System.out.println("--- createUpcomingProjects ---");

        try {
            customer_name = request.getParameter("customer_name");
            // sow_created_date = request.getParameter("sow_created_date");
            sow_created_date = new Date(new java.util.Date().getTime()); // Right now
            // expected = request.getParameter("expected");
            estimated_size = (int) Double.parseDouble(request.getParameter("estimated_size"));
            jira = request.getParameter("jira");
            dc = request.getParameter("dc");
            tem = request.getParameter("tem");
            notes = request.getParameter("notes");
            expected_start_month = stringToDate(request.getParameter("expected_start_month"));
            expected_end_month = stringToDate(request.getParameter("expected_end_month"));

            // updated_date = request.getParameter("updated_date");
            apps_needed = (request.getParameter("apps_needed") == null) ? 0 : Integer.parseInt(request.getParameter("apps_needed"));

            String latestDrive;

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject json = new JSONObject();

            if (expected_start_month.after(expected_end_month)) {
                eMessage = "The expected start date is after the expected end date.";
                json.put("message", eMessage);
                response.getWriter().write(json.toString());
                response.flushBuffer();
                return;
            } else if (createUpcomingProject()) {
                //latestDrive = getCreatedUpcomingProject();
                //response.getWriter().write(latestDrive);
                response.getWriter().write(newestCreatedUpcomingProjectJSON.toString());
                System.out.println("Newest upcoming project = " + newestCreatedUpcomingProjectJSON.toString());
            } else {
                json.put("message", eMessage);
                response.getWriter().write(json.toString());
            }
            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean createUpcomingProject() {
        boolean result = false;
        Connection connect = null;
        PreparedStatement psNewUpcomingProject = null;
        try {
            Date sqlNow = now();

            connect = dataSource.getConnection();

            String query_createSprint;

            query_createSprint = "insert into upcoming_sow (customer_name, sow_created_date, estimated_size, jira, dc, tem, notes, expected_start_month, expected_end_month, updated_date, apps_needed) values (?,?,?,?,?,?,?,?,?,?,?);";

            psNewUpcomingProject = connect.prepareStatement(query_createSprint);

            psNewUpcomingProject.setString(1, customer_name);
            psNewUpcomingProject.setDate(2, sqlNow);
            psNewUpcomingProject.setInt(3, estimated_size);
            psNewUpcomingProject.setString(4, jira);
            psNewUpcomingProject.setString(5, dc);
            psNewUpcomingProject.setString(6, tem);
            psNewUpcomingProject.setString(7, notes);
            psNewUpcomingProject.setDate(8, expected_start_month);
            psNewUpcomingProject.setDate(9, expected_end_month);
            psNewUpcomingProject.setDate(10, sqlNow);
            psNewUpcomingProject.setInt(11, apps_needed);

            System.out.println("creating upcoming project " + psNewUpcomingProject.toString());

            psNewUpcomingProject.executeUpdate();

            System.out.println("Create drive: " + query_createSprint);

            result = true;

            newestCreatedUpcomingProjectJSON = new JSONObject();
            newestCreatedUpcomingProjectJSON.put("customer_name", customer_name);
            newestCreatedUpcomingProjectJSON.put("sow_created_date", sqlNow.toString());
            newestCreatedUpcomingProjectJSON.put("estimated_size", estimated_size);
            newestCreatedUpcomingProjectJSON.put("jira", jira);
            newestCreatedUpcomingProjectJSON.put("dc", dc);
            newestCreatedUpcomingProjectJSON.put("tem", tem);
            newestCreatedUpcomingProjectJSON.put("notes", notes);
            newestCreatedUpcomingProjectJSON.put("expected_start_month", expected_start_month.toString());
            newestCreatedUpcomingProjectJSON.put("expected_end_month", expected_end_month.toString());
            newestCreatedUpcomingProjectJSON.put("updated_date", sqlNow.toString());
            //newestCreatedUpcomingProjectJSON.put("apps_needed", apps_needed);
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psNewUpcomingProject);
        }
        return result;
    }

    private String getCreatedUpcomingProject() {

        JSONObject json = new JSONObject();
        Connection connect = null;
        PreparedStatement psSearch = null;
        ResultSet rs = null;
        try {
            connect = dataSource.getConnection();

            java.util.Date currentDatetime = new java.util.Date();
            java.sql.Timestamp sqlTime = new Timestamp(currentDatetime.getTime());

            String query_getDriveById = "select * from upcoming_sow where customer_name ='" + customer_name + "';";

            psSearch = connect.prepareStatement(query_getDriveById);
            rs = psSearch.executeQuery();

            while (rs.next()) {
                json.put("customer_name", customer_name);
                json.put("sow_created_date", sqlTime);
                json.put("estimated_size", estimated_size);
                json.put("jira", jira);
                json.put("dc", dc);
                json.put("tem", tem);
                json.put("notes", notes);
                json.put("expected_start_month", expected_start_month.toString());
                json.put("expected_end_month", expected_end_month.toString());
                json.put("updated_date", sqlTime);
                json.put("apps_needed", apps_needed);
            }
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psSearch, rs);
        }

        return json.toString();
    }

    public Date stringToDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(format.parse(date).getTime());
    }

    public Date now() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlNow = new Date(now.getTime());
        return sqlNow;
    }
}