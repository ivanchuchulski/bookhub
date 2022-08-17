package server;

import database.DatabaseConnector;
import database.User;
import implementations.BookImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ServerController {
    private DatabaseConnector databaseConnector;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabLogin;

    @FXML
    private TextField txtAdminUsername;

    @FXML
    private PasswordField txtAdminPassword;

    @FXML
    private Button btnAdminLogin;

    @FXML
    private Button btnExit;

    @FXML
    private Tab tabAdminPanel;

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
    private Button btnLogout;

    @FXML
    private Text txtAdminNameGreet;


    public void setDatabaseConnector(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @FXML
    void initialize() {
        wrapTextArea(txaUsers);
        wrapTextArea(txaBooks);

        ObservableList<Tab> tabs = tabPane.getTabs();
        tabs.remove(tabAdminPanel);

        btnAdminLogin.setDefaultButton(true);

        btnAdminLogin.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                btnAdminLogin.fire();
            }
        });
    }

    @FXML
    void btnAdminLoginClicked(ActionEvent event) {
        String usernameAdmin = txtAdminUsername.getText();
        String passwordAdmin = txtAdminPassword.getText();

        if (usernameAdmin.length() == 0 || passwordAdmin.length() == 0) {
            showAlertMessage(Alert.AlertType.WARNING, "Login", "Fields are necessary!");
            return;
        }

        if (databaseConnector.adminLogin(usernameAdmin, passwordAdmin)) {
            txtAdminUsername.setText("");
            txtAdminPassword.setText("");

            txtAdminNameGreet.setText(usernameAdmin + "!");

            ObservableList<Tab> tabs = tabPane.getTabs();
            tabs.remove(tabLogin);
            tabs.add(tabAdminPanel);

            showAlertMessage(Alert.AlertType.INFORMATION, "Admin Login", "Login successful!");
        } else {
            showAlertMessage(Alert.AlertType.WARNING, "Admin Login", "Incorrect credentials");
        }
    }

    @FXML
    void btnExitClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit confirmation");
        alert.setHeaderText("Quitting Bookhub server panel");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                databaseConnector.disconnect();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @FXML
    void btnDisplayBooksClicked(ActionEvent event) {
        txaBooks.clear();

        List<BookImpl> books = databaseConnector.fetchAllBooks();

        books.stream().map(BookImpl::toString).forEach(txaBooks::appendText);
    }

    @FXML
    void btnDisplayUsersClicked(ActionEvent event) {

        txaUsers.clear();

        List<User> users = databaseConnector.fetchAllUsers();

        users.stream().map(User::toString).forEach(txaUsers::appendText);
    }

    @FXML
    void btnLogoutClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout confirmation");
        alert.setHeaderText("Logout Bookhub server panel");
        alert.setContentText("Are you sure you want to logout from the panel?");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                txtAdminNameGreet.setText("");
                txaUsers.clear();
                txaBooks.clear();

                ObservableList<Tab> tabs = tabPane.getTabs();
                tabs.remove(tabAdminPanel);
                tabs.add(tabLogin);
            }
        });
    }

    @FXML
    void btnStopServerClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Stop server confirmation");
        alert.setHeaderText("Stopping Bookhub server");
        alert.setContentText("Are you sure you want to stop the server?");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // maybe disconnect all clients somehow

                // exit the javafx platform and java runtime
                databaseConnector.disconnect();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void wrapTextArea(TextArea textArea) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(textArea);

        textArea.setWrapText(true);
    }

    private void showAlertMessage(Alert.AlertType type, String header, String context) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
    }
}
