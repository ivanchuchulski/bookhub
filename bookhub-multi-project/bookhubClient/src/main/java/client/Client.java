package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("client.fxml"));
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
