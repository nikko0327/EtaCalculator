package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class updateUpcomingProject extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer_name;
    //private Date sow_created_date;
    private int estimated_size;
    private String jira;
    private String dc;
    private String tem;
    private String notes;
    private Date expected_start_month;
    private Date expected_end_month;
    // private Date updated_date;
    private int apps_needed;
    private boolean started;
    private boolean trying_to_start;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateUpcomingProject() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        System.out.println("--- updateUpcomingProject ---");

        try {
            customer_name = request.getParameter("customer_name");
            //sow_created_date = stringToDate(request.getParameter("sow_created_date")); // Null from POST
            estimated_size = (int) Double.parseDouble("0" + request.getParameter("estimated_size"));
            jira = request.getParameter("jira");
            dc = request.getParameter("dc");
            tem = request.getParameter("tem");
            notes = request.getParameter("notes");
            expected_start_month = stringToDate(request.getParameter("expected_start_month"));
            expected_end_month = stringToDate(request.getParameter("expected_end_month"));
            // updated_date = stringToDate(request.getParameter("updated_date"));
            apps_needed = (request.getParameter("apps_needed") == null) ? 0 : Integer.parseInt("0" + request.getParameter("apps_needed"));
            started = request.getParameter("started").equals("true");
            trying_to_start =
                    (request.getParameter("trying_to_start") != null && request.getParameter("trying_to_start").equals("true"));
            //System.out.println("Started is " + started);

            JSONObject json = new JSONObject();

            if (expected_start_month != null && expected_end_month != null && expected_start_month.after(expected_end_month)) {
                eMessage = "The expected start date is after the expected end date.";
                json.put("result", eMessage);
            } else if (expected_start_month != null && expected_end_month != null && daysBetween(expected_start_month, expected_end_month) <= 45) {
                eMessage = "The start date and end dates are too close to each other.\nLess than 45 days would require an unfeasible amount of appliances.";
                json.put("result", eMessage);
            } else if (updateUpcomingProject()) {
                json.put("result", "success");
            } else {
                json.put("result", eMessage);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public boolean updateUpcomingProject() {

        boolean result = false;
        Connection connect = null;
        PreparedStatement psUpdateUpcomingProject = null;
        try {

            connect = dataSource.getConnection();

            if (trying_to_start) {
                psUpdateUpcomingProject = connect.prepareStatement("update upcoming_sow set start = TRUE where customer_name = ?");
                psUpdateUpcomingProject.setString(1, customer_name);
                psUpdateUpcomingProject.executeUpdate();
                System.out.println("Started project for " + customer_name);
            } else {

                Date now = now();

                String query_updateDrive = "update upcoming_sow set customer_name = ?, estimated_size = ?, jira = ?, dc = ?, tem = ?, notes = ?, " +
                        "expected_start_month = ?, expected_end_month = ?, updated_date = ?, apps_needed = ?, start = ? " +
                        "where customer_name = '" + customer_name + "'";

                psUpdateUpcomingProject = connect.prepareStatement(query_updateDrive);
                psUpdateUpcomingProject.setString(1, customer_name);
                //psUpdateUpcomingProject.setDate(2, sow_created_date);
                psUpdateUpcomingProject.setInt(2, estimated_size);
                psUpdateUpcomingProject.setString(3, jira);
                psUpdateUpcomingProject.setString(4, dc);
                psUpdateUpcomingProject.setString(5, tem);
                psUpdateUpcomingProject.setString(6, notes);
                psUpdateUpcomingProject.setDate(7, expected_start_month);
                psUpdateUpcomingProject.setDate(8, expected_end_month);
                psUpdateUpcomingProject.setDate(9, now);
                psUpdateUpcomingProject.setInt(10, apps_needed);
                psUpdateUpcomingProject.setBoolean(11, started);
                psUpdateUpcomingProject.executeUpdate();

                System.out.println("Update Assignment: " + query_updateDrive);

//            String query_selectDriveById = "select * from upcoming_sow where customer_name = '" + customer_name + "'";
//            PreparedStatement prepSelectDriveStmt = connect.prepareStatement(query_selectDriveById);
//            ResultSet selectDriveRes = prepSelectDriveStmt.executeQuery();
            }

            result = true;

        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psUpdateUpcomingProject);
        }

        return result;
    }

    public Date stringToDate(String date) throws ParseException {
        if(date.trim().isEmpty()) {
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new Date(format.parse(date).getTime());
    }

    public Date now() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlNow = new Date(now.getTime());
        return sqlNow;
    }

    //Method to calculate day in between dates
    public long daysBetween(Date a, Date b) {
        return Math.abs((a.getTime() - b.getTime()) / (60 * 60 * 24 * 1000));
    }
}
