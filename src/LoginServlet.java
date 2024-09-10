import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LoginServlet: doPost method called");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");
        
        System.out.println("Received login attempt - Login Name: " + loginName);

        String userId = checkLogin(loginName, password);

        JsonObject jsonResponse = new JsonObject();
        if (userId != null) {
            System.out.println("Login successful for user ID: " + userId);
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("redirect", "admin_interface(for_copy).html");
            jsonResponse.addProperty("userId", userId);
        } else {
            System.out.println("Login failed for login name: " + loginName);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Invalid login name or password.");
        }

        String responseString = jsonResponse.toString();
        System.out.println("Sending response: " + responseString);
        out.print(responseString);
        out.flush();
    }

    private String checkLogin(String loginName, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            System.out.println("Database connection established");
            
            String query = "SELECT ID FROM account WHERE [Login name] = ? AND Password = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, loginName);
            ps.setString(2, password);
            
            System.out.println("Executing query: " + query);
            System.out.println("With parameters - Login Name: " + loginName + ", Password: [HIDDEN]");
            
            rs = ps.executeQuery();
    
            if (rs.next()) {
                String userId = rs.getString("ID");
                System.out.println("User found with ID: " + userId);
                return userId;
            } else {
                System.out.println("No user found with provided credentials");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
                System.out.println("Database resources closed");
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}