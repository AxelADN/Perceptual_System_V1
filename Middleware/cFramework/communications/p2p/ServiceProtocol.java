// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p;

import cFramework.communications.messages.HandShakeMessage;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.NodeAddress;
import cFramework.communications.messages.IgniteEntityListMessage;
import cFramework.communications.messages.base.Message;
import cFramework.communications.fiels.Address;
import java.net.BindException;
import cFramework.nodes.NodeConf;
import cFramework.log.NodeLog;
import cFramework.nodes.service.Service;
import cFramework.communications.multicast.Multicast;
import cFramework.communications.Protocol;

public class ServiceProtocol implements Protocol
{
    private P2PCommunications myCommunications;
    private Multicast multicastConection;
    private Service service;
    private boolean isEntityUp;
    private NodeLog log;
    
    public ServiceProtocol(final Service service, final NodeLog logger) {
        this.isEntityUp = false;
        this.service = service;
        this.log = logger;
        final NodeConf nc = new NodeConf();
        nc.setEntityID((byte)(-1));
        try {
            this.myCommunications = new P2PCommunications(this, 60845, this.log);
            (this.multicastConection = new Multicast(this, nc, this.log)).setUp();
            this.log.message("Running on 60845");
        }
        catch (BindException e) {
            this.log.message("Port already in use");
        }
    }
    
    @Override
    public void receive(final Address address, final byte[] message) {
        final Message m = Message.getMessage(message);
        if (m.getOperationCode() == 30) {
            final NodeConf nc = ((IgniteEntityListMessage)m).getNodeConfiguration();
            if (!nc.isLocal()) {
                final String list = ((IgniteEntityListMessage)m).getList();
                this.myCommunications.send(new NodeAddress(0L, new Address("127.0.0.1", 32456 + nc.getEntityID())), new HelloMessage().toByteArray());
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500L);
                            if (!ServiceProtocol.this.isEntityUp) {
                                ServiceProtocol.this.service.createEntity(nc, list);
                            }
                            else {
                                ServiceProtocol.this.log.message("Entity " + nc.getEntityID() + " Already UP");
                            }
                            ServiceProtocol.this.isEntityUp = false;
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
        else if (m.getOperationCode() == 50) {
            this.myCommunications.send(new NodeAddress(0L, address), new HandShakeMessage().toByteArray());
        }
        else if (m.getOperationCode() == 49) {
            this.isEntityUp = true;
        }
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
