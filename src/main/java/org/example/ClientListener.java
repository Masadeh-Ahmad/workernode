package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ClientListener implements Runnable{

    private final ExecutorService executor = WorkerNode.getExecutor();
    public final int port;

    public ClientListener(int port){
        this.port = port;


    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(900000);
                executor.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
