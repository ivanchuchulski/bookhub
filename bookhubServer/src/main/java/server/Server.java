package server;

import api.interfaces.ServerObjectInterface;
import implementations.ServerObjectInterfaceImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static final int SERVER_REGISTRY_PORT = BookhubServerConfig.REGISTRY_PORT;
    private static final String SERVER_INTERFACE_REGISTRY_NAME = BookhubServerConfig.SERVER_INTERFACE_REGISTRY_NAME;

    public Server(boolean startAdmin) {
        try {
            ServerObjectInterface server = new ServerObjectInterfaceImpl();
            Registry registry = LocateRegistry.createRegistry(SERVER_REGISTRY_PORT);

            registry.bind(SERVER_INTERFACE_REGISTRY_NAME, server);

            System.out.println("registered server object successfully");

            if (startAdmin) {
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
