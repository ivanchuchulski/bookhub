package api.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerObjectInterface extends Remote {

    boolean register(String username, String password) throws RemoteException;

    boolean login(String username, String password) throws RemoteException;

    List<Book> getBookByType(SearchCategory category, String argument) throws RemoteException;

    boolean addUserBookPreference(String username, String title, BookPreference bookPreference) throws RemoteException;
}
