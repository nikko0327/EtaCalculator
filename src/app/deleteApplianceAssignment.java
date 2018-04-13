package app;

/**
 * Created by nlee on 8/15/16.
 */

import db_credentials.mysql_credentials;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class deleteApplianceAssignment extends HttpServlet implements mysql_credentials {
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String appliance;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteApplianceAssignment() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        appliance = request.getParameter("appliance");

        JSONObject json = new JSONObject();

        if (removeDriveAndHistory())
            json.put("result", "success");
        else
            json.put("result", eMessage);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
        response.flushBuffer();
    }

    public boolean removeDriveAndHistory() {
        boolean result = false;

        Connection connect = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connect = DriverManager.getConnection(db_url, user_name, password);
            connect = dataSource.getConnection();

            String query_deleteDrive = "delete from appliance_assignment where appliance = '" + appliance + "';";

            PreparedStatement prepDeleteDriveStmt = connect.prepareStatement(query_deleteDrive);
            int deleteDriveRes = prepDeleteDriveStmt.executeUpdate();

            System.out.println("Delete drive: " + query_deleteDrive);

            result = true;

            prepDeleteDriveStmt.close();


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
