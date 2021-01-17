package database;

import api.interfaces.Book;
import api.enums.BookStatus;
import implementations.BookImpl;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost/bookhub";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        DatabaseConnector connector = new DatabaseConnector();

        connector.getBooksForUser("test");
    }

    public boolean loginUserInDB(String username, String password) {
        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            String checkUserRegisteredQuery = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement prep = connection.prepareStatement(checkUserRegisteredQuery);
            prep.setString(1, username);
            prep.setString(2, password);

            ResultSet result = prep.executeQuery();

            if (result.next()) {
                result.close();
                prep.close();
                return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUserInDB(String username, String password) {
        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String checkUserExistQuery = "SELECT * FROM users WHERE username = ?";

            PreparedStatement prep = connection.prepareStatement(checkUserExistQuery);
            prep.setString(1, username);

            ResultSet result = prep.executeQuery();

            if (result.next()) {
                result.close();
                prep.close();

                return false;
            }

            String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

            prep = connection.prepareStatement(insertUserQuery);

            prep.setString(1, username);
            prep.setString(2, password);

            prep.execute();
            prep.close();
            result.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> fetchAllUsers() {

        List<User> users = new ArrayList<>();

        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            System.out.println("Connecting to DB");
            String sql = "SELECT * FROM users";

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {

                String targetUsername = resultSet.getString("username");
                String targetPassword = resultSet.getString("password");

                User user = new User(targetUsername, targetPassword);
                users.add(user);
            }

            stmt.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public List<BookImpl> fetchAllBooks() {
        List<BookImpl> books = new ArrayList<>();

        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            System.out.println("Connecting to DB");
            String sql = "SELECT * FROM book";

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {

                String bookTitle = resultSet.getString("title");
                String bookId = resultSet.getString("id");
                String bookPublisher = resultSet.getString("publisher");
                String bookPublishedDate = resultSet.getString("publishedDate");
                String bookDescription = resultSet.getString("description");
                String bookSmallThumbnailLink = resultSet.getString("smallThumbnailLink");

                var book = new BookImpl(bookTitle, bookId, bookPublisher, bookPublishedDate,
                        bookDescription, bookSmallThumbnailLink);

                books.add(book);
            }

            stmt.close();
            resultSet.close();

        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
        return books;
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

    private void addBookToDB(Book book) {
        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connecting to DB");

            // when trying to insert the same book the primary key is the same, so ON DUBLICATE ... is not needed
            String sql = "INSERT INTO book (title, id, publisher, publishedDate, description, smallThumbnailLink) " +
                         "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE title = ?";


            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, book.getTitle());
            prep.setString(2, book.getId());
            prep.setString(3, book.getPublisher());
            prep.setString(4, book.getPublishedDate());
            prep.setString(5, book.getDescription());
            prep.setString(6, book.getSmallThumbnailLink());

            prep.setString(7, book.getTitle());

            prep.execute();

            prep.close();
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public Map<Book, BookStatus> getBooksForUser(String username) {
        Map<Book, BookStatus> userBooks = new HashMap<>();

        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connecting to DB");

            String sql = "SELECT book.title, book.id, book.publisher, book.publishedDate, book.description, " +
                         "book.smallThumbnailLink, preferences.preferenceType FROM " +
                         "book JOIN preferences ON book.id = preferences.bookId WHERE username = ?";


            PreparedStatement prep = connection.prepareStatement(sql);

            prep.setString(1, username);

            ResultSet resultSet = prep.executeQuery();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String bookId = resultSet.getString("id");
                String bookPublisher = resultSet.getString("publisher");
                String bookPublishedDate = resultSet.getString("publishedDate");
                String bookDescription = resultSet.getString("description");
                String bookSmallThumbnailLink = resultSet.getString("smallThumbnailLink");

                String bookPreferenceString = String.join("_",
                        resultSet.getString("preferenceType").toUpperCase()
                                .split("\\s+"));

                var book = new BookImpl(bookTitle, bookId, bookPublisher, bookPublishedDate,
                        bookDescription, bookSmallThumbnailLink);

                userBooks.put(book, BookStatus.valueOf(bookPreferenceString));
            }

            prep.close();
            resultSet.close();

            return userBooks;
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addUserPreferenceBookToDB(String username, Book book, BookStatus selectedCategory) {
        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connecting to DB");

            addBookToDB(book);

            String sql = "INSERT INTO preferences (username, bookId, preferenceType) VALUES (?, ?, ?)" +
                         "ON DUPLICATE KEY UPDATE preferenceType = ?";

            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, username);
            prep.setString(2, book.getId());

            System.out.println(username);
            System.out.println(book.getId());
            System.out.println(selectedCategory.getText());

            prep.setString(3, selectedCategory.getText());
            prep.setString(4, selectedCategory.getText());

            prep.execute();

            prep.close();

            return true;
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeBook(String username, String bookId) {
        try (var connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connecting to DB");

            String sql = "DELETE FROM preferences WHERE username = ? AND bookId = ?";

            PreparedStatement prep = connection.prepareStatement(sql);
            prep.setString(1, username);
            prep.setString(2, bookId);

            prep.execute();
            prep.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
