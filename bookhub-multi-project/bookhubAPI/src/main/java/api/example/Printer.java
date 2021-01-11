package api.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Printer extends Remote {
    void print(Student student) throws RemoteException;
}
