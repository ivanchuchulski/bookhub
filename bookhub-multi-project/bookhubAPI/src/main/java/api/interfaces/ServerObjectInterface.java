package api.interfaces;

import api.enums.BookStatus;
import api.enums.SearchCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServerObjectInterface extends Remote {

    boolean register(String username, String password) throws RemoteException;

    boolean login(String username, String password) throws RemoteException;

    List<Book> getBookByType(SearchCategory category, String argument) throws RemoteException;

    boolean addUserBookPreference(String username, Book book, BookStatus bookStatus) throws RemoteException;

    Map<Book, BookStatus> getBooksForUser(String username) throws RemoteException;

    void removeBook(String username, String bookId) throws RemoteException;
}
