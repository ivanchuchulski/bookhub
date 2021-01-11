package api.interfaces;

import api.dto.Items;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerObjectInterface extends Remote {
    boolean loginUser(String username, String password) throws RemoteException;

    Items getBookByTitle(String title) throws RemoteException;

}
