package app;

/**
 * Created by nlee on 8/15/16.
 */

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class deleteApplianceAssignment extends HttpServlet {
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
                          HttpServletResponse response) throws ServletException {
        try {
            appliance = request.getParameter("appliance");

            JSONObject json = new JSONObject();

            if (removeAppliance()) {
                json.put("result", "success");
            } else {
                json.put("result", eMessage);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json.toString());
            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public boolean removeAppliance() {
        boolean result = false;

        Connection connect = null;
        PreparedStatement psDeleteAppliance = null;

        try {
            connect = dataSource.getConnection();

            String query_deleteDrive = "delete from appliance_assignment where appliance = ?;";

            psDeleteAppliance = connect.prepareStatement(query_deleteDrive);
            psDeleteAppliance.setString(1, appliance);
            psDeleteAppliance.executeUpdate();

            System.out.println("Delete drive: " + query_deleteDrive);

            result = true;
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psDeleteAppliance);
        }

        return result;
    }
}
