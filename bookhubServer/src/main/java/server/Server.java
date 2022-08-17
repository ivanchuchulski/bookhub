package server;

import api.interfaces.ServerObjectInterface;
import database.DatabaseConnector;
import implementations.ServerObjectInterfaceImpl;
import javafx.application.Application;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static final int SERVER_REGISTRY_PORT = BookhubServerConfig.REGISTRY_PORT;
    private static final String SERVER_INTERFACE_REGISTRY_NAME = BookhubServerConfig.SERVER_INTERFACE_REGISTRY_NAME;

    public Server(boolean startAdminGUI) {
        try {
            ServerObjectInterface server = new ServerObjectInterfaceImpl();
            Registry registry = LocateRegistry.createRegistry(SERVER_REGISTRY_PORT);

            registry.bind(SERVER_INTERFACE_REGISTRY_NAME, server);

            System.out.println("registered server object successfully");

            if (startAdminGUI) {
                DatabaseConnector databaseConnector = new DatabaseConnector();
                AdminUI.setDatabaseConnector(databaseConnector);
                Application.launch(AdminUI.class);
            }

        } catch (AlreadyBoundException | RemoteException e) {
            throw new RuntimeException("error registering objects", e);
        }
    }


    public static void main(String[] args) {
        new Server(true);
    }
}
