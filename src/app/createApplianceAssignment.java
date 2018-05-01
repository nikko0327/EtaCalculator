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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/uploadServlet12")

public class createApplianceAssignment extends HttpServlet {
    //    private static final long serialVersionUID = 1L;
    private String eMessage;

    private String appliance;
    private String current;
    private String previous;
    private String version;
    private String updated_by;
    private JSONObject json = new JSONObject();


    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    InputStream inputStream;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public createApplianceAssignment() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException {

        System.out.println("--- createApplianceAssignment ---");

        try {
            appliance = request.getParameter("appliance");
            current = request.getParameter("current");
            previous = request.getParameter("previous");
            version = request.getParameter("version");
            updated_by = request.getParameter("updated_by");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (createApplianceAssignment()) {

                response.getWriter().write(json.toString());
                //System.out.println("latest drive = " + latestDrive);
            } else {
                //JSONObject json = new JSONObject();
                json.put("message", eMessage);
                response.getWriter().write(json.toString());
            }

            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean createApplianceAssignment() {

        boolean result = false;
        Connection connect = null;
        PreparedStatement psNewAppliance = null;
        try {

            Date now = now();
            connect = dataSource.getConnection();

            String query_createSprint;

            query_createSprint = "insert into appliance_assignment (appliance, current, previous, updated_by, last_updated, version) values (?,?,?,?,?,?);";

            psNewAppliance = connect.prepareStatement(query_createSprint);

            psNewAppliance.setString(1, appliance);
            psNewAppliance.setString(2, current);
            psNewAppliance.setString(3, previous);
            psNewAppliance.setString(4, updated_by);
            psNewAppliance.setDate(5, now);
            psNewAppliance.setString(6, version);

            psNewAppliance.executeUpdate();

            System.out.println("Create drive: " + query_createSprint);

            json.put("appliance", appliance);
            json.put("current", current);
            json.put("previous", previous);
            json.put("updated_by", updated_by);
            json.put("last_updated", now.toString());
            json.put("version", version);

            sendEmail(now);
            result = true;
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psNewAppliance);
        }

        return result;
    }

    private String getCreatedDrive() {

        json = new JSONObject();
        Connection connect = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;

        try {
            connect = dataSource.getConnection();

            String query_getDriveById = "select * from appliance_assignment where appliance = ?;";

            prepStmt = connect.prepareStatement(query_getDriveById);
            prepStmt.setString(1, appliance);
            rs = prepStmt.executeQuery();

            while (rs.next()) {
                json.put("appliance", appliance);
                json.put("current", current);
                json.put("previous", previous);
            }
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, prepStmt, rs);
        }

        return json.toString();
    }

    public static Date now() {
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlNow = new Date(now.getTime());
        return sqlNow;
    }

    private void sendEmail(Date date) {
        ApplianceEmail email = new ApplianceEmail();
        email.setApplianceIP(appliance);
        email.setApplianceStatus(ApplianceEmail.CREATED);
        email.setCurrent(current);
        email.setPrevious(previous);
        email.setTimestamp(new java.sql.Timestamp(date.getTime()));
        email.setUpdater(updated_by);
        email.setVersion(version);
        EmailNotifier.pingAllTheThings(email);
    }
}