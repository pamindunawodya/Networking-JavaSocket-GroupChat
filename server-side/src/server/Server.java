package server;

import client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new Client has Connected!");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            stopServer();

        }
    }

    public void stopServer() {
        try {
            if (serverSocket!=null){
                serverSocket.close();
                System.out.println("server stopped");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public  static void main(String[]args) throws IOException {
//        ServerSocket serverSocket=new ServerSocket(1234);
//        Server server=new Server(serverSocket);
//        server.startServer();
//    }

}