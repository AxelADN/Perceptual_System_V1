// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p;

import cFramework.communications.messages.IgniteEntityListMessage;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.fiels.Address;
import java.net.BindException;
import cFramework.nodes.NodeConf;
import cFramework.log.NodeLog;
import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;

public class IgniterProtocols implements Protocol
{
    P2PCommunications myCommunications;
    private NodeAddress serviceAddress;
    
    public IgniterProtocols(final NodeLog log) {
        this.serviceAddress = new NodeAddress(0L, "127.0.0.1", 60845);
        try {
            this.myCommunications = new P2PCommunications(this, new NodeConf(), log);
        }
        catch (BindException e) {
            System.out.println("Port already in use");
            e.printStackTrace();
        }
    }
    
    @Override
    public void receive(final Address address, final byte[] message) {
    }
    
    public void sendServiceUpRequest() {
    }
    
    public boolean isServiceUP() {
        return this.myCommunications.send(this.serviceAddress, new HelloMessage().toByteArray());
    }
    
    public void sendList(final String[] list, final NodeConf nc) {
        String prefix = "";
        final String path = System.getProperty("java.class.path");
        final StringBuilder builder = new StringBuilder();
        builder.append(path + ",");
        for (final String s : list) {
            builder.append(prefix);
            prefix = ",";
            builder.append(s);
        }
        final String newList = builder.toString();
        this.myCommunications.send(this.serviceAddress, new IgniteEntityListMessage(nc, newList).toByteArray());
    }
    
    public void stop() {
        this.myCommunications.stop();
    }
    
    @Override
    public P2PCommunications getCommunications() {
        return this.myCommunications;
    }
    
    @Override
    public NodeAddress getNodeAddress() {
        return new NodeAddress(0L, this.myCommunications.getAddress());
    }
}
