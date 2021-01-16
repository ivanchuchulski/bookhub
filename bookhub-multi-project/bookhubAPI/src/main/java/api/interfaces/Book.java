package api.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Book extends Remote {

    String getId() throws RemoteException;

    String getTitle() throws RemoteException;

    String[] getAuthors() throws RemoteException;

    String getPublisher() throws RemoteException;

    String getPublishedDate() throws RemoteException;

    String getDescription() throws RemoteException;

    String getSmallThumbnailLink() throws RemoteException;

    String printInfo() throws RemoteException;
}
