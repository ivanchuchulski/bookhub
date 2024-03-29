package implementations;

import api.enums.BookStatus;
import api.enums.SearchCategory;
import api.interfaces.Book;
import api.interfaces.ServerObjectInterface;
import database.DatabaseConnector;
import dto.BookTransfer;
import dto.Items;
import exceptions.BookQueryException;
import google.books.GoogleBooksAPI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServerObjectInterfaceImpl extends UnicastRemoteObject implements ServerObjectInterface {
    private static final String NOT_FOUND_IMAGE_URL = "https://drudesk.com/sites/default/files/styles" +
            "/blog_page_header_1088x520" +
            "/public/2018-02/404-error-page-not-found.jpg?itok=YW-iShwf";

    private final GoogleBooksAPI googleBooksAPI;
    private final DatabaseConnector databaseConnector;

    public ServerObjectInterfaceImpl() throws RemoteException {
        super();
        googleBooksAPI = new GoogleBooksAPI();
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        if (databaseConnector.isUserRegistered(username, password)) {
            return false;
        }

        return databaseConnector.registerUserInDB(username, password);
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        return databaseConnector.isUserRegistered(username, password);
    }

    @Override
    public List<Book> getBookByType(SearchCategory category, String argument) throws RemoteException {
        try {
            Items booksByTitle = googleBooksAPI.getBookFromGoogleAPIByType(category, argument);
            List<Book> result = new ArrayList<>();

            if (booksByTitle.getItems() != null) {
                for (BookTransfer item : booksByTitle.getItems()) {
                    BookTransfer.VolumeInfo volumeInfo = item.getVolumeInfo();

                    String imageLink = volumeInfo.getImageLinks() == null ?
                            NOT_FOUND_IMAGE_URL : volumeInfo.getImageLinks().getSmallThumbnail();

                    result.add(new BookImpl(volumeInfo.getTitle(), item.getId(), volumeInfo.getAuthors(),
                                            volumeInfo.getPublisher(), volumeInfo.getPublishedDate(),
                                            volumeInfo.getDescription(), imageLink));
                }
                return result;
            }

        } catch (IOException | BookQueryException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    @Override
    public boolean addUserBookPreference(String username, Book book, BookStatus bookStatus) throws RemoteException {
        return databaseConnector.addUserPreferenceBookToDB(username, book, bookStatus);
    }

    @Override
    public Map<Book, BookStatus> getBooksForUser(String username) throws RemoteException {
        return databaseConnector.getBooksForUser(username);
    }

    @Override
    public void removeBook(String username, String bookId) throws RemoteException {
        databaseConnector.removeBookPreference(username, bookId);
    }
}
