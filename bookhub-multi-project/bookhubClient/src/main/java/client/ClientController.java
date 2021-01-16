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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientController {

    private ServerObjectInterface server;
    private Registry registry;
    private List<Book> searchBooksResultsList;

    private Map<Book, BookPreference> userBookMap;

    private List<Book> userBooksResultsList;

    private String username;

    private boolean searchFilter = false;

    private List<Book> temporaryFilterBooks = new ArrayList<>();


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
    private Button btnSearchMyBooks;

    @FXML
    private ComboBox<BookPreference> cmbPreferencesMyBooks;

    @FXML
    private ImageView imgViewMyBooks;

    @FXML
    private Button btnSetPreferenceMyBooks;

    @FXML
    private ListView<String> listViewMyBooks;

    @FXML
    private TextField txtSearchTitleMyBooks;

    @FXML
    private Button btnFetchBooks;

    @FXML
    private TextArea txaMyBooks;

    @FXML
    private Button btnClearFilter;


    @FXML
    void btnClearFilterClicked(ActionEvent event) {
        clearMyBooksGUI();
        searchFilter = false;
        temporaryFilterBooks.clear();
        txtSearchTitleMyBooks.clear();


        ObservableList<String> myBooksObservableList = FXCollections.observableList(userBooksResultsList
                .stream().map(e -> {
                    try {
                        return String.format("%s, %s", e.getTitle(), e.getPublishedDate());
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList()));

        listViewMyBooks.setItems(myBooksObservableList);
    }


    @FXML
    void btnSearchMyBooksClicked(ActionEvent event) {
        String title = txtSearchTitleMyBooks.getText().toLowerCase();

        searchFilter = true;

        List<Book> books = new ArrayList<>();
        temporaryFilterBooks.clear();


        userBooksResultsList.stream()
                .filter(e -> {
                    try {
                        return e.getTitle().toLowerCase().contains(title);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    return false;
                }).forEach(e -> {
            books.add(e);
            temporaryFilterBooks.add(e);
        });

        temporaryFilterBooks.forEach(e -> {
            try {
                System.out.println(e.printInfo());
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        });


        System.out.println(temporaryFilterBooks.size());

        var observableList = getObservableList(books);
        listViewMyBooks.setItems(observableList);
    }


    private ObservableList<String> getObservableList(List<Book> books) {

        ObservableList<String> myBooksObservableList = FXCollections.observableList(books
                .stream().map(e -> {
                    try {
                        return String.format("%s, %s", e.getTitle(), e.getPublishedDate());
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList()));

        return myBooksObservableList;
    }

    @FXML
    void listViewMyBooksClicked(MouseEvent event) {
        try {
            if (listViewMyBooks.getSelectionModel().getSelectedItem() != null) {

                int index = listViewMyBooks.getSelectionModel().getSelectedIndex();
                String content = listViewMyBooks.getSelectionModel().getSelectedItem();


                if (index == -1) {
                    txaMyBooks.clear();
                    txaMyBooks.setText("error");
                    return;
                }

                Book selectedBook = null;

                if (searchFilter) {
                    for (Book book : temporaryFilterBooks) {
                        if (String.format("%s, %s", book.getTitle(), book.getPublishedDate()).equals(content)) {
                            selectedBook = book;
                            break;
                        }
                    }

                } else {
                    selectedBook = (Book) userBookMap.keySet().toArray()[index];
                }

                txaMyBooks.clear();
                txaMyBooks.setText(selectedBook.getDescription());

                Image javafxImage = new Image(selectedBook.getSmallThumbnailLink());
                imgViewMyBooks.setImage(javafxImage);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void clearMyBooksGUI() {
        cmbPreferencesMyBooks.getSelectionModel().select(-1);
        txaMyBooks.clear();
        imgViewMyBooks.setImage(null);
        listViewMyBooks.getSelectionModel().select(-1);

    }

    private void clearTemporaryBooksList() {
        searchFilter = false;
        temporaryFilterBooks.clear();

    }

    @FXML
    void btnFetchBooksClicked(ActionEvent event) {

        try {

            clearTemporaryBooksList();

            userBookMap = server.getBooksForUser(username);

            userBooksResultsList = new ArrayList(userBookMap.keySet());

            ObservableList<String> myBooksObservableList = FXCollections.observableList(userBooksResultsList
                    .stream().map(e -> {
                        try {
                            return String.format("%s, %s", e.getTitle(), e.getPublishedDate());
                        } catch (RemoteException remoteException) {
                            remoteException.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList()));


            clearMyBooksGUI();

            listViewMyBooks.setItems(myBooksObservableList);
        } catch (RemoteException e) {
            e.printStackTrace();


        }


    }


    @FXML
    void btnSetPreferenceMyBooksClicked(ActionEvent e) {

    }


    private void wrapTextArea(TextArea textArea) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(textArea);

        textArea.setWrapText(true);
    }


    private void fillComboBox(ComboBox<BookPreference> comboBox) {
        comboBox.getItems().addAll(BookPreference.values());
        comboBox.getSelectionModel().selectFirst();
    }

    private void disableTabs() {
        tabSearch.setDisable(true);
        tabMyBooks.setDisable(true);
    }

    private void updateUserBooksByPreferenceGUI() {
        int selectedIndex = cmbPreferencesMyBooks.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            return;
        }

        txaMyBooks.clear();
        imgViewMyBooks.setImage(null);


        BookPreference preference = BookPreference.values()[selectedIndex];

        if (userBooksResultsList == null) {
            return;
        }

        List<Book> booksForCategory;

        if (searchFilter) {
            booksForCategory = temporaryFilterBooks.stream()
                    .filter(e -> userBookMap.get(e).equals(preference)).collect(Collectors.toList());

        } else {
            booksForCategory = userBooksResultsList.stream()
                    .filter(e -> userBookMap.get(e).equals(preference)).collect(Collectors.toList());
        }

        ObservableList<String> observableList = getObservableList(booksForCategory);
        listViewMyBooks.setItems(observableList);
    }

    @FXML
    void initialize() {
        cmbCategory.getItems().addAll(SearchCategory.values());
        cmbCategory.getSelectionModel().selectFirst();

        fillComboBox(cmbBookPreference);
//        fillComboBox(cmbPreferencesMyBooks);

        cmbPreferencesMyBooks.getItems().addAll(BookPreference.values());
        cmbPreferencesMyBooks.getSelectionModel().select(-1);

        disableTabs();

        wrapTextArea(txaSearchPanel);
        wrapTextArea(txaMyBooks);


        cmbPreferencesMyBooks.setOnAction((event) -> {
            updateUserBooksByPreferenceGUI();
        });

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

            int index = listViewSearchPanel.getSelectionModel().getSelectedIndex();

            if (index == -1) {
                txaSearchPanel.clear();
                txaSearchPanel.setText("error");
                return;
            }

            var book = searchBooksResultsList.get(index);

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
