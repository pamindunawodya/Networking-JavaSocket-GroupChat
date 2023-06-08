package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler>clientHandlers=new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientname;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
         this.bufferedWriter=  new BufferedWriter(new OutputStreamWriter( socket.getOutputStream()));
         this.bufferedReader=  new BufferedReader(new InputStreamReader(socket.getInputStream()));
         this .clientname=bufferedReader.readLine();
         clientHandlers.add(this);
         broadcastMessage("SERVER:" +clientname+ "joined the Chat!");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }




    @Override
    public void run() {

        String message;
        while (socket.isConnected()){
            try {
                message=bufferedReader.readLine();
                broadcastMessage(message);
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler:clientHandlers){
            try{
                if(!clientHandler.clientname.equals(clientname)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER" +clientname +"has leaving the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
                System.out.println("A client left from chat");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
