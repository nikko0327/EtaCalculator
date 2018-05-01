package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class updateApplianceAssignment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String appliance;
    private String current;
    private String previous;
    private String version;
    private String updated_by;
    //private JSONObject json = new JSONObject();

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
                          HttpServletResponse response) throws ServletException {

        System.out.println("--- updateApplianceAssignment ---");

        try {
            appliance = request.getParameter("appliance");
            current = request.getParameter("current");
            previous = request.getParameter("previous");
            version = request.getParameter("version");
            updated_by = request.getParameter("updated_by");

            JSONObject json = new JSONObject();

            if (updateApplianceAssignment()) {
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

    public boolean updateApplianceAssignment() {

        boolean result = false;
        Connection connect = null;
        PreparedStatement updateApplianceAssignment = null;

        try {
            connect = dataSource.getConnection();

            Date now = now();

            String query_updateDrive = "update appliance_assignment set appliance = ?, current = ?, previous = ?, " +
                    "version = ?, updated_by = ?, last_updated = ? where appliance = ?";

            updateApplianceAssignment = connect.prepareStatement(query_updateDrive);
            updateApplianceAssignment.setString(1, appliance);
            updateApplianceAssignment.setString(2, current);
            updateApplianceAssignment.setString(3, previous);
            updateApplianceAssignment.setString(4, version);
            updateApplianceAssignment.setString(5, updated_by);
            updateApplianceAssignment.setDate(6, now);
            updateApplianceAssignment.setString(7, appliance);
            updateApplianceAssignment.executeUpdate();

            System.out.println("Update assignment: " + query_updateDrive);

//            String query_selectDriveById = "select * from appliance_assignment where appliance = '" + appliance + "'";
//            PreparedStatement prepSelectDriveStmt = connect.prepareStatement(query_selectDriveById);
//            ResultSet selectDriveRes = prepSelectDriveStmt.executeQuery();

            sendEmail(now);
            result = true;
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, updateApplianceAssignment);
        }

        return result;
    }

    public static java.sql.Date now() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlNow = new java.sql.Date(now.getTime());
        return sqlNow;
    }

    private void sendEmail(Date date) {
        ApplianceEmail email = new ApplianceEmail();
        email.setApplianceIP(appliance);
        email.setApplianceStatus(ApplianceEmail.UPDATED);
        email.setCurrent(current);
        email.setPrevious(previous);
        email.setTimestamp(new java.sql.Timestamp(date.getTime()));
        email.setUpdater(updated_by);
        email.setVersion(version);
        EmailNotifier.pingAllTheThings(email);
    }
}
