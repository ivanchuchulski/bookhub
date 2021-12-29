package client;

import api.interfaces.Book;
import api.enums.BookStatus;
import api.enums.SearchCategory;
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
import javafx.scene.text.Text;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClientController {
    private static final int SERVER_REGISTRY_PORT = BookhubClientConfig.REGISTRY_PORT;
    private static final String SERVER_INTERFACE_REGISTRY_NAME = BookhubClientConfig.SERVER_INTERFACE_REGISTRY_NAME;

    private Registry registry;
    private ServerObjectInterface server;

    private String username;
    private Map<Book, BookStatus> userBookMap = new HashMap<>();
    private List<Book> userBooksResultsList = new ArrayList<>();

    private List<Book> searchBooksResultsList = new ArrayList<>();

    private final List<Book> temporaryFilterBooks = new ArrayList<>();

    private boolean searchFilter = false;

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
    private Button btnLoginQuit;

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
    private ComboBox<BookStatus> cmbBookStatus;

    @FXML
    private Button btnSearch;

    @FXML
    private TextArea txaSearchPanel;

    @FXML
    private ImageView imgViewSearchPane;

    @FXML
    private ScrollPane scrPaneMyBooks;

    @FXML
    private ListView<String> listViewSearchPanel;

    @FXML
    private Tab tabMyBooks;

    @FXML
    private Button btnSearchMyBooks;

    @FXML
    private ComboBox<BookStatus> cmbStatusMyBooks;

    @FXML
    private ImageView imgViewMyBooks;

    @FXML
    private Button btnSetStatusMyBooks;

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
    private Button btnRemoveBook;

    @FXML
    private Button btnQuitMyBooks;

    @FXML
    private ComboBox<BookStatus> cmbNewStatus;

    @FXML
    private Text txtGreetUsername;

    @FXML
    private Text txtMyBooksGreetUsername;

    @FXML
    void initialize() {
        try {
            registry = LocateRegistry.getRegistry(SERVER_REGISTRY_PORT);
            server = (ServerObjectInterface) registry.lookup(SERVER_INTERFACE_REGISTRY_NAME);

            initialSetupGUI();
            fillSearchCategory();
            setupElementsListeners();
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
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            if (username.length() == 0 || password.length() == 0) {
                showAlertMessage(Alert.AlertType.WARNING, "Login", "Fields are necessary!");
                return;
            }

            if (server.login(username, password)) {
                this.username = txtUsername.getText();

                txtUsername.setText("");
                txtPassword.setText("");

                ObservableList<Tab> tabs = tabPaneMenu.getTabs();
                tabs.remove(tabLogin);
                tabs.add(0, tabSearch);
                tabs.add(1, tabMyBooks);

                String usernameGreet = username + "!";
                txtGreetUsername.setText(usernameGreet);
                txtMyBooksGreetUsername.setText(usernameGreet);

                showAlertMessage(Alert.AlertType.INFORMATION, "Login", "Login successful!");
            } else {
                showAlertMessage(Alert.AlertType.WARNING, "Login", "Incorrect credentials");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlertMessage(Alert.AlertType.INFORMATION, "Login", "Login failed, please try again");
        }
    }

    @FXML
    void btnRegisterClicked(ActionEvent event) {
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            if (username.length() == 0 || password.length() == 0) {
                showAlertMessage(Alert.AlertType.WARNING, "Register", "Fields for registration are necessary!");
                return;
            }

            if (server.register(username, password)) {
                txtUsername.setText("");
                txtPassword.setText("");
                showAlertMessage(Alert.AlertType.CONFIRMATION, "Registration", "Registration successful!");
            } else {
                showAlertMessage(Alert.AlertType.INFORMATION, "Registration", "Registration failed!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            showAlertMessage(Alert.AlertType.INFORMATION, "Registration", "Registration failed, please try again");
        }
    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        try {
            String inputText = textSearch.getText();
            SearchCategory selectedCategory = cmbCategory.getSelectionModel().getSelectedItem();

            searchBooksResultsList = server.getBookByType(selectedCategory, inputText);

            // clear the list
            clearSearchResults();

            if (searchBooksResultsList == null) {
                listViewSearchPanel.setItems(FXCollections.observableArrayList(List.of("nothing found")));
                return;
            }

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

            listViewSearchPanel.setItems(FXCollections.observableList(bookInfos));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    void listViewSearchPanelOnClick(MouseEvent event) {
        if (listViewSearchPanel.getSelectionModel().getSelectedItem() != null) {
            int index = listViewSearchPanel.getSelectionModel().getSelectedIndex();

            if (index == -1) {
                txaSearchPanel.clear();
                txaSearchPanel.setText("error");
                return;
            }

            Book book = searchBooksResultsList.get(index);

            try {
                txaSearchPanel.clear();
                txaSearchPanel.setText(book.getDescription());

                Image javafxImage = new Image(book.getSmallThumbnailLink());
                imgViewSearchPane.setImage(javafxImage);
            } catch (Exception e) {
                e.printStackTrace();
                txaSearchPanel.setText("unable to display book description");
            }
        }
    }

    @FXML
    void btnAddBookClicked(ActionEvent event) {
        int index = listViewSearchPanel.getSelectionModel().getSelectedIndex();

        if (searchBooksResultsList.isEmpty()) {
            showAlertMessage(Alert.AlertType.INFORMATION, "Book preference", "No book selected to add");
            return;
        }

        Book selectedBook = searchBooksResultsList.get(index);

        BookStatus bookStatus = cmbBookStatus.getSelectionModel().getSelectedItem();

        try {
            boolean success = server.addUserBookPreference(username, selectedBook, bookStatus);

            if (success) {
                showAlertMessage(Alert.AlertType.INFORMATION, "Add Book to preferences", "Successfully " +
                        "added book to preferences");
            } else {
                showAlertMessage(Alert.AlertType.ERROR, "Add Book to preferences", "Failed to add " +
                        "add book to preferences");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnQuitClicked(ActionEvent event) {
        logoutDialog();
    }

    @FXML
    void btnLoginQuitClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit confirmation");
        alert.setHeaderText("Quitting Bookhub");
        alert.setContentText("Are you sure you want to exit?");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

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

        int preferenceIndex = cmbStatusMyBooks.getSelectionModel().getSelectedIndex();

        temporaryFilterBooks.clear();

        Predicate<Book> filter;

        if (preferenceIndex == -1) {

            filter = e -> {
                try {
                    return e.getTitle().toLowerCase().contains(title);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                return false;
            };
        } else {
            BookStatus preference = cmbStatusMyBooks.getSelectionModel().getSelectedItem();

            filter = e -> {
                try {
                    return e.getTitle().toLowerCase().contains(title) && userBookMap.get(e)
                                                                                    .equals(preference);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                return false;
            };
        }

        List<Book> books = filterUserBooks(filter);
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


    @FXML
    void btnFetchBooksClicked(ActionEvent event) {
        try {

            clearTemporaryBooksList();

            userBookMap = server.getBooksForUser(username);

            userBooksResultsList = new ArrayList<>(userBookMap.keySet());

            ObservableList<String> myBooksObservableList =
                    FXCollections.observableList(userBooksResultsList.stream()
                                                                     .map(e -> {
                                                                         try {
                                                                             return String
                                                                                     .format("%s, %s", e.getTitle(), e
                                                                                             .getPublishedDate());
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
    void btnSetStatusMyBooksClicked(ActionEvent event) throws RemoteException {
        if (searchFilter) {
            if (listViewMyBooks.getSelectionModel().getSelectedItem() != null) {
                int selectedBookIndex = listViewMyBooks.getSelectionModel().getSelectedIndex();
                String content = listViewMyBooks.getSelectionModel().getSelectedItem();

                // no book selected
                if (selectedBookIndex == -1) {
                    return;
                }

                int selectedStatusIndex = cmbNewStatus.getSelectionModel().getSelectedIndex();

                // no status selected
                if (selectedStatusIndex == -1) {
                    return;
                }

                BookStatus bookStatus = cmbNewStatus.getSelectionModel().getSelectedItem();

                Book selectedBook = null;

                if (searchFilter) {
                    for (Book book : temporaryFilterBooks) {
                        if (String.format("%s, %s", book.getTitle(), book.getPublishedDate()).equals(content)) {
                            selectedBook = book;
                            break;
                        }
                    }
                } else {
                    selectedBook = (Book) userBookMap.keySet().toArray()[selectedBookIndex];
                }
                server.addUserBookPreference(username, selectedBook, bookStatus);

                showAlertMessage(Alert.AlertType.INFORMATION, "Change book status", "Book status changed" +
                        " successfully");

                btnFetchBooksClicked(event);
            }
        }
    }

    @FXML
    void btnRemoveBookClicked(ActionEvent event) {
        try {
            if (listViewMyBooks.getSelectionModel().getSelectedItem() != null) {
                int index = listViewMyBooks.getSelectionModel().getSelectedIndex();
                String content = listViewMyBooks.getSelectionModel().getSelectedItem();

                if (index == -1) {
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
                server.removeBook(username, selectedBook.getId());

                btnFetchBooksClicked(event);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnQuitMyBooksClicked(ActionEvent event) {
        logoutDialog();
    }

    private void initialSetupGUI() {
        fillComboBox(cmbBookStatus);
        fillComboBox(cmbStatusMyBooks);
        fillComboBox(cmbNewStatus);

        ObservableList<Tab> tabs = tabPaneMenu.getTabs();
        tabs.remove(tabSearch);
        tabs.remove(tabMyBooks);

        wrapTextArea(txaSearchPanel);
        wrapTextArea(txaMyBooks);
    }

    private void fillSearchCategory() {
        cmbCategory.getItems().addAll(SearchCategory.values());
        cmbCategory.getSelectionModel().selectFirst();
    }

    private void setupElementsListeners() {
        cmbStatusMyBooks.valueProperty().addListener(l -> btnSearchMyBooksClicked(new ActionEvent()));

        tabMyBooks.selectedProperty().addListener(l -> btnFetchBooksClicked(new ActionEvent()));

        cmbStatusMyBooks.setOnAction((event) -> updateUserBooksByPreferenceGUI());
    }


    private void showAlertMessage(Alert.AlertType type, String header, String context) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
    }

    private ObservableList<String> getObservableList(List<Book> books) {
        return FXCollections.observableList(books.stream().map(e -> {
            try {
                return String.format("%s, %s", e.getTitle(), e.getPublishedDate());
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }

    private List<Book> filterUserBooks(Predicate<Book> p) {
        List<Book> books = new ArrayList<>();

        userBooksResultsList.stream()
                            .filter(p)
                            .forEach(e -> {
                                books.add(e);
                                temporaryFilterBooks.add(e);
                            });

        return books;
    }

    private void clearMyBooksGUI() {
        txtSearchTitleMyBooks.clear();

        cmbStatusMyBooks.getSelectionModel().select(-1);
        listViewMyBooks.getSelectionModel().select(-1);

        txaMyBooks.clear();

        imgViewMyBooks.setImage(null);
    }

    private void clearTemporaryBooksList() {
        searchFilter = false;
        temporaryFilterBooks.clear();
    }

    private void fillComboBox(ComboBox<BookStatus> comboBox) {
        comboBox.getItems().addAll(BookStatus.values());
        comboBox.getSelectionModel().select(-1);
    }

    private void updateUserBooksByPreferenceGUI() {
        int selectedIndex = cmbStatusMyBooks.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            return;
        }

        txaMyBooks.clear();
        imgViewMyBooks.setImage(null);

        BookStatus preference = BookStatus.values()[selectedIndex];

        if (userBooksResultsList == null) {
            return;
        }

        List<Book> booksForCategory;

        if (searchFilter) {
            booksForCategory = temporaryFilterBooks.stream()
                                                   .filter(e -> userBookMap.get(e).equals(preference))
                                                   .collect(Collectors.toList());

        } else {
            booksForCategory = userBooksResultsList.stream()
                                                   .filter(e -> userBookMap.get(e).equals(preference))
                                                   .collect(Collectors.toList());
        }

        ObservableList<String> observableList = getObservableList(booksForCategory);

        listViewMyBooks.setItems(observableList);
    }

    private void wrapTextArea(TextArea textArea) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(textArea);

        textArea.setWrapText(true);
    }

    private void clearSearchResults() {
        listViewSearchPanel.getItems().clear();
        txaSearchPanel.clear();
        imgViewSearchPane.setImage(null);
    }

    private void logoutDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout confirmation");
        alert.setHeaderText("Logging out from Bookhub");
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                ObservableList<Tab> tabs = tabPaneMenu.getTabs();
                tabs.remove(tabSearch);
                tabs.remove(tabMyBooks);
                tabs.add(tabLogin);

                username = "";
                txtGreetUsername.setText("");
                txtMyBooksGreetUsername.setText("");

                userBookMap.clear();
                userBooksResultsList.clear();

                textSearch.clear();
                clearSearchResults();
                clearMyBooksGUI();
            }
        });
    }
}
