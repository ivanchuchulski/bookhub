package server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ServerController {

    @FXML
    private Label lblAccountInfo;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblPrivilege;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private ComboBox<?> cmbPrivilege;

    @FXML
    private Label lblViewData;

    @FXML
    private TextArea txaLog;

    @FXML
    private Button btnOpenByEncryption;

    @FXML
    private Button btnOpenByCard;

    @FXML
    private Label lblTableView;

    @FXML
    private TableView<?> tbvTableView;

    @FXML
    private Button btnViewUsers;

    @FXML
    private Button btnViewCardNumbers;

    @FXML
    void btnOpenByCardClicked(ActionEvent event) {

    }

    @FXML
    void btnOpenByEncryptionClicked(ActionEvent event) {

    }

    @FXML
    void btnViewCardNumbersClicked(ActionEvent event) {

    }

    @FXML
    void btnViewUsersClicked(ActionEvent event) {

    }

}
