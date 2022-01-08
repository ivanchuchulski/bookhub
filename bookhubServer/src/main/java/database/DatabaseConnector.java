package database;

import api.interfaces.Book;
import api.enums.BookStatus;
import implementations.BookImpl;
import server.BookhubServerConfig;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnector {
    private static final String DB_URL = BookhubServerConfig.DB_URL;
    private static final String USER = BookhubServerConfig.DB_USER;
    private static final String PASSWORD = BookhubServerConfig.DB_PASSWORD;

    private Connection connection;

    public DatabaseConnector() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public boolean isUserRegistered(String username, String password) {
        String checkUserRegisteredQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement prep = connection.prepareStatement(checkUserRegisteredQuery)) {
            prep.setString(1, username);
            prep.setString(2, password);

            try (ResultSet result = prep.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUserInDB(String username, String password) {
        String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement prep = connection.prepareStatement(insertUserQuery)) {
            prep.setString(1, username);
            prep.setString(2, password);

            prep.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> fetchAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement()) {

            try (ResultSet resultSet = stmt.executeQuery(sql)) {
                while (resultSet.next()) {
                    String targetUsername = resultSet.getString("username");
                    String targetPassword = resultSet.getString("password");

                    users.add(new User(targetUsername, targetPassword));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<BookImpl> fetchAllBooks() {
        List<BookImpl> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Statement stmt = connection.createStatement()) {

            try (ResultSet resultSet = stmt.executeQuery(sql)) {
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
            }

        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Map<Book, BookStatus> getBooksForUser(String username) {
        Map<Book, BookStatus> userBooks = new HashMap<>();

        String sql = "SELECT book.title, book.id, book.publisher, book.publishedDate, book.description, " +
                "book.smallThumbnailLink, preferences.preferenceType FROM " +
                "book JOIN preferences ON book.id = preferences.bookId WHERE username = ?";
        try (PreparedStatement prep = connection.prepareStatement(sql)) {

            prep.setString(1, username);

            try (ResultSet resultSet = prep.executeQuery()) {

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
            }
            return userBooks;
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public boolean addUserPreferenceBookToDB(String username, Book book, BookStatus selectedCategory) {
        addBookToDB(book);

        String sql = "INSERT INTO preferences (username, bookId, preferenceType) VALUES (?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE preferenceType = ?";

        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, username);
            prep.setString(2, book.getId());

            printPreference(username, book, selectedCategory);

            prep.setString(3, selectedCategory.getText());
            prep.setString(4, selectedCategory.getText());

            prep.execute();

            return true;
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeBookPreference(String username, String bookId) {
        String sql = "DELETE FROM preferences WHERE username = ? AND bookId = ?";

        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, username);
            prep.setString(2, bookId);

            prep.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean adminLogin(String username, String password) {
        String sql = "SELECT password FROM admin WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String dbPass = resultSet.getString("password");
                    return password.equals(dbPass);
                }

                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addBookToDB(Book book) {
        // when trying to insert the same book the primary key is the same, so ON DUPLICATE ... is not needed
        String sql = "INSERT INTO book (title, id, publisher, publishedDate, description, smallThumbnailLink) " +
                "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE title = ?";
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, book.getTitle());
            prep.setString(2, book.getId());
            prep.setString(3, book.getPublisher());
            prep.setString(4, book.getPublishedDate());
            prep.setString(5, book.getDescription());
            prep.setString(6, book.getSmallThumbnailLink());

            prep.setString(7, book.getTitle());

            prep.execute();
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printPreference(String username, Book book, BookStatus selectedCategory) throws RemoteException {
        System.out.println(username);
        System.out.println(book.getId());
        System.out.println(selectedCategory.getText());
    }


}
