package server;

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        initRootScene(primaryStage);
    }

    private void initRootScene(Stage stage) throws IOException {
        URL rootSceneURL = Server.class.getResource(ROOT_SCENE_FXML_FILENAME);

        if (rootSceneURL == null) {
            throw new RuntimeException("could not load main scene fxml");
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
}
