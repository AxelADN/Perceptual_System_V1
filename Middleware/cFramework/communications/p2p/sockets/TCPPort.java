// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p.sockets;

import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import cFramework.log.NodeLog;
import cFramework.communications.fiels.Address;
import java.net.ServerSocket;
import cFramework.communications.BinaryArrayNotificable;

public class TCPPort implements Port
{
    private BinaryArrayNotificable messageReceiver;
    private ServerSocket serverSocket;
    private Address myAddress;
    private boolean run;
    private NodeLog log;
    
    public TCPPort(final BinaryArrayNotificable messageManager, final NodeLog log) {
        this.messageReceiver = messageManager;
        this.log = log;
    }
    
    @Override
    public Address setUp(final int port) {
        this.myAddress = new Address();
        try {
            this.serverSocket = new ServerSocket(port);
            this.myAddress.setPort(this.serverSocket.getLocalPort());
            String ip = "127.0.0.1";
            final Enumeration en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                final NetworkInterface ni = (NetworkInterface)en.nextElement();
                final Enumeration ee = ni.getInetAddresses();
                while (ee.hasMoreElements()) {
                    final InetAddress ia = (InetAddress) ee.nextElement();
                    if (!ia.getHostAddress().equals("127.0.0.1") && !ia.getHostAddress().equals("127.0.1.1") && !ia.getHostAddress().equals("0.0.0.0")) {
                        ip = ia.getHostAddress();
                    }
                }
            }
            this.myAddress.setIp(ip);
            this.run = true;
        }
        catch (IOException socketException) {
            this.log.debug(socketException.toString());
            return null;
        }
        return this.myAddress;
    }
    
    @Override
    public void startListening() {
        new Thread(this).start();
    }
    
    @Override
    public void run() {
        while (this.run) {
            try {
                final Socket connectionSocket = this.serverSocket.accept();
                new TCPSocketListener(connectionSocket, this.messageReceiver).start();
            }
            catch (SocketException e) {
                if (!this.run && e.getMessage().toLowerCase().equals("socket closed")) {
                    continue;
                }
                e.printStackTrace();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        try {
            if (!this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    @Override
    public void stopListening() {
        this.run = false;
        try {
            this.serverSocket.close();
            this.log.developer("Socket Sussefully Closed");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean send(final Address address, final byte[] message) {
        return new TCPSocketSender(address, message, this.myAddress.getPort(), this.log).send();
    }
}
