package org.example.model;

import org.bson.Document;
import org.example.WorkerNode;

public class WithDraw extends Transaction{
    protected WithDraw(String username, double balance, double amount) {
        super(username, balance, amount);
    }

    @Override
    public boolean doTransaction() throws Exception {
        Document document = userDAO.read("username",username);
        if(!WorkerNode.getNodeId().equals(document.getString("affinityNodeId"))){
            Document nodeDoc = nodesDAO.read("nodeId",document.getString("affinityNodeId"));
            return sendToAffinity(nodeDoc.getString("address"));
        }
        double balance = (double)document.get("balance");
        if(balance == this.balance && balance >= amount){
            document.put("balance",balance - amount);
            userDAO.update("username",username,document);
            return true;
        }
        return false;
    }
}
