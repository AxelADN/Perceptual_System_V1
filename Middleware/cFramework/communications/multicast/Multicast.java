// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.multicast;

import java.net.DatagramPacket;
import java.io.IOException;
import cFramework.nodes.NodeConf;
import cFramework.communications.fiels.Address;
import cFramework.log.NodeLog;
import java.net.InetAddress;
import cFramework.communications.Protocol;
import java.net.MulticastSocket;

public class Multicast extends Thread
{
    private MulticastSocket socket;
    private Protocol receiver;
    private InetAddress group;
    private NodeLog logger;
    private Address address;
    private NodeConf nc;
    
    public Multicast(final Protocol receiver, final NodeConf nc, final NodeLog temporal) {
        this.receiver = receiver;
        this.logger = temporal;
        this.nc = nc;
    }
    
    public void setUp() {
        try {
            this.address = new Address("228.5.6.7", 6789 + this.nc.getEntityID());
            this.group = InetAddress.getByName(this.address.getIp());
            this.socket = new MulticastSocket(this.address.getPort());
            this.start();
            this.socket.joinGroup(this.group);
        }
        catch (IOException ex) {
            this.logger.error(ex.getMessage());
        }
    }
    
    public void leaveGroup() {
        try {
            this.socket.leaveGroup(this.group);
        }
        catch (IOException ex) {
            this.logger.error(ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        DatagramPacket input = null;
        while (true) {
            byte[] buffer = null;
            try {
                buffer = new byte[1024];
                input = new DatagramPacket(buffer, buffer.length);
                this.socket.receive(input);
            }
            catch (IOException ex) {
                this.logger.error(ex.getMessage());
            }
            this.receiver.receive(null, buffer);
        }
    }
    
    public void send(final byte[] m) {
        try {
            this.logger.developer("Send data to multicast group");
            final DatagramPacket output = new DatagramPacket(m, m.length, this.group, this.address.getPort());
            this.socket.send(output);
        }
        catch (IOException ex) {
            this.logger.error(ex.getMessage());
        }
    }
}
