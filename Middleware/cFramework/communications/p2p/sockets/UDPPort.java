// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p.sockets;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import cFramework.communications.fiels.Address;
import cFramework.log.NodeLog;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import cFramework.communications.BinaryArrayNotificable;

public class UDPPort implements Port
{
    private BinaryArrayNotificable messageReceiver;
    private DatagramSocket datagramSocket;
    private DatagramPacket receiveMessage;
    private boolean run;
    private NodeLog log;
    
    public UDPPort(final BinaryArrayNotificable messageManager, final NodeLog log) {
        this.messageReceiver = messageManager;
        this.log = log;
    }
    
    @Override
    public Address setUp(int port) {
        final Address myAddress = new Address();
        try {
            if (port == 0) {
                this.datagramSocket = new DatagramSocket();
                port = this.datagramSocket.getLocalPort();
            }
            else {
                this.datagramSocket = new DatagramSocket(port);
            }
            myAddress.setPort(port);
            try {
                final InetAddress addr = InetAddress.getLocalHost();
                String ip = addr.getHostAddress();
                if (ip.equals("0.0.0.0") || ip.equals("127.0.1.1")) {
                    ip = "127.0.0.1";
                }
                myAddress.setIp(ip);
                this.run = true;
            }
            catch (UnknownHostException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        catch (SocketException socketException) {
            socketException.printStackTrace();
            return null;
        }
        return myAddress;
    }
    
    @Override
    public void startListening() {
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        while (this.run) {
            try {
                final byte[] datagrama = new byte[64000];
                this.receiveMessage = new DatagramPacket(datagrama, datagrama.length);
                this.datagramSocket.receive(this.receiveMessage);
            }
            catch (InterruptedIOException interruptedIOException) {
                this.log.developer(interruptedIOException.getMessage());
                continue;
            }
            catch (IOException ioException) {
                this.log.debug(ioException.getMessage());
            }
            final byte[] messageData = new byte[this.receiveMessage.getLength()];
            System.arraycopy(this.receiveMessage.getData(), 0, messageData, 0, messageData.length);
            this.messageReceiver.receive(new Address(this.receiveMessage.getAddress().toString().substring(1), this.receiveMessage.getPort()), messageData);
        }
        this.datagramSocket.close();
    }
    
    @Override
    public boolean send(final Address addr, final byte[] data) {
        try {
            final DatagramPacket datagramPacket = new DatagramPacket(data, data.length, InetAddress.getByName(addr.getIp()), addr.getPort());
            this.datagramSocket.send(datagramPacket);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    public void stopListening() {
        this.run = false;
    }
}
