package server;

import database.DatabaseConnector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AdminUI extends Application {
    private static final String ROOT_SCENE_FXML_FILENAME = "server.fxml";

    private static DatabaseConnector databaseConnector;

    public static void setDatabaseConnector(DatabaseConnector databaseConnector) {
        AdminUI.databaseConnector = databaseConnector;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initRootScene(primaryStage);
    }

    private void initRootScene(Stage stage) throws IOException {
        URL rootSceneURL = Main.class.getResource(ROOT_SCENE_FXML_FILENAME);

        if (rootSceneURL == null) {
            throw new RuntimeException("could not load main scene fxml");
        }

        FXMLLoader rootSceneLoader = new FXMLLoader(rootSceneURL);
        Parent root = rootSceneLoader.load();

        // init controller dependencies
        ServerController serverController = rootSceneLoader.getController();
        serverController.setDatabaseConnector(databaseConnector);

        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("Server control panel");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            databaseConnector.disconnect();
            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }
}
