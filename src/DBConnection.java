import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String url = "jdbc:sqlserver://DESKTOP-L9G0DQ6;databaseName=project";  // Adjust the URL as needed
    private static String username = "sa";  // Your SQL Server username
    private static String password = "123456789";  // Your SQL Server password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }
}
