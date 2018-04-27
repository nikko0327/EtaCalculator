package app;

/**
 * Created by nlee on 8/15/16.
 * Updated by mihuang near 4/20/18.
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


public class deleteCurrentProject extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String eMessage;
    private String customer;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteCurrentProject() {
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
            customer = request.getParameter("customer");

            JSONObject json = new JSONObject();

            if (removeCurrentProject()) {
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

    public boolean removeCurrentProject() {
        boolean result = false;
        Connection connect = null;
        PreparedStatement psDeleteCurrentProject = null;
        try {
            connect = dataSource.getConnection();

            String query_deleteDrive = "delete from current_project where customer = ?;";

            psDeleteCurrentProject = connect.prepareStatement(query_deleteDrive);
            psDeleteCurrentProject.setString(1, customer);
            psDeleteCurrentProject.executeUpdate();

            System.out.println("Deleting current project: " + query_deleteDrive);

            result = true;
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psDeleteCurrentProject);
        }

        return result;
    }
}
