package implementations;


import api.interfaces.Book;
import api.interfaces.BookPreference;
import api.interfaces.SearchCategory;
import api.interfaces.ServerObjectInterface;
import database.DatabaseConnector;
import dto.BookTransfer;
import dto.Items;
import google.books.GoogleBooksAPI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ServerObjectInterfaceImpl extends UnicastRemoteObject implements ServerObjectInterface {

    private final GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();
    private final DatabaseConnector databaseConnector = new DatabaseConnector();

    public ServerObjectInterfaceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        return databaseConnector.registerUserInDB(username, password);
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        return databaseConnector.loginUserInDB(username, password);
    }

    @Override
    public List<Book> getBookByType(SearchCategory category, String argument) throws RemoteException {
        try {
            Items booksByTitle = googleBooksAPI.getBookFromGoogleAPIByType(category, argument);
            List<Book> result = new ArrayList<>();

            if (booksByTitle.getItems() != null) {
                for (BookTransfer item : booksByTitle.getItems()) {
                    BookTransfer.VolumeInfo volumeInfo = item.getVolumeInfo();

                    String notFoundImage = "https://drudesk.com/sites/default/files/styles/blog_page_header_1088x520" +
                                           "/public/2018-02/404-error-page-not-found.jpg?itok=YW-iShwf";

                    String imageLink = volumeInfo.getImageLinks() == null ?
                            notFoundImage : volumeInfo.getImageLinks().getSmallThumbnail();

                    result.add(new BookImpl(volumeInfo.getTitle(), item.getId(), volumeInfo.getAuthors(), volumeInfo.getPublisher(),
                            volumeInfo.getPublishedDate(), volumeInfo.getDescription(),
                            imageLink));
                }
                return result;
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addUserBookPreference(String username, Book book, BookPreference bookPreference) throws RemoteException {
        return databaseConnector.addUserPreferenceBookToDB(username, book, bookPreference);
    }

    @Override
    public Map<Book, BookPreference> getBooksForUser(String username) throws RemoteException {
        return databaseConnector.getBooksForUser(username);
    }
}
