package api.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:mysql://localhost/bookhub";

    private static final String USER = "root";
    private static final String PASSWORD = null;

    public static void main(String[] args) {
        DatabaseConnector connector = new DatabaseConnector();
    }

    private User searchByUsername(String username) {

        User user = null;

        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            System.out.println("Connecting to DB");
            String sql = "SELECT * FROM users WHERE username = ?";


            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, username);

            ResultSet resultSet = prep.executeQuery();

            while (resultSet.next()) {

                String targetUsername = resultSet.getString("username");
                String targetPassword = resultSet.getString("password");

                //result = String.format("%s %s", targetUsername, targetPassword);
                user = new User(targetUsername, targetPassword);
            }

            prep.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private boolean addUserToDB(String username, String password) {

        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            System.out.println("Connecting to DB");
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, username);
            prep.setString(2, password);

            boolean success = prep.execute();

            prep.close();

            return success;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
