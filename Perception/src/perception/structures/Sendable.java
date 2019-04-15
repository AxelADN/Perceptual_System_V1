/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;
import perception.config.AreaNames;

/**
 *
 * @author AxelADN
 */
public class Sendable implements Serializable {
    
    private final Object obj;
    private final ArrayList<Integer> sender;
    private final ArrayList<Integer> receiver;
    private int sendID;
    private static int ID = 0;
    
    public Sendable() {
        this.receiver = new ArrayList<>();
        this.sender = new ArrayList<>();
        this.obj = (int) 0;
        this.sender.add(AreaNames.LostData);
        this.receiver.add(AreaNames.LostData);
        this.sendID = 0;
    }
    
    public Sendable(Object data) {
        this.receiver = new ArrayList<>();
        this.sender = new ArrayList<>();
        this.obj = data;
        this.sender.add(AreaNames.LostData);
        this.receiver.add(AreaNames.LostData);
        this.sendID = 0;
    }
    
    public Sendable(Object data, int sender) {
        this.receiver = new ArrayList<>();
        this.sender = new ArrayList<>();
        this.obj = data;
        this.sender.add(sender);
        this.receiver.add(AreaNames.LostData);
        this.sendID = 0;
    }
    
  
    
    public Sendable(Object data, int sender, ArrayList<Integer> receiver) {
        this.sender = new ArrayList<>();
        this.obj = data;
        this.sender.add(sender);
        this.receiver = (ArrayList<Integer>) receiver.clone();
        this.sendID = 0;
    }
    
    public Sendable(Object data, int sender, int receiver) {
        this.receiver = new ArrayList<>();
        this.sender = new ArrayList<>();
        this.obj = data;
        this.sender.add(sender);
        this.receiver.add(receiver);
        this.sendID = 0;
    }
    
    public Sendable(Object data, int sender, ArrayList<Integer> trace, ArrayList<Integer> receiver) {
        this.sender = (ArrayList<Integer>)trace.clone();
        this.obj = data;
        this.sender.add(sender);
        this.receiver = (ArrayList<Integer>) receiver.clone();
        this.sendID = 0;
    }
    
    public Sendable(Object data, int sender, ArrayList<Integer> trace,int receiver) {
        this.receiver = new ArrayList<>();
        this.sender = (ArrayList<Integer>)trace.clone();
        this.obj = data;
        this.sender.add(sender);
        this.receiver.add(receiver);
        this.sendID = 0;
    }
    
    public void assignID() {
        this.sendID = ID;
        ID++;
    }
    
    public int getID() {
        return sendID;
    }
    
    public ArrayList<Integer> getReceivers() {
        return this.receiver;
    }
    
    public int getReceiver() {
        return this.receiver.get(0);
    }
    
    public int getSender() {
        return this.sender.get(this.sender.size()-1);
    }
    
    public ArrayList<Integer> getTrace() {
        return this.sender;
    }
    
    public Object getData() {
        return this.obj;
    }
    
}
