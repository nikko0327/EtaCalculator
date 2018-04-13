package db_credentials;

import javax.annotation.Resource;
import javax.sql.DataSource;

public interface mysql_credentials {

    final String mysql_driver = "com.mysql.jdbc.Driver";

    final String user_name = "root";
    //final String password = "@Rm@d1ll0!";
    final String password = "";
    final String db_url = "jdbc:mysql://localhost/EtaCalculator";

}
