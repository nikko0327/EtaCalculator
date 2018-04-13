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

public class updateApplianceAssignment extends HttpServlet implements mysql_credentials {
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String appliance;
    private String current;
    private String previous;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public updateApplianceAssignment() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        appliance = request.getParameter("appliance");
        current = request.getParameter("current");
        previous = request.getParameter("previous");
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

            String query_updateDrive = "update appliance_assignment set appliance = ?, current = ?, previous = ?" +
                    "where appliance = '" + appliance + "'";

            PreparedStatement prepUpdateDriveStmt = connect.prepareStatement(query_updateDrive);
            prepUpdateDriveStmt.setString(1, appliance);
            prepUpdateDriveStmt.setString(2, current);
            prepUpdateDriveStmt.setString(3, previous);

            int updateRes = prepUpdateDriveStmt.executeUpdate();

            System.out.println("Update Assignment: " + query_updateDrive);

            String query_selectDriveById = "select * from appliance_assignment where appliance = '" + appliance + "'";
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
