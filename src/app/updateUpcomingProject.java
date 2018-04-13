package app;

import db_credentials.mysql_credentials;
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


public class updateUpcomingProject extends HttpServlet implements mysql_credentials {
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String customer_name;
    private String sow_created_date;
    private String estimated_size;
    private String jira;
    private String dc;
    private String tem;
    private String notes;
    private String expected_start_month;
    private String expected_end_month;
    private String updated_date;
    private String apps_needed;

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

        customer_name = request.getParameter("customer_name");
        sow_created_date = request.getParameter("sow_created_date");
        estimated_size = request.getParameter("estimated_size");
        jira = request.getParameter("jira");
        dc = request.getParameter("dc");
        tem = request.getParameter("tem");
        notes = request.getParameter("notes");
        expected_start_month = request.getParameter("expected_start_month");
        expected_end_month = request.getParameter("expected_end_month");
        updated_date = request.getParameter("updated_date");
        apps_needed = request.getParameter("apps_need");

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
//            Class.forName("com.mysql.jdbc.Driver");
//            connect = DriverManager.getConnection(db_url, user_name, password);

            connect = dataSource.getConnection();

            Date currentDatetime = new Date();
            Timestamp sqlTime = new Timestamp(currentDatetime.getTime());

            String query_updateDrive = "update upcoming_sow set customer_name = ?, estimated_size = ?, jira = ?, dc = ?, tem = ?, notes = ?, expected_start_month = ?, expected_end_month = ?, updated_date = ?, apps_needed = ? " +
                    "where customer_name = '" + customer_name + "'";

            PreparedStatement prepUpdateDriveStmt = connect.prepareStatement(query_updateDrive);
            prepUpdateDriveStmt.setString(1, customer_name);
//            prepUpdateDriveStmt.setString(2, sow_created_date);
            prepUpdateDriveStmt.setString(2, estimated_size);
            prepUpdateDriveStmt.setString(3, jira);
            prepUpdateDriveStmt.setString(4, dc);
            prepUpdateDriveStmt.setString(5, tem);
            prepUpdateDriveStmt.setString(6, notes);
            prepUpdateDriveStmt.setString(7, expected_start_month);
            prepUpdateDriveStmt.setString(8, expected_end_month);
            prepUpdateDriveStmt.setTimestamp(9, sqlTime);
            prepUpdateDriveStmt.setString(10, apps_needed);

            int updateRes = prepUpdateDriveStmt.executeUpdate();

            System.out.println("Update Assignment: " + query_updateDrive);

            String query_selectDriveById = "select * from upcoming_sow where customer_name = '" + customer_name + "'";
            PreparedStatement prepSelectDriveStmt = connect.prepareStatement(query_selectDriveById);
            ResultSet selectDriveRes = prepSelectDriveStmt.executeQuery();

            result = true;

            prepSelectDriveStmt.close();
            prepUpdateDriveStmt.close();


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


}
