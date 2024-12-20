package org.example.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.example.dao.UsersDAO;
import org.example.encryption.AESEncryption;
import org.example.model.Credentials;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BroadcastReceiver implements Runnable{
    private final AESEncryption encryption = new AESEncryption();
    private final UsersDAO usersDAO = new UsersDAO();
    private final int port;

    public BroadcastReceiver(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port);){
            // Create a datagram socket
            // Set the socket to receive broadcast messages
            socket.setBroadcast(true);

            // Create a buffer for incoming packets
            byte[] buffer = new byte[1024];

            // Listen for incoming packets
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byte[] message = packet.getData();
                ObjectMapper objectMapper = new ObjectMapper();
                Document document = objectMapper.readValue(encryption.decrypt(message), Document.class);
                usersDAO.update("username",document.get("username"),document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
