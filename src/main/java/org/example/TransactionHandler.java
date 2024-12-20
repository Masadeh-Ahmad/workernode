package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.encryption.AESEncryption;
import org.example.model.Transaction;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TransactionHandler implements Runnable{
    private final Socket clientSocket;
    private final AESEncryption encryption = new AESEncryption();

    public TransactionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())){
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] encryptedData = (byte [])in.readObject();
            Transaction transaction = objectMapper.readValue(encryption.decrypt(encryptedData),Transaction.class);
            if(transaction.doTransaction())
                out.writeObject("OK");
            else
                out.writeObject(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
