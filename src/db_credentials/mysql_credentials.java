package db_credentials;

public interface mysql_credentials {

    final String mysql_driver = "com.mysql.jdbc.Driver";

    final String user_name = "root";

    // PRODUCTION
    final String password = "@Rm@d1ll0!";
    final String db_url = "jdbc:mysql://localhost/EtaCalculator";

    // LOCAL
    // final String password = "";
    // final String db_url = "jdbc:mysql://localhost/EtaCalculatorTest";
}
