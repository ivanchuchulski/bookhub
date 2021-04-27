package server;

import database.DatabaseConnector;
import database.User;
import implementations.BookImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.util.List;

public class ServerController {

    private DatabaseConnector connector = new DatabaseConnector();

    @FXML
    private Label lblServerTitle;

    @FXML
    private TextArea txaUsers;

    @FXML
    private TextArea txaBooks;

    @FXML
    private Button btnDisplayBooks;

    @FXML
    private Button btnDisplayUsers;

    @FXML
    private Button btnStopServer;

    @FXML
    private Label lblUsers;

    @FXML
    private Label lblBooks;

    @FXML
    void btnDisplayBooksClicked(ActionEvent event) {

        txaBooks.clear();

        List<BookImpl> books = connector.fetchAllBooks();

        books.stream().map(BookImpl::toString).forEach(txaBooks::appendText);
    }

    @FXML
    void btnDisplayUsersClicked(ActionEvent event) {

        txaUsers.clear();

        List<User> users = connector.fetchAllUsers();

        users.stream().map(User::toString).forEach(txaUsers::appendText);
    }

    @FXML
    void btnStopServerClicked(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void initialize() {
        wrapTextArea(txaBooks);
        wrapTextArea(txaBooks);
    }

    private void wrapTextArea(TextArea textArea) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(textArea);

        textArea.setWrapText(true);
    }

}
