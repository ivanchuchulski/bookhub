package api.interfaces;

import api.dto.BookTransfer;
import api.dto.Items;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerObjectInterfaceImpl extends UnicastRemoteObject implements ServerObjectInterface {

    private GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();

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

                result.add(new BookImpl(volumeInfo.getTitle(), volumeInfo.getAuthors(), volumeInfo.getPublisher(),
                                        volumeInfo.getPublishedDate(), volumeInfo.getDescription(),
                                        volumeInfo.getImageLinks().getSmallThumbnail()));

            }
            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
