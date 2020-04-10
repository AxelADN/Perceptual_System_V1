// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.entity;

import cFramework.communications.messages.base.Message;
import cFramework.util.IDHelper;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.BindException;
import java.io.IOException;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import cFramework.log.NodeLog;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.nodes.NodeConf;
import cFramework.communications.p2p.EntityProtocols;
import cFramework.communications.messages.MessageReceiverable;

public class Entity implements MessageReceiverable
{
    EntityProtocols communications;
    NodeConf nc;
    private AreasManager areas;
    private NodeRouteTable routeTable;
    private NodeLog log;
    
    public Entity(final String[] areasNames, final NodeConf nc) {
        this.nc = nc;
        this.log = new NodeLog("Entity " + nc.getEntityID(), nc.isDebug());
        this.routeTable = SingletonNodeRouteTable.getInstance();
        try {
            this.communications = new EntityProtocols(this, this.nc, 32456, this.routeTable, this.log);
        }
        catch (BindException e2) {
            this.log.message("Port already in use");
            this.log.message("It is posible that an entity with the EntityID " + nc.getEntityID() + " is already running");
            this.log.message("Press Enter to close");
            try {
                System.in.read();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        (this.areas = new AreasManager(this.routeTable, this.communications, this.log)).addList(areasNames, nc);
        new Thread() {
            @Override
            public void run() {
                final BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            Label_0018_Outer:
                while (true) {
                    while (true) {
                        try {
                            while (true) {
                                final String command = buffer.readLine();
                                final String[] words = command.split(" ");
                                if (words[0].toUpperCase().equals("FAIL")) {
                                    long id = 0L;
                                    if (words.length == 3) {
                                        id = IDHelper.generateID(words[1], words[2]);
                                    }
                                    else {
                                        id = IDHelper.generateID(words[1]);
                                    }
                                    System.out.println("send fail to ID: " + id);
                                }
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            continue Label_0018_Outer;
                        }
                    }
                }
            }
        }.start();
    }
    
    @Override
    public void receive(final Message m) {
    }
    
    public static void main(final String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        String list = "";
        int nodeConfValue = 0;
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("--list")) {
                list = args[i + 1];
            }
            else if (args[i].equals("--nodeConfiguration")) {
                nodeConfValue = Integer.parseInt(args[i + 1]);
            }
        }
        new Entity(list.split(","), new NodeConf(nodeConfValue));
    }
}
