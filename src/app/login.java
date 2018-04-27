package app;

import net.sf.json.JSONObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Servlet implementation class login
 */
public class login extends HttpServlet implements db_credentials.mysql_credentials {
    private static final boolean SPEED_TEST = false;
    private static final long serialVersionUID = 1L;
    private String eMessage;

    private Set<String> users;

    @Resource(name = "jdbc/EtaCalculatorDB")
    private DataSource dataSource;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
    }

    public void init() {
        users = new HashSet<String>();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject json = new JSONObject();

        loadAuthorizedUsers();

        if (processRequest(username, password)) {
            json.put("result", "success");
            json.put("username", username);
            json.put("mail", username + "@proofpoint.com");

            Cookie loginCookie = new Cookie("username", username);
            // setting cookie to expiry in 60 mins
            loginCookie.setMaxAge(60 * 60);
            response.addCookie(loginCookie);
        } else
            json.put("result", eMessage);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
        response.flushBuffer();
    }

    private boolean processRequest(String username, String password) {
        boolean result = false;
        String mail = "";

        Hashtable env = new Hashtable(11);

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ldap.corp.proofpoint.com");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + ",ou=People,dc=extreme-email,dc=com");
        env.put(Context.SECURITY_CREDENTIALS, password);

        if (!users.contains(username)) {
            eMessage = "Unauthorized User";
            return result;
        }

        try {
            // Create initial context
            DirContext ctx = new InitialDirContext(env);

            String[] attrIDs = {"mail"};
            // other attributes are: givenName,sn,userPassword, loginShell, etc
            // http://docs.oracle.com/javase/tutorial/jndi/ops/getattrs.html

            Attributes answer = ctx.getAttributes(
                    "uid=" + username + ",ou=People,dc=extreme-email,dc=com", attrIDs);

            for (NamingEnumeration ae = answer.getAll(); ae.hasMore(); ) {
                Attribute attr = (Attribute) ae.next();
                //System.out.println("attribute: " + attr.getID());
                /* Print each value */
                for (NamingEnumeration e = attr.getAll(); e.hasMore(); ) {
                    //System.out.println("value: " + e.next());
                    mail = e.next().toString();
                }
            }

            // Close the context when we're done
            result = true;
            ctx.close();

        } catch (NamingException e) {
            e.printStackTrace();
            result = false;
            eMessage = e.getMessage();
        }

        return result;
    }

    private void loadAuthorizedUsers() {
        Connection connect = null;
        try {

            connect = dataSource.getConnection();

            String query_selectUsers = "select * from user_info where login = true;";

            PreparedStatement prepSelectUsersStmt = connect.prepareStatement(query_selectUsers);
            ResultSet rs = prepSelectUsersStmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getString("username"));
            }

            rs.close();
            prepSelectUsersStmt.close();

        } catch (SQLException e) {
            eMessage = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_credentials.DB.closeResources(connect);
        }
    }
}
