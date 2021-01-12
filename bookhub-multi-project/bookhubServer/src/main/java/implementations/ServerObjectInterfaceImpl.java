package implementations;

import dto.BookTransfer;
import dto.Items;
import api.interfaces.BookImpl;
import google.books.GoogleBooksAPI;
import api.interfaces.ServerObjectInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerObjectInterfaceImpl extends UnicastRemoteObject implements ServerObjectInterface {

    private final GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();

    public ServerObjectInterfaceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean loginUser(String username, String password) throws RemoteException {
        return true;
    }

    @Override
    public List<BookImpl> getBookByTitle(String title) throws RemoteException {
        try {
            Items booksByTitle = googleBooksAPI.getBookFromGoogleAPI(title);
            List<BookImpl> result = new ArrayList<>();

            for (BookTransfer item : booksByTitle.getItems()) {
                BookTransfer.VolumeInfo volumeInfo = item.getVolumeInfo();

                String notFoundImage = "https://drudesk.com/sites/default/files/styles/blog_page_header_1088x520" +
                        "/public/2018-02/404-error-page-not-found.jpg?itok=YW-iShwf";

                String imageLink = volumeInfo.getImageLinks() == null ?
                        notFoundImage : volumeInfo.getImageLinks().getSmallThumbnail();

                result.add(new BookImpl(volumeInfo.getTitle(), volumeInfo.getAuthors(), volumeInfo.getPublisher(),
                                        volumeInfo.getPublishedDate(), volumeInfo.getDescription(),
                                        imageLink));
            }
            return result;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
