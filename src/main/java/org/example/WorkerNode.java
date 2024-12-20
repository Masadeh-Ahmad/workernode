package org.example;

import org.example.broadcast.BroadcastReceiver;
import org.example.broadcast.BroadcastSender;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerNode {

    private static final String NODE_ID = "node01";
    private static final String bootStrapAddress = "localhost";
    private static final int bootStrapPort = 4000;
    private static final ExecutorService executor = new ThreadPoolExecutor(2, 100, 60L, TimeUnit.SECONDS, new java.util.concurrent.LinkedBlockingQueue<>());
    private static final BroadcastSender broadcastSender = new BroadcastSender(5000);
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver(5000);
    private final TransactionReceiver transactionReceiver = new TransactionReceiver(6000);
    private final ClientListener clientListener = new ClientListener(8000);
    public void run() throws IOException {
        try {
            Socket bootstrap = new Socket(bootStrapAddress,bootStrapPort);
            executor.execute(broadcastReceiver);
            executor.execute(transactionReceiver);
            executor.execute(clientListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNodeId() {
        return NODE_ID;
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static BroadcastSender getBroadcastSender() {
        return broadcastSender;
    }
    public static String getBootStrapAddress(){
        return bootStrapAddress;
    }
    public static int getBootStrapPort(){
        return bootStrapPort;
    }
}
