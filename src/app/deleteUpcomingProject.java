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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                          HttpServletResponse response) throws ServletException, IOException {
        customer_name = request.getParameter("customer_name");

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
            connect = dataSource.getConnection();

            String query_deleteDrive = "delete from upcoming_sow where customer_name = '" + customer_name + "';";

            PreparedStatement prepDeleteDriveStmt = connect.prepareStatement(query_deleteDrive);
            int deleteDriveRes = prepDeleteDriveStmt.executeUpdate();

            System.out.println("Delete drive: " + query_deleteDrive);

            result = true;

            prepDeleteDriveStmt.close();


        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
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
