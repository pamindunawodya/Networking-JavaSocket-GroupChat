package client;

import Controllers.ClientFormController;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(socket+"client socket");
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void clientSendMessage(String message) {
        try {
            System.out.println(message+"line 29");
            bufferedWriter.write(username + " : " + message);
            bufferedWriter.newLine();
            System.out.println(message);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void listenForMessage(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        String message = bufferedReader.readLine();
                        ClientFormController.receivemsg(message, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything(socket, bufferedWriter, bufferedReader);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
