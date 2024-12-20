package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.UsersDAO;
import org.example.database.DatabaseConfig;
import org.example.dto.UserDTO;
import org.example.encryption.AESEncryption;
import org.example.model.Credentials;
import org.example.model.Transaction;
import org.example.model.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    private final AESEncryption encryption = new AESEncryption();
    private final UsersDAO usersDAO;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        usersDAO = new UsersDAO();
    }
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())){
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] encryptedData = (byte[])in.readObject();
            Credentials credentials = objectMapper.readValue(encryption.decrypt(encryptedData), Credentials.class);
            User user = checkCredentials(credentials);
            if(user == null){
                out.writeObject(null);
                throw new NoSuchElementException();
            }
            String json = objectMapper.writeValueAsString(new UserDTO(user.getUsername(), user.getBalance()));
            byte[] encrypted = encryption.encrypt(json);
            out.writeObject(encrypted);

            encryptedData = (byte [])in.readObject();
            Transaction transaction = objectMapper.readValue(encryption.decrypt(encryptedData),Transaction.class);
            if(transaction.doTransaction())
                out.writeObject("OK");
            else
                out.writeObject(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private User checkCredentials(Credentials credentials) throws Exception {
        User user = User.fromDocument(usersDAO.read("username",credentials.getUsername()));
        if (user != null && user.checkPassword(credentials.getPassword()))
            return user;
        return null;
    }


}
