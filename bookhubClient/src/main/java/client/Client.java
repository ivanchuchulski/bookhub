package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Client extends Application {
    private static final String ROOT_SCENE_FXML_FILENAME = "client.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        URL rootSceneURL = Client.class.getResource(ROOT_SCENE_FXML_FILENAME);

        if (rootSceneURL == null) {
            throw new RuntimeException("could not load main scene fxml");
        }

        Parent root = FXMLLoader.load(rootSceneURL);
        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("client");
        stage.setScene(scene);
        stage.setOnCloseRequest( event-> {
            Platform.exit();
            System.exit(0);
        });
        stage.show();
    }

}
