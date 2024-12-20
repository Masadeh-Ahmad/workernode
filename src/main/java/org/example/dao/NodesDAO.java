package org.example.dao;

import org.example.database.DatabaseConfig;


public class NodesDAO extends MongoDAO {

    public NodesDAO(){
        super("nodes");
    }
}
