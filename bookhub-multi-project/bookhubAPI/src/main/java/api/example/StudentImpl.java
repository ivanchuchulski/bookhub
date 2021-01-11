package api.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StudentImpl extends UnicastRemoteObject implements Student {
    private final String name;

    public StudentImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }
}
