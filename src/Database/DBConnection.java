package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            String url = "jdbc:sqlserver://cinemadatabase.database.windows.net:1433;"
                    + "database=movie;"
                    + "encrypt=true;"
                    + "trustServerCertificate=false;"
                    + "hostNameInCertificate=*.database.windows.net;"
                    + "loginTimeout=30;";

            String user = "cinema";
            String password = "movie12@";

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");

        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }

        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
