package org.example.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.NodesDAO;
import org.example.dao.UsersDAO;
import org.example.database.DatabaseConfig;
import org.example.encryption.AESEncryption;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Transaction {

    protected final String username;
    protected final double balance;
    protected final double amount;
    protected final AESEncryption encryption = new AESEncryption();
    protected final UsersDAO userDAO = new UsersDAO();
    protected final NodesDAO nodesDAO = new NodesDAO();

    protected Transaction(String username, double balance, double amount) {
        this.username = username;
        this.balance = balance;
        this.amount = amount;
    }
    public abstract boolean doTransaction() throws Exception;
    protected boolean sendToAffinity(String address){
        try (Socket socket = new Socket(address,6000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
            var objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(this);
            byte[] encrypted = encryption.encrypt(json);
            out.writeObject(encrypted);
            return in.readObject().toString().equals("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
