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
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server extends Application {
    private static final int SERVER_REGISTRY_PORT = BookhubServerConfig.REGISTRY_PORT;

    private static final String SERVER_INTERFACE_REGISTRY_NAME = "interface";
    private static final String ROOT_SCENE_FXML_FILENAME = "server.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        initRMIRegistry();

        initRootScene(stage);
    }

    private void initRMIRegistry() throws RemoteException, AlreadyBoundException {
        ServerObjectInterface server = new ServerObjectInterfaceImpl();
        Registry registry = LocateRegistry.createRegistry(SERVER_REGISTRY_PORT);

        registry.bind(SERVER_INTERFACE_REGISTRY_NAME, server);

        System.out.println("registered server object successfully");
    }

    private void initRootScene(Stage stage) throws IOException {
        URL rootSceneURL = Server.class.getResource(ROOT_SCENE_FXML_FILENAME);

        if (rootSceneURL == null) {
            throw new AssertionError("could not load main scene fxml");
        }

        Parent root = FXMLLoader.load(rootSceneURL);

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Server control panel");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();

        System.out.println("started main scene");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
