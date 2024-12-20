package org.example;

import org.example.dao.UsersDAO;
import org.example.encryption.AESEncryption;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NewUserReceiver implements Runnable{
    private final UsersDAO usersDAO = new UsersDAO();
    private final AESEncryption encryption = new AESEncryption();

    @Override
    public void run() {
        while (true) {
            try (Socket bootstrap = new Socket(WorkerNode.getBootStrapAddress(), WorkerNode.getBootStrapPort());
                 ObjectInputStream in = new ObjectInputStream(bootstrap.getInputStream())) {
                bootstrap.setSoTimeout(2000);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
