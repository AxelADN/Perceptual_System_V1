// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.service;

import cFramework.nodes.entity.Entity;
import cFramework.util.OSHelper;
import cFramework.nodes.NodeConf;
import cFramework.log.NodeLog;
import cFramework.communications.p2p.ServiceProtocol;

public class Service
{
    ServiceProtocol protocol;
    NodeLog log;
    
    public Service() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        (this.log = new NodeLog("Service", null)).message("This is the Cmiddle Service, it is not necessary shut it down to run a new entity");
        this.protocol = new ServiceProtocol(this, this.log);
    }
    
    public void createEntity(final NodeConf nc, final String list) {
        this.log.message("Setting up Entity " + nc.getEntityID());
        final String path = list.substring(0, list.indexOf(44));
        final String areas = list.substring(list.indexOf(44) + 1);
        String command = "java -cp " + OSHelper.preparePath(path) + " ";
        command = command + Entity.class.getName() + " ";
        command = command + "--list " + areas + " ";
        command = command + "--nodeConfiguration " + nc.toInt() + " ";
        OSHelper.exec(command);
    }
    
    public static void main(final String[] args) {
        new Service();
    }
}
