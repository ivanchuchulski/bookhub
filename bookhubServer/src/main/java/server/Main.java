package server;

import api.interfaces.ServerObjectInterface;
import database.DatabaseConnector;
import implementations.ServerObjectInterfaceImpl;
import javafx.application.Application;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    private static final int SERVER_REGISTRY_PORT = BookhubServerConfig.REGISTRY_PORT;
    private static final String SERVER_INTERFACE_REGISTRY_NAME = BookhubServerConfig.SERVER_INTERFACE_REGISTRY_NAME;

    public static void main(String[] args) {
        try {
            boolean startAdminGUI = true;

            ServerObjectInterface server = new ServerObjectInterfaceImpl();
            Registry registry = LocateRegistry.createRegistry(SERVER_REGISTRY_PORT);

            registry.bind(SERVER_INTERFACE_REGISTRY_NAME, server);

            if (startAdminGUI) {
                DatabaseConnector databaseConnector = new DatabaseConnector();
                AdminUI.setDatabaseConnector(databaseConnector);
                Application.launch(AdminUI.class);
            }

        } catch (AlreadyBoundException | RemoteException e) {
            throw new RuntimeException("error registering objects", e);
        }
    }
}
