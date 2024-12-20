package org.example.broadcast;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.example.encryption.AESEncryption;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastSender {
    private final AESEncryption encryption = new AESEncryption();
    private final int port;
    public BroadcastSender(int port){
        this.port = port;
    }
    public void send(Document document){
        try (DatagramSocket socket = new DatagramSocket()){

            // Set the broadcast flag on the socket
            socket.setBroadcast(true);

            // Create a broadcast address
            InetAddress broadcast = InetAddress.getByName("255.255.255.255");

            // Send a message to the broadcast address

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(document);
            byte[] buffer = encryption.encrypt(json);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcast, port);
            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
