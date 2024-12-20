package org.example;

import org.example.database.DatabaseConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConfig.of("mongodb://localhost:27018","worker");
            WorkerNode workerNode = new WorkerNode();
            workerNode.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}