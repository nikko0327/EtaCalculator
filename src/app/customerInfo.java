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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class customerInfo
 */
public class customerInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public customerInfo() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String result = customer_info(name);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
        response.flushBuffer();
    }

    public String customer_info(String name) {

        name = name.trim();

        String customers = "";
        String query_customerInfo = null;

        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try {

            connect = dataSource.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            JSONObject json = new JSONObject();

            query_customerInfo = "select * from customer_info " + "where name='" + name + "';";
            System.out.println(query_customerInfo);

            ps = connect.prepareStatement(query_customerInfo);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("name", rs.getString("name"));
                map.put("guid", rs.getString("guid"));

                list.add(map);
            }

            int totalCustomers = list.size();

            json.accumulate("totalCustomers", totalCustomers);
            json.accumulate("customers", list);

            customers = json.toString();

            System.out.println(customers);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, ps, rs);
        }

        return customers;
    }
}

		
		
		
		
		
		
		
		
		
		
