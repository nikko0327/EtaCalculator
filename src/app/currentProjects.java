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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servlet implementation class currentProjects
 */
public class currentProjects extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String searchResult;
    private String eMessage;
    int applianceCounter = 0;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public currentProjects() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        System.out.println("--- currentProjects ---");
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (getSearchResult()) {
                System.out.println(searchResult);
                response.getWriter().write(searchResult);
            } else {
                response.getWriter().write(new JSONObject().put("message", eMessage).toString());
            }

            response.flushBuffer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException(e);
        }
    }

    public boolean getSearchResult() {

        boolean result = false;

        Connection connect = null;
        PreparedStatement psSearchDrive = null;
        ResultSet rs = null;

        try {
            connect = dataSource.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            String query_searchDrive = "";

            //get all drives except the ones returned to customer
            query_searchDrive = "select * from current_project";

            System.out.println("Search drive: " + query_searchDrive);

            psSearchDrive = connect.prepareStatement(query_searchDrive);
            rs = psSearchDrive.executeQuery();

            //Query to count appliances
            int counter = db_credentials.DB.getApplianceCount(connect);
            System.out.println("Appliances total: " + counter);
            // ./Query to count appliances

            //Counting appliances in use
            int counterUsed = db_credentials.DB.getAppliancesInUseCount(connect);
            System.out.println("Counter USED: " + counterUsed); //Test run
            // ./Counting appliances in used

            result = true;

            while (rs.next()) {

                list.add(getSearchDriveJSONResults(rs));
            }

            //int totalMatches = list.size();

            JSONObject json = new JSONObject();
            json.accumulate("totalMatches", counter);
            json.accumulate("totalApplianceInUse", counterUsed);
            json.accumulate("drives", list);

            searchResult = json.toString();
            //System.out.println("Search result:\n" + searchResult);
        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psSearchDrive, rs);
        }

        return result;
    }

    //Method to calculate day in between dates
    public int daysBetween(Date d) {
        Date d1 = new Date();
        int daysBetween = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        return daysBetween;
    }


    public Map<String, String> getSearchDriveJSONResults(ResultSet rs) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            java.sql.Date createdDate = rs.getDate("created_date");
            //System.out.println("--- GETSEARCHDRIVEJSONRESULTS IN CURRENTPROJECTS: " + createdDate.toString());

            int dataSize = rs.getInt("data_size");
            int applianceCount = rs.getInt("appliance_count");

            map.put("customer", rs.getString("customer"));
            map.put("jira", rs.getString("jira"));
            map.put("dc", rs.getString("dc"));
            map.put("data_size", "" + dataSize);
            map.put("import_engr", rs.getString("import_engr"));
            map.put("tem", rs.getString("tem"));
            map.put("current_stage", rs.getString("current_stage"));
            map.put("created_date", createdDate.toString());
            System.out.println("---***--- created date: " + createdDate.toString());
            map.put("notes", rs.getString("notes"));
            map.put("appliance_count", "" + applianceCount);
            map.put("current_appliance_count", "" + applianceCount);
            map.put("is_completed", (rs.getBoolean("is_completed")) ? "Yes" : "No");

            //Added for Closing Date
            if ((createdDate == null) || (createdDate.toString().isEmpty())) { // Check if created_date is set to null or blank.
                map.put("closing_date", "Date not set");
            } else {
                double calculateClosing = dataSize / (applianceCount * 150); // Calculate closing date by number of days from creation.
                int calculatedClosingInt = (int) Math.ceil(calculateClosing); // Round up
                int totalPreProcessingNum = calculatedClosingInt + 14; // Total number of days that takes to comple plus 2 weeks cushion
                int totalProcessingNum = calculatedClosingInt; // Total number of days that takes to comple plus 2 weeks cushion
                int mapping = 30;
                int overallProcess = totalPreProcessingNum + mapping + totalProcessingNum; // Total amout of days required for the process to finish
                System.out.println("Data Size: " + dataSize); // Test run
                System.out.println("Number of Apps: " + applianceCount); // Test run
                System.out.println("Pre-processing Days: " + totalPreProcessingNum); // Test run
                System.out.println("Mapping: " + "30"); // Test run
                System.out.println("Processing: " + totalProcessingNum); // Test run
                System.out.println("TOTAL DAYS: " + overallProcess); // Test run

//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Formats the date to "-" in between year month and date
//                String dateInString = createdDate.toString(); // Gets the value of created_date and set to string
//                String[] splitter = dateInString.split("-"); //Splits into yyyymmdd
//
//                String year = splitter[0];
//                int yearInt = Integer.parseInt(year); //Set to integer to be use in Calendar Object
//                String month = splitter[1];
//                int monthInt = Integer.parseInt(month); //Set to integer to be use in Calendar Object
//                String day = splitter[2];
//                int dayInt = Integer.parseInt(day); //Set to integer to be use in Calendar Object
//
//                Calendar calendar = new GregorianCalendar(yearInt, monthInt - 1, dayInt + overallProcess); // Create Calendar object to do calculation
//                String calendarFormatter = formatter.format(calendar.getTime()); // format calendar to string to show values to output
//                System.out.println("Calendar: " + calendarFormatter); //Test run

                java.sql.Date endDate = stringToDate(createdDate.toString());
                System.out.println(endDate.toString());
                long daysInMS = (long) overallProcess * 86400000;
                System.out.println(endDate.getTime() + daysInMS);
                endDate.setTime(endDate.getTime() + daysInMS);
                System.out.println(endDate.toString());

                map.put("closing_date", endDate.toString()); // Set closing_date as calendarFormatter

                int daysBetween = daysBetween(endDate);
                map.put("daysBetweenTodayAndFinish", "" + daysBetween); //Getting days between today and finish date.
                System.out.println("Between: " + daysBetween);
                //./Added for Closing Date
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // map.put("closing_date", date);
        //System.out.println(map.toString());
        return map;
    }

    public java.sql.Date stringToDate(String date) throws ParseException {
        if (date.trim().isEmpty()) {
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new java.sql.Date(format.parse(date).getTime());
    }
}