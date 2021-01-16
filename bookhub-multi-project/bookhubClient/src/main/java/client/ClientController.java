package client;

import api.interfaces.Book;
import api.interfaces.BookPreference;
import api.interfaces.SearchCategory;
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
import java.util.stream.Collectors;

public class ClientController {

    private ServerObjectInterface server;
    private Registry registry;
    private List<Book> searchBooksResultsList;

    private ObservableList<Book> searchBooksResult;

    private String username;

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
    private ComboBox<SearchCategory> cmbCategory;

    @FXML
    private Label lblCategory;

    @FXML
    private Button btnAddBook;

    @FXML
    private Label lblSearch;

    @FXML
    private ComboBox<BookPreference> cmbBookPreference;

    @FXML
    private Button btnSearch;

    @FXML
    private TextArea txaSearchPanel;

    @FXML
    private ImageView imageViewSearchPane;

    @FXML
    private ScrollPane scrPaneMyBooks;

    @FXML
    private ListView<String> listViewSearchPanel;

    @FXML
    private Tab tabMyBooks;

    @FXML
    void initialize() {
        cmbCategory.getItems().addAll(SearchCategory.values());
        cmbCategory.getSelectionModel().selectFirst();

        tabSearch.setDisable(true);
        tabMyBooks.setDisable(true);

        cmbBookPreference.getItems().addAll(BookPreference.values());
        cmbBookPreference.getSelectionModel().selectFirst();


        // add scroll pane to text Search Panel and wrap text in it
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(txaSearchPanel);

        txaSearchPanel.setWrapText(true);

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
    }

    @FXML
    void btnLoginClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            boolean loggedIn = server.login(username, password);

            this.username = txtUsername.getText();

            txtUsername.setText("");
            txtPassword.setText("");

            if (loggedIn) {
                tabSearch.setDisable(false);
                tabMyBooks.setDisable(false);

                tabSearch.getContent().requestFocus();

                tabLogin.setDisable(true);
                tabPaneMenu.getTabs().remove(0);


                showAlertMessage(Alert.AlertType.INFORMATION, "Login", "Login successful!");
            } else {
                showAlertMessage(Alert.AlertType.WARNING, "Login", "Incorrect credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    void btnQuitClicked(ActionEvent event) {

    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        try {
            String inputText = textSearch.getText();
            SearchCategory selectedCategory = cmbCategory.getSelectionModel().getSelectedItem();

            listViewSearchPanel.getItems().clear();

            searchBooksResultsList = server.getBookByType(selectedCategory, inputText);

            // clear the list
            listViewSearchPanel.getItems().clear();
            txaSearchPanel.clear();
            imageViewSearchPane.setImage(null);

            if (searchBooksResultsList != null) {
                txaSearchPanel.setText("select a book to view it's description");

                List<String> bookInfos = searchBooksResultsList.stream()
                        .map(book -> {
                            try {
                                return book.printInfo();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .collect(Collectors.toList());

                listViewSearchPanel.getItems().clear();
                listViewSearchPanel.getItems().addAll(bookInfos);

            } else {
                listViewSearchPanel.setItems(FXCollections.observableArrayList(List.of("nothing found")));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    void listViewSearchPanelOnClick(MouseEvent event) throws RemoteException {
        if (listViewSearchPanel.getSelectionModel().getSelectedItem() != null) {
            // get the title by looking at the separator in book printInfo method, and split by that separator
            String separator = " : ";
            String selectedBookTitle = listViewSearchPanel.getSelectionModel().getSelectedItem().split(separator)[0];

            System.out.println("selected : " + selectedBookTitle);

            Book book = null;

            for (Book searchResult : searchBooksResultsList) {
                System.out.println(searchResult.getTitle());

                if (searchResult.getTitle().equals(selectedBookTitle)) {
                    book = searchResult;
                }
            }

            if (book == null) {
                txaSearchPanel.clear();
                txaSearchPanel.setText("error");
                return;
            }

            txaSearchPanel.clear();
            txaSearchPanel.setText(book.getDescription());

            try {
                Image javafxImage = new Image(book.getSmallThumbnailLink());
                imageViewSearchPane.setImage(javafxImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnAddBookClicked(ActionEvent event) {

        int index = listViewSearchPanel.getSelectionModel().getSelectedIndex();

        Book selectedBook = searchBooksResultsList.get(index);

        BookPreference bookPreference = cmbBookPreference.getSelectionModel().getSelectedItem();

        try {
            boolean success = server.addUserBookPreference(username, selectedBook, bookPreference);

            if (success) {
                showAlertMessage(Alert.AlertType.CONFIRMATION, "Add Book to preferences", "Successfully " +
                                                                                          "added book to preferences");
            } else {
                showAlertMessage(Alert.AlertType.ERROR, "Add Book to preferences", "Failed to add " +
                                                                                   "add book to preferences");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void showAlertMessage(Alert.AlertType type, String header, String context) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
    }
}
