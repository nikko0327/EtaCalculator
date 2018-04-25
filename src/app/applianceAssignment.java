package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class currentProjects
 */
public class applianceAssignment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String searchResult;
    private String eMessage;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public applianceAssignment() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        System.out.println("--- applianceAssignment ---");
        try {
            String appliance = request.getParameter("appliance");
            String current = request.getParameter("current");
            String previous = request.getParameter("previous");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (getSearchResult(appliance, current, previous)) {
                response.getWriter().write(searchResult);
            } else {
                response.getWriter().write(new JSONObject().put("message", eMessage).toString());
            }

            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public boolean getSearchResult(String appliance, String current, String previous) {

//        appliance = appliance.trim();
//        current = current.trim();
//        previous = previous.trim();

        boolean result = false;

        Connection connect = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connect = dataSource.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            String query_searchDrive = "";

            // For Search
//
//            if ((appliance.equalsIgnoreCase(null) || appliance.equalsIgnoreCase(""))
//                    && (current.equalsIgnoreCase(null) || current.equalsIgnoreCase(""))
//                    && (previous.equalsIgnoreCase(null) || previous.equalsIgnoreCase(""))) {
//
//                // get all drives except the ones returned to customer
//                query_searchDrive = "select * from appliance_assignment " +
//                        "where appliance <> 'appliance' " +
//                        "and current <> 'current' " +
//                        "and previous <> 'previous' " +
//                        "order by current desc;";
//            } else {
//                query_searchDrive = "select * from appliance_assignment where";
//
//                if (!(appliance.equalsIgnoreCase(null) || appliance.equalsIgnoreCase("")))
//                    query_searchDrive += " appliance like '%" + appliance + "%'";
//
//                if (!(current.equalsIgnoreCase(null) || current.equalsIgnoreCase(""))) {
//                    if (query_searchDrive.equalsIgnoreCase("select * from appliance_assignment where"))
//                        query_searchDrive += " current like '%" + current + "%'";
//                    else
//                        query_searchDrive += " and current like '%" + current + "%'";
//                }
//
//                if (!(previous.equalsIgnoreCase(null) || previous.equalsIgnoreCase(""))) {
//                    if (query_searchDrive.equalsIgnoreCase("select * from appliance_assignment where"))
//                        query_searchDrive += " previous like '%" + previous + "%'";
//                    else
//                        query_searchDrive += " and previous like '%" + previous + "%'";
//                }
//
//                query_searchDrive += " order by current desc;";
//            }

            System.out.println("Search drive: " + query_searchDrive);

            ps = connect.prepareStatement(query_searchDrive);
            rs = ps.executeQuery();

            result = true;

            while (rs.next()) {
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

        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, ps, rs);
        }

        return result;
    }
}
