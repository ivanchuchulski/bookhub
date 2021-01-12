package client;

import api.interfaces.BookImpl;
import api.interfaces.ServerObjectInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private TabPane tpaneMenu;

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
    private TextField textFieldInput;

    @FXML
    private Button btnQuit;

    @FXML
    private Label lblDercypt1;

    @FXML
    private ListView<BookImpl> listViewSearchPanel;

    @FXML
    private Button btnAdd;

    @FXML
    private Label lblDercypt11;

    @FXML
    private Button btnSearch;

    @FXML
    private TextArea txaSearchPanel;

    @FXML
    private ScrollPane scrPaneMyBooks;

    @FXML
    private ImageView imageViewSearchPane;


    @FXML
    void btnLoginClicked(ActionEvent event) {

    }

    @FXML
    void btnQuitClicked(ActionEvent event) {

    }

    @FXML
    void btnRegisterClicked(ActionEvent event) {

    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        String title = textFieldInput.getText();

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
