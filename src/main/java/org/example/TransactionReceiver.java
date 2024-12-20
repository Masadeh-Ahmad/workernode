package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class TransactionReceiver implements Runnable{
    private final ExecutorService executor = WorkerNode.getExecutor();
    private final int port;

    public TransactionReceiver(int port) {
        this.port = port;
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new TransactionHandler(clientSocket));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
