import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

public abstract class EmailNotifier {
    private static final String serverUrl = "http://lv-drivetrac.corp.proofpoint.com/DriveTracker/";
    private static final String SENDER = "ETACalculator@proofpoint.com";
    private static final String HOST = "smtp.us.proofpoint.com";

    public static boolean pingAllTheThings(HashMap<String, String> contents) {
        boolean sent = false;
        Session mailSession = Session.getInstance(new Properties());

        try {
            HashSet<InternetAddress> recipients = loadRecipients();
            if (recipients.isEmpty()) {
                System.out.println("[ALERT]: No email recipients detected for this action.");
                return false;
            }

            InternetAddress[] addresses = recipients.toArray(new InternetAddress[recipients.size()]);

            Properties properties = new Properties();
            properties.put("mail.smtp.host", HOST);
            properties.put("mail.smtp.port", "25");

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(SENDER));
            message.addRecipients(Message.RecipientType.BCC, addresses);


            message.setSubject("[ETA Calculator] (" + info.getAssetTag() + ") "
                    + info.getType() + " for " + info.getCustomerName());

            if (info.getType().equals("DELETED")) {
                message.setText("Drive removed from system with following details\n"
                        + "By " + info.getUpdatedBy() + "\n"
                        + "At " + info.getLastUpdated()
                );
            } else {
                message.setText("Drive " + info.getType().toLowerCase() + " in system with following details\n"
                        + "By " + info.getUpdatedBy() + "\n"
                        + "At " + info.getLastUpdated() + "\n\n"
                        + "PP Asset Tag: " + info.getAssetTag() + "\n"
                        + "Customer: " + info.getCustomerName() + "\n"
                        + "Drive Location: " + info.getDriveLocation() + "\n"
                        + "Drive State: " + info.getDriveState() + "\n"
                        + "Notes: " + info.getNotes() + "\n\n"
                        + "Go to " + serverUrl + "historyDrive.jsp?pp_asset_tag=" + info.getAssetTag() + " for more details."
                );
            }

            Transport.send(message);
            sent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sent;
    }

//    public boolean send() {
//        boolean result = false;
//
//        Session mailSession = Session.getInstance(props);
//        try {
//            MimeMessage message = new MimeMessage(mailSession);
//
//            message.setFrom(new InternetAddress(from));
//
//            // check if there is anyone to send to, if not dont do it
//            if (recipients.isEmpty()) {
//                this.eMessage = "Warning: No one will receive an email for this particular action.";
//
//                return false;
//            }
//
//            InternetAddress[] addresses = recipients.toArray(new InternetAddress[0]);
//            message.addRecipients(Message.RecipientType.BCC, addresses);
//
//            message.setSubject("[DriveTracking] (" + info.getAssetTag() + ") "
//                    + info.getType() + " for " + info.getCustomerName());
//
//            if (info.getType().equals("DELETED")) {
//                message.setText("Drive removed from system with following details\n"
//                        + "By " + info.getUpdatedBy() + "\n"
//                        + "At " + info.getLastUpdated()
//                );
//            } else {
//                message.setText("Drive " + info.getType().toLowerCase() + " in system with following details\n"
//                        + "By " + info.getUpdatedBy() + "\n"
//                        + "At " + info.getLastUpdated() + "\n\n"
//                        + "PP Asset Tag: " + info.getAssetTag() + "\n"
//                        + "Customer: " + info.getCustomerName() + "\n"
//                        + "Drive Location: " + info.getDriveLocation() + "\n"
//                        + "Drive State: " + info.getDriveState() + "\n"
//                        + "Notes: " + info.getNotes() + "\n\n"
//                        + "Go to " + serverUrl + "historyDrive.jsp?pp_asset_tag=" + info.getAssetTag() + " for more details."
//                );
//            }
//
//            Transport.send(message);
//            result = true;
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            eMessage = "Unable to send message.";
//        }
//
//        return result;
//    }

    private static HashSet<InternetAddress> loadRecipients() {
        HashSet<InternetAddress> recipients = new HashSet<>();

        Connection connect = null;
        PreparedStatement psSelectUser = null;
        ResultSet rs = null;

        try {
            connect = db_credentials.DB.getConnection();
            psSelectUser = connect.prepareStatement("SELECT username FROM user_info WHERE notification=true;");
            rs = psSelectUser.executeQuery();

            while (rs.next()) {
                recipients.add(new InternetAddress(rs.getString("username") + "@proofpoint.com"));
            }

        } catch (Exception e) {
            System.out.println("-- Error in loading email recipients --");
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psSelectUser, rs);
        }
        return recipients;
    }

//    @SuppressWarnings("Duplicates")
//    private void loadRecipients() {
//        Connection connect = null;
//        PreparedStatement psSelectUser = null;
//        ResultSet rs = null;
//
//        try {
//            connect = db_credentials.DB.getConnection();
//
//            String query_selectUsers = "SELECT username FROM user_info WHERE notification=true";
//
//            psSelectUser = connect.prepareStatement(query_selectUsers);
//            rs = psSelectUser.executeQuery();
//
//            while (rs.next()) {
//                recipients.add(new InternetAddress(rs.getString("username") + "@proofpoint.com"));
//            }
//
//        } catch (Exception e) {
//            eMessage = e.getMessage();
//            e.printStackTrace();
//        } finally {
//            db_credentials.DB.closeResources(connect, psSelectUser, rs);
//        }
//    }
}
