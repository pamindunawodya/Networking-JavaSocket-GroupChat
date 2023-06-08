package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginContext;
    public JFXTextField txtUsername;
    public JFXButton btnLogin;

    public void loginOnAction(ActionEvent actionEvent) {
        if (!txtUsername.getText().isEmpty()) {
            ClientFormController.username = txtUsername.getText();
            Stage stage = (Stage) loginContext.getScene().getWindow();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../Resources/view/ClientForm.fxml"))));
                stage.setMaximized(false);
                stage.setResizable(false);
                stage.setTitle(txtUsername.getText() + " | Play Tech Chat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Enter your Name !..").showAndWait();
        }
    }
}
