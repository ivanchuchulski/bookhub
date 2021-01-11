package api.interfaces;

import api.dto.Items;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    public Items getBookByTitle(String title) throws RemoteException {

        Items booksByTitle = null;

        try {
            booksByTitle = googleBooksAPI.getBookFromGoogleAPI(title);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return booksByTitle;
    }
}
