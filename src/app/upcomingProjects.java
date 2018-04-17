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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Servlet implementation class currentProjects
 */
public class upcomingProjects extends HttpServlet implements mysql_credentials {
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
//            Class.forName("com.mysql.jdbc.Driver");
//            connect = DriverManager.getConnection(db_url, user_name, password);
            connect = dataSource.getConnection();

            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

            String query_searchDrive = "";


            //get all drives except the ones returned to customer
//                query_searchDrive = "select * from drive_info";
            query_searchDrive = "select * from upcoming_sow";


            System.out.println("Search drive: " + query_searchDrive);

            PreparedStatement prepSearchDriveStmt = connect.prepareStatement(query_searchDrive);
            ResultSet rs = prepSearchDriveStmt.executeQuery();

            //Query to count appliances
            String query_appliance = ""; //Empty variable for appliance count query
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

                map.put("customer_name", rs.getString("customer_name"));
                map.put("sow_created_date", rs.getString("sow_created_date"));
                map.put("estimated_size", rs.getString("estimated_size"));
                map.put("jira", rs.getString("jira"));
                map.put("dc", rs.getString("dc"));
                map.put("tem", rs.getString("tem"));
                map.put("notes", rs.getString("notes"));
                map.put("expected_start_month", rs.getString("expected_start_month"));
                map.put("expected_end_month", rs.getString("expected_end_month"));
                map.put("updated_date", rs.getString("updated_date"));

                if (rs.getString("expected_start_month").contains("-") || rs.getString("expected_end_month").contains("-")) {

                    //Getting days between two dates
                    String dayStart = rs.getString("expected_start_month");
                    String dayEnd = rs.getString("expected_end_month");

                    Calendar cal1 = new GregorianCalendar();
                    Calendar cal2 = new GregorianCalendar();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = sdf.parse(dayStart);
                    cal1.setTime(date);
                    date = sdf.parse(dayEnd);
                    cal2.setTime(date);
                    int numOfDays = daysBetween(cal1.getTime(), cal2.getTime()); // This is the one we need to calculate the appliances needed
                    String numberOfDaysString = String.valueOf(numOfDays);
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
                } else {
                    map.put("apps_needed", "Dates are not set");

                }


                list.add(map);
            }

            int totalMatches = list.size();

            JSONObject json = new JSONObject();
            json.accumulate("totalMatches", totalMatches);
            json.accumulate("drives", list);


            json.accumulate("applianceCount", counter);
            json.accumulate("totalApplianceInUse", counterUsed);

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
        catch (ParseException e) {
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

    //Method to calculate day in between dates
    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
