package app;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;

public abstract class EmailNotifier {
    private static final String serverUrl = "http://lv-drivetrac.corp.proofpoint.com/EtaCalculator/";
    private static final String SENDER = "ETACalculator@proofpoint.com";
    private static final String HOST = "smtp.us.proofpoint.com";

    public static boolean pingAllTheThings(ApplianceEmail email) {
        boolean sent = false;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", "25");
        Session mailSession = Session.getInstance(properties);

        try {
            HashSet<InternetAddress> recipients = loadRecipients();
            if (recipients.isEmpty()) {
                System.out.println("[ALERT]: No email recipients detected for this action.");
                return false;
            }

            InternetAddress[] addresses = recipients.toArray(new InternetAddress[recipients.size()]);

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(SENDER));
            message.addRecipients(Message.RecipientType.BCC, addresses);

            message.setSubject("[ETA Calculator] (" + email.getApplianceIP() + ") " + email.getApplianceStatusString());

            String text = "";

            switch (email.getApplianceStatus()) {
                case ApplianceEmail.CREATED:
                    text = "An appliance was created in the system with the following details:\n\nCreated by: ";
                    break;
                case ApplianceEmail.UPDATED:
                    text = "An appliance was updated in the system with the following details:\n\nUpdated by: ";
                    break;
                case ApplianceEmail.DELETED:
                    text = "An appliance was deleted in the system with the following details:\n\nDeleted by: ";
                    break;
                default:
                    text = "An appliance was modified in some way or form with the following details:\n\nModified by: ";
                    break;
            }

            text += email.getUpdater() + "\n";
            text += "Appliance affected: " + email.getApplianceIP() + "\n";
            text += "Version: " + email.getVersion() + "\n";
            text += "Time of occurrence: " + email.getTimestamp() + "\n";
            text += "Currently in use for: " + email.getCurrent() + "\n";
            text += "Previously in use for: " + email.getPrevious() + "\n";

            message.setText(text);

            Transport.send(message);
            sent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sent;
    }

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
            System.out.println("-- Error when loading email recipients --");
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect, psSelectUser, rs);
        }
        return recipients;
    }

}
