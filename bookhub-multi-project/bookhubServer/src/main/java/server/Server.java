package server;

import api.interfaces.ServerObjectInterface;
import implementations.ServerObjectInterfaceImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        ServerObjectInterface server = new ServerObjectInterfaceImpl(); // create Interface instance
        Registry registry = LocateRegistry.createRegistry(7777); //  create registry
        registry.bind("interface", server); // bind registry to object

        System.out.println("Object registered successfully.");

        Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Server control panel");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();

    }
}
