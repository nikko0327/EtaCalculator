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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servlet implementation class currentProjects
 */
public class upcomingProjects extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String searchResult;
    private String eMessage;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public upcomingProjects() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        System.out.println("--- upcomingProjects ---");

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (searchUpcomingProjects()) {
                response.getWriter().write(searchResult);
            } else {
                response.getWriter().write(new JSONObject().put("message", eMessage).toString());
            }

            response.flushBuffer();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public boolean searchUpcomingProjects() {

        boolean result = false;

        Connection connect = null;
        PreparedStatement prepSearchDriveStmt = null;
        ResultSet rs = null;

        try {
            connect = dataSource.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            String query_searchDrive = "";

            //get all drives except the ones returned to customer
            // query_searchDrive = "select * from drive_info";
            query_searchDrive = "select * from upcoming_sow";

            System.out.println("Search drive: " + query_searchDrive);

            prepSearchDriveStmt = connect.prepareStatement(query_searchDrive);
            rs = prepSearchDriveStmt.executeQuery();

            //Query to count appliances
            int counter = db_credentials.DB.getApplianceCount(connect);
            System.out.println("Appliance Count: " + counter); //Test run

            // ./Query to count appliances

            // Counting appliances in use
            int counterUsed = db_credentials.DB.getAppliancesInUseCount(connect);
            System.out.println("Counter USED: " + counterUsed); //Test run

            // ./Counting appliances in used

            result = true;

            while (rs.next()) {
                list.add(getSearchDriveJSONResults(rs));
            }

            int totalMatches = list.size();

            JSONObject json = new JSONObject();
            json.accumulate("totalMatches", totalMatches);
            json.accumulate("drives", list);
            json.accumulate("applianceCount", counter);
            json.accumulate("totalApplianceInUse", counterUsed);

            searchResult = json.toString();
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, prepSearchDriveStmt, rs);
        }

        return result;
    }

    //Method to calculate day in between dates
    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public Map<String, String> getSearchDriveJSONResults(ResultSet rs) throws SQLException, ParseException {
        //System.out.println("--- Calling getSearchDriveJSONResults in upcomingProjects ---");
        Map<String, String> map = new HashMap<String, String>();

        map.put("customer_name", rs.getString("customer_name"));
        map.put("sow_created_date", rs.getDate("sow_created_date").toString());
        map.put("estimated_size", "" + rs.getInt("estimated_size"));
        map.put("jira", rs.getString("jira"));
        map.put("dc", rs.getString("dc"));
        map.put("tem", rs.getString("tem"));
        map.put("notes", rs.getString("notes"));

        String expected_start_month = rs.getDate("expected_start_month").toString();
        map.put("expected_start_month", rs.getDate("expected_start_month").toString());
        //System.out.println("Expected start month: " + expected_start_month);

        String expected_end_month = rs.getDate("expected_end_month").toString();
        map.put("expected_end_month", expected_end_month);
        //System.out.println("Expected end month: " + expected_end_month);

        map.put("updated_date", rs.getDate("updated_date").toString());

        if (expected_start_month.contains("-") || expected_end_month.contains("-")) {

            //Getting days between two dates
            String dayStart = expected_start_month;
            String dayEnd = expected_end_month;

            Calendar cal1 = new GregorianCalendar();
            Calendar cal2 = new GregorianCalendar();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date = sdf.parse(dayStart);
            cal1.setTime(date);
            date = sdf.parse(dayEnd);
            cal2.setTime(date);
            int numOfDays = daysBetween(cal1.getTime(), cal2.getTime()); // This is the one we need to calculate the appliances needed
            //String numberOfDaysString = String.valueOf(numOfDays);
            System.out.println("Days Between " + cal1.getTime() + " and " + cal2.getTime() + ": ");
            System.out.println("Days= " + numOfDays);
            // ./Getting days between two dates

            //Calculating to get Appliances needed START
            final int gigsPerDay = 150; //Number of gigabytes Proofpoint process
            System.out.println("Number of Gigs Per Day: " + gigsPerDay);
            int dateNoPadding = numOfDays - 14; // Taking away 2 weeks of padding
            System.out.println("Date No Padding: " + dateNoPadding);
            int dateNoMapping = dateNoPadding - 30; // Taking away 30 mapping days
            System.out.println("With No Mapping: " + dateNoMapping);
            double dateNoMappingDivTwo = dateNoMapping / 2; // Taking away 30 mapping days
            System.out.println("Divide by 2: " + dateNoMappingDivTwo);
            double estimatedSize = Double.parseDouble(rs.getString("estimated_size"));
            System.out.println("Estimated Size: " + estimatedSize);
            double numberOfGigsPerDayPre = estimatedSize / dateNoMappingDivTwo; // First, divide estimated size / date with no padding and mapping days divide by 2
            double numberOfGigsPerDay = numberOfGigsPerDayPre / gigsPerDay; // Then, divide the outcome to / 150 Gb/day
            System.out.println("Gigs Per Day Before Round Up: " + numberOfGigsPerDay);
            int totalNumberOfGigsNeeded = (int) Math.ceil(numberOfGigsPerDay);
            System.out.println("Total After Round Off: " + totalNumberOfGigsNeeded);
            //Calculating to get Appliances needed END
            String finalOutput = String.valueOf(totalNumberOfGigsNeeded); //Final output we need for getting apps needed
            map.put("apps_needed", finalOutput);
            //map.put("apps_needed", "" + 3);
        } else {
            map.put("apps_needed", "Dates are not set");
        }
        return map;
    }
}
