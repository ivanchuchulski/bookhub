package api.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Student extends Remote {
    String getName() throws RemoteException;
}
