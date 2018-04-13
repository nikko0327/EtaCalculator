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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class customerInfo
 */
public class customerInfo extends HttpServlet implements mysql_credentials {
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

        name.trim();

        String customers = "";
        String query_customerInfo = null;

        Connection connect = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connect = DriverManager.getConnection(db_url, user_name, password);

            connect = dataSource.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            JSONObject json = new JSONObject();

            query_customerInfo = "select * from customer_info " + "where name='" + name + "';";

            System.out.println(query_customerInfo);

            PreparedStatement prepStmt = connect.prepareStatement(query_customerInfo);

            ResultSet rs = prepStmt.executeQuery();
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

            rs.close();
            prepStmt.close();
            connect.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        finally {
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return customers;
    }
}

		
		
		
		
		
		
		
		
		
		
