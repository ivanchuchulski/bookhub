package api.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterImpl extends UnicastRemoteObject implements Printer {
    public PrinterImpl() throws RemoteException {
        super();
    }

    @Override
    public void print(Student student) throws RemoteException {
        System.out.println(student.getName());
    }
}
