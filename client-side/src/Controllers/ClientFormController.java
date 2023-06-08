package Controllers;

import client.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController {

    public Button btnSend;
    public Button btnCamera;
    public Button btnEmoji;
    public static String username;
    public Label lblName;
    public AnchorPane clientContext;
    public ImageView imgProfile;
    public ComboBox cmbInfo;
    public TextField txtType;
    public VBox vBox;
    public ScrollPane sp_emoji;
    public GridPane gp_emoji;
    public FlowPane as;
    public ScrollPane spmain;
    private Client client;

    int[] emojis = {

            0x1F606, // 😆
            0x1F601, // 😁
            0x1F602, // 😂
            0x1F609, // 😉
            0x1F618, // 😘
            0x1F610, // 😐
            0x1F914, // 🤔
            0x1F642, // 🙂
            0x1F635, // 😵
            0x1F696, // 🚖
            0x1F636, // 😶
            0x1F980, // 🦀
            0x1F625, // 😥
            0x1F634, // 😴
            0x1F641, // 🙁
            0x1F643, // 🙂
            0x1F600 // 😀



    };

    public void MsgsendOnAction(MouseEvent mouseEvent) {
        sp_emoji.setVisible(false);
        String message = txtType.getText();

        if (!message.isEmpty()) {
            sendMessage(message);
            txtType.clear();
            client.clientSendMessage(message);

        }
    }


    public void stickerOnAction(ActionEvent actionEvent) {
        if (sp_emoji.isVisible()) {
            sp_emoji.setVisible(false);
        } else {
            sp_emoji.setVisible(true);
            Text text = new Text(new String(Character.toChars(emojis[4])));
            text.setStyle("-fx-font-size: 25px; -fx-font-family: 'Noto Emoji';");
            gp_emoji.add(text, 0, 0);
        }
    }

    public void sendMessage(String message) {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:  #d6c94f;-fx-background-radius:15;-fx-font-size: 14;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vBox.getChildren().add(hBox);

    }

    //static method for client class's access and update UI when recieving msg
    public static void receivemsg(String message,VBox vBox) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl); //label add to hbox
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox); //message added hbox add to vbox
            }
        });
    }


    public void cameraOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image to send.");
        System.out.println("choose file");
        File file = fileChooser.showOpenDialog(new Stage());
    }

    private void setEmojisToPane() {
        int codePoint = 0x1F600;   //emoji character code starting point
        for (int j = 0; j < 4; j++) {   //for add emojies to row wise
            for (int i = 0; i < 8; i++) {  //for add emojies to column wise
                Label text = new Label(new String(Character.toChars(codePoint)));   //Character code convert to string to show emoji
                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        txtType.appendText(text.getText());   //add selected emoji to textarea at ui
                    }
                });
                text.setStyle("-fx-font-size: 35px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gp_emoji.add(text,i,j); //add emojies to grid pane
                codePoint++;
            }
        }
    }

    public void initialize() {
        setEmojisToPane();

        sp_emoji.setVisible(false);
        sp_emoji.setStyle("-fx-background-color: coral;");

        if (username != null){
            lblName.setText(username);
        }else {
            System.err.println("username is null");
        }
//        usernameLabel.setText(username);
        try {
            client = new Client(new Socket("localhost", 1234), username);
            System.out.println(client+"line 150");
        } catch (IOException e) {
            e.printStackTrace();
        }

        clientContext.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                spmain.setVvalue((Double) newValue);
            }
        });
        client.listenForMessage(vBox);
        client.clientSendMessage("");
    }


}
