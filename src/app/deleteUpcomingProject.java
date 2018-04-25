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

public class deleteUpcomingProject extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String eMessage;
    private String customer_name;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteUpcomingProject() {
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
            customer_name = request.getParameter("customer_name");

            JSONObject json = new JSONObject();

            if (removeUpcomingProject()) {
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

    public boolean removeUpcomingProject() {
        boolean result = false;

        Connection connect = null;
        PreparedStatement psDeleteUpcomingProject = null;
        try {
            connect = dataSource.getConnection();

            String query_deleteDrive = "delete from upcoming_sow where customer_name = ?;";

            psDeleteUpcomingProject = connect.prepareStatement(query_deleteDrive);
            psDeleteUpcomingProject.setString(1, customer_name);
            psDeleteUpcomingProject.executeUpdate();

            System.out.println("Delete drive: " + query_deleteDrive);

            result = true;

        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psDeleteUpcomingProject);
        }

        return result;
    }
}
