// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.NodeAddress;
import java.util.concurrent.ArrayBlockingQueue;
import cFramework.communications.p2p.sockets.TCPPort;
import cFramework.communications.p2p.sockets.UDPPort;
import java.net.BindException;
import cFramework.nodes.NodeConf;
import cFramework.log.NodeLog;
import cFramework.communications.AddressAndBytes;
import java.util.concurrent.BlockingQueue;
import cFramework.communications.fiels.Address;
import cFramework.communications.p2p.sockets.Port;
import cFramework.communications.Protocol;
import cFramework.communications.BinaryArrayNotificable;

public class P2PCommunications implements BinaryArrayNotificable
{
    private Protocol protocolsLayer;
    private Port socketPort;
    private Address myAddress;
    private BlockingQueue<AddressAndBytes> messageQueue;
    private Thread messageQueueThread;
    private NodeLog log;
    
    public P2PCommunications(final Protocol protocols, final int port, final NodeLog log) throws BindException {
        this(protocols, new NodeConf(), port, log);
    }
    
    public P2PCommunications(final Protocol protocols, final NodeConf nc, final NodeLog log) throws BindException {
        this(protocols, nc, 0, log);
    }
    
    public P2PCommunications(final Protocol protocols, final NodeConf nc, final int port, final NodeLog log) throws BindException {
        this.protocolsLayer = protocols;
        this.log = log;
        if (nc.isUDP()) {
            this.socketPort = new UDPPort(this, log);
        }
        else {
            this.socketPort = new TCPPort(this, log);
        }
        this.myAddress = this.socketPort.setUp(port);
        if (this.myAddress == null) {
            throw new BindException();
        }
        this.socketPort.startListening();
        this.messageQueue = new ArrayBlockingQueue<AddressAndBytes>(1024);
        (this.messageQueueThread = new Thread() {
            @Override
            public void run() {
                boolean run = true;
                while (run) {
                    try {
                        final AddressAndBytes m = P2PCommunications.this.messageQueue.take();
                        P2PCommunications.this.protocolsLayer.receive(m.getAddress(), m.getBytes());
                    }
                    catch (InterruptedException e) {
                        log.developer("MessageQueue closed");
                        run = false;
                    }
                }
            }
        }).start();
    }
    
    public boolean send(final NodeAddress address, final byte[] message) {
        if (address instanceof LocalJVMNodeAddress) {
            ((LocalJVMNodeAddress)address).getNodeReference().receive(this.myAddress, message);
            return true;
        }
        return this.socketPort.send(address.getAddress(), message);
    }
    
    @Override
    public void receive(final Address address, final byte[] message) {
        try {
            this.messageQueue.put(new AddressAndBytes(address, message));
        }
        catch (InterruptedException e) {
            this.log.debug("Error enqueueing message");
            final StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            this.log.developer(errors.toString());
        }
    }
    
    public Address getAddress() {
        return this.myAddress;
    }
    
    public void stop() {
        this.socketPort.stopListening();
        this.messageQueueThread.interrupt();
    }
}
