package app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db_credentials.mysql_credentials;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class currentProjects
 */
public class applianceAssignment extends HttpServlet implements mysql_credentials {
    private static final long serialVersionUID = 1L;
    private String searchResult;
    private String eMessage;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public applianceAssignment() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String appliance = request.getParameter("appliance");
        String current = request.getParameter("current");
        String previous = request.getParameter("previous");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if(getSearchResult(appliance, current, previous))
            response.getWriter().write(searchResult);
        else
            response.getWriter().write(new JSONObject().put("message", eMessage).toString());

        response.flushBuffer();
    }

    public boolean getSearchResult(String appliance, String current, String previous){

        appliance.trim();
        current.trim();
        previous.trim();

        boolean result = false;

        Connection connect = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(db_url, user_name, password);

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            String query_searchDrive = "";


//            For Search

            if ((appliance.equalsIgnoreCase(null) || appliance.equalsIgnoreCase(""))
                    && (current.equalsIgnoreCase(null) || current.equalsIgnoreCase(""))
                    && (previous.equalsIgnoreCase(null) || previous.equalsIgnoreCase(""))) {
                //get all drives except the ones returned to customer
                query_searchDrive = "select * from appliance_assignment " +
                        "where appliance <> 'appliance' " +
                        "and current <> 'current' " +
                        "and previous <> 'previous' " +
                        "order by current desc;";
            }
            else {
                query_searchDrive = "select * from appliance_assignment where";

                if (!(appliance.equalsIgnoreCase(null) || appliance.equalsIgnoreCase("")))
                    query_searchDrive += " appliance like '%" + appliance + "%'";

                if (!(current.equalsIgnoreCase(null) || current.equalsIgnoreCase(""))) {
                    if(query_searchDrive.equalsIgnoreCase("select * from appliance_assignment where"))
                        query_searchDrive += " current like '%" + current + "%'";
                    else
                        query_searchDrive += " and current like '%" + current + "%'";
                }

                if (!(previous.equalsIgnoreCase(null) || previous.equalsIgnoreCase(""))) {
                    if(query_searchDrive.equalsIgnoreCase("select * from appliance_assignment where"))
                        query_searchDrive += " previous like '%" + previous + "%'";
                    else
                        query_searchDrive += " and previous like '%" + previous + "%'";
                }

                query_searchDrive += " order by current desc;";
            }

            //get all drives except the ones returned to customer
//                query_searchDrive = "select * from drive_info";
//            query_searchDrive = "select * from appliance_assignment";


            System.out.println("Search drive: " + query_searchDrive);

            PreparedStatement prepSearchDriveStmt = connect.prepareStatement(query_searchDrive);
            ResultSet rs = prepSearchDriveStmt.executeQuery();

            result = true;

            while(rs.next()){
                Map<String, String> map = new HashMap<String, String>();

                map.put("appliance", rs.getString("appliance"));
                map.put("current", rs.getString("current"));
                map.put("previous", rs.getString("previous"));
                list.add(map);
            }

            int totalMatches = list.size();

            JSONObject json = new JSONObject();
            json.accumulate("totalMatches", totalMatches);
            json.accumulate("drives", list);

            searchResult = json.toString();

            rs.close();
            prepSearchDriveStmt.close();

        } catch(SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if(connect != null)
                    connect.close();
            } catch(SQLException se) {
                eMessage = se.getMessage();
                se.printStackTrace();
            }
        }

        return result;
    }
}
