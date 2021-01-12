package client;

import api.interfaces.BookImpl;
import api.interfaces.ServerObjectInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Controller {

    private ServerObjectInterface server;
    private Registry registry;

    private ObservableList<BookImpl> searchResults;

    @FXML
    private TabPane tabPaneMenu;

    @FXML
    private Tab tabLogin;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblUsername;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private Tab tabSearch;

    @FXML
    private Label lblOperationsUsername;

    @FXML
    private TextField textSearch;

    @FXML
    private Button btnQuit;

    @FXML
    private ComboBox<?> cmbCategory;

    @FXML
    private Label lblCategory;

    @FXML
    private Button btnAdd;

    @FXML
    private Label lblSearch;

    @FXML
    private ComboBox<?> cmbAction;

    @FXML
    private Button btnSearch;

    @FXML
    private TextArea txaSearchPanel;

    @FXML
    private ImageView imageViewSearchPane;

    @FXML
    private ScrollPane scrPaneMyBooks;

    @FXML
    private ListView<BookImpl> listViewSearchPanel;


    @FXML
    void btnAddBook(ActionEvent event) {

    }

    private void showAlertMessage(Alert.AlertType type, String header, String context) {

        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
    }


    @FXML
    void btnLoginClicked(ActionEvent event) {

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {

            boolean loggedIn = server.login(username, password);

            txtUsername.setText("");
            txtPassword.setText("");

            if (loggedIn) {
                showAlertMessage(Alert.AlertType.CONFIRMATION, "Login", "Login successful!");
            } else {
                showAlertMessage(Alert.AlertType.INFORMATION, "Login", "Incorrect credentials");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnQuitClicked(ActionEvent event) {

    }

    @FXML
    void btnRegisterClicked(ActionEvent event) {

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            boolean success = server.register(username, password);

            txtUsername.setText("");
            txtPassword.setText("");

            if (success) {
                showAlertMessage(Alert.AlertType.CONFIRMATION, "Registration", "Registration successful!");
            } else {
                showAlertMessage(Alert.AlertType.INFORMATION, "Registration", "Registration failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        String title = textSearch.getText();

        try {
            List<BookImpl> booksList = server.getBookByTitle(title);

            txaSearchPanel.setText(booksList.toString());

            searchResults = FXCollections.observableArrayList(booksList);
            listViewSearchPanel.setItems(searchResults);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void listViewSearchPanelOnClick(MouseEvent event) {
        BookImpl book = listViewSearchPanel.getSelectionModel().getSelectedItem();

        txaSearchPanel.clear();
        txaSearchPanel.setText(book.toString());

        try {
            Image javafxImage = new Image(book.getSmallThumbnailLink());
            imageViewSearchPane.setImage(javafxImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
//        scrPaneMyBooks.setContent(txaMyBooks);
//        scrPaneMyBooks.setFitToWidth(true);
//        scrPaneMyBooks.setPrefWidth(400);
//        scrPaneMyBooks.setPrefHeight(180);

        try {
            registry = LocateRegistry.getRegistry(7777);
            server = (ServerObjectInterface) registry.lookup("interface");

        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Connecting to login panel");
            alert.setContentText("Error connecting to server Please try again later");

            alert.showAndWait();

            Platform.exit();
            System.exit(0);
        }

        //tabSearch.setDisable(true); // disable Operations tab
    }
}
