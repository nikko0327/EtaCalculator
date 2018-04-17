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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servlet implementation class currentProjects
 */
public class currentProjects extends HttpServlet implements mysql_credentials {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (getSearchResult())
            response.getWriter().write(searchResult);
        else
            response.getWriter().write(new JSONObject().put("message", eMessage).toString());

        response.flushBuffer();
    }

    public boolean getSearchResult() {

        boolean result = false;

        Connection connect = null;
        try {
            connect = db_credentials.DB.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            String query_searchDrive = "";
            String query_appliance = ""; //Empty variable for appliance count query

            //get all drives except the ones returned to customer
            query_searchDrive = "select * from current_project";
            System.out.println("Search drive: " + query_searchDrive);
            PreparedStatement prepSearchDriveStmt = connect.prepareStatement(query_searchDrive);
            ResultSet rs = prepSearchDriveStmt.executeQuery();

            //Query to count appliances
            query_appliance = "select * from appliance_assignment"; //Setting query for appliance count
            System.out.println("Search drive: " + query_searchDrive); //Test run
            PreparedStatement prepSearchDriveStmtAppliance = connect.prepareStatement(query_appliance); //Statement to execute
            ResultSet rsAppliance = prepSearchDriveStmtAppliance.executeQuery(); //Execute statement
            int counter = 0;
            while (rsAppliance.next()) { //Go through every row and add 1 to counter
                counter++;
            }
            System.out.println("Appliance Count: " + counter); //Test run
            // ./Query to count appliances

            //Counting appliances in use
            String query_count = ""; //Empty variable for appliance count query
            query_count = "select * from current_project"; //Setting query for appliance count
            System.out.println("Search drive: " + query_count); //Test run
            PreparedStatement prepSearchDriveStmtCount = connect.prepareStatement(query_count); //Statement to execute
            ResultSet rsCount = prepSearchDriveStmtCount.executeQuery(); //Execute statement
            int counterUsed = 0;
            while (rsCount.next()) { //Go through every row and add 1 to counter
                int applianceCount = Integer.parseInt(rsCount.getString("appliance_count"));
                counterUsed = counterUsed + applianceCount;
            }
            System.out.println("Counter USED: " + counterUsed); //Test run
            // ./Counting appliances in used

            result = true;

            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("customer", rs.getString("customer"));
                map.put("jira", rs.getString("jira"));
                map.put("dc", rs.getString("dc"));
                map.put("data_size", rs.getString("data_size"));
                map.put("import_engr", rs.getString("import_engr"));
                map.put("tem", rs.getString("tem"));
                map.put("current_stage", rs.getString("current_stage"));
                map.put("created_date", rs.getString("created_date"));
                map.put("notes", rs.getString("notes"));
                map.put("appliance_count", rs.getString("appliance_count"));
                map.put("is_completed", rs.getString("is_completed"));

                //Added for Closing Date
                if ((rs.getString("created_date") == null) || (rs.getString("created_date") == "")) { //Check if created_date is set to null or blank.
                    map.put("closing_date", "Date Not Set");
                } else {

                    String dataSize = rs.getString("data_size"); //Set variable for calculation
                    double dataSizeNum = Double.parseDouble(dataSize); //Convert to double for calculation

                    String applianceCount = rs.getString("appliance_count"); //Set variable for calculation
                    double applianceCountNum = Double.parseDouble(applianceCount); //Convert to double for calculation

                    double calculateClosing = dataSizeNum / (applianceCountNum * 150); //Calculate closing date by number of days from creation.
                    int calculatedClosingInt = (int) Math.ceil(calculateClosing); // Round up
                    int totalPreProcessingNum = calculatedClosingInt + 14; // Total number of days that takes to comple plus 2 weeks cushion
                    int totalProcessingNum = calculatedClosingInt; // Total number of days that takes to comple plus 2 weeks cushion
                    int mapping = 30;
                    int overallProcess = totalPreProcessingNum + mapping + totalProcessingNum; // Total amout of days required for the process to finish

                    System.out.println("Data Size: " + dataSizeNum); // Test run
                    System.out.println("Number of Apps: " + applianceCountNum); // Test run
                    System.out.println("Pre-processing Days: " + totalPreProcessingNum); // Test run
                    System.out.println("Mapping: " + "30"); // Test run
                    System.out.println("Processing: " + totalProcessingNum); // Test run
                    System.out.println("TOTAL DAYS: " + overallProcess); // Test run

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //Formats the date to "-" in between year month and date
                    String dateInString = String.valueOf(rs.getString("created_date")); // Gets the value of created_date and set to string
                    String[] splitter = dateInString.split("-"); //Splits into yyyymmdd

                    String year = splitter[0];
                    int yearInt = Integer.parseInt(year); //Set to integer to be use in Calendar Object
                    String month = splitter[1];
                    int monthInt = Integer.parseInt(month); //Set to integer to be use in Calendar Object
                    String day = splitter[2];
                    int dayInt = Integer.parseInt(day); //Set to integer to be use in Calendar Object

                    System.out.println(dateInString); //Print for test
                    System.out.println(Arrays.toString(splitter));//Print for test

                    Calendar calendar = new GregorianCalendar(yearInt, monthInt - 1, dayInt + overallProcess); // Create Calendar object to do calculation
                    String calendarFormatter = formatter.format(calendar.getTime()); // format calendar to string to show values to output
                    System.out.println("Calendar: " + calendarFormatter); //Test run

                    map.put("closing_date", calendarFormatter); // Set closing_date as calendarFormatter

                    int betweenTodayAndFinish = daysBetween(calendar.getTime());
                    String betweenDaysFromTodayAndFinish = Integer.toString(betweenTodayAndFinish);
                    map.put("daysBetweenTodayAndFinish", betweenDaysFromTodayAndFinish); //Getting days between today and finish date.
                    System.out.println("Between: " + daysBetween(calendar.getTime()));
                    // ./Added for Closing Date
                }
//                map.put("closing_date", date);

                list.add(map);
            }


            int totalMatches = list.size();

            JSONObject json = new JSONObject();
            json.accumulate("totalMatches", counter);
            json.accumulate("totalApplianceInUse", counterUsed);
            json.accumulate("drives", list);

            searchResult = json.toString();

            rs.close();
            prepSearchDriveStmt.close();

        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            eMessage = e.getMessage();
//            e.printStackTrace();
//        }
        finally {
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

    //Method to calculate day in between dates
    public int daysBetween(Date d) {
        Date d1 = new Date();
        return (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}