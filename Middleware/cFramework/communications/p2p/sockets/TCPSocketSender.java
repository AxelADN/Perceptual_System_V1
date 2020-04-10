// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p.sockets;

import java.io.IOException;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.HelloMessage;
import java.io.DataOutputStream;
import java.net.Socket;
import cFramework.log.NodeLog;
import cFramework.communications.fiels.Address;

public class TCPSocketSender extends Thread
{
    Address address;
    byte[] message;
    int myPort;
    NodeLog log;
    
    public TCPSocketSender(final Address address, final byte[] message, final int myPort, final NodeLog log) {
        this.address = address;
        this.message = message;
        this.myPort = myPort;
        this.log = log;
    }
    
    @Override
    public void run() {
        this.send();
    }
    
    public boolean send() {
        try {
            final Socket s = new Socket(this.address.getIp(), this.address.getPort());
            final DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeShort((short)this.myPort);
            out.write(this.message);
            out.close();
            s.close();
        }
        catch (IOException e) {
            if (!(Message.getMessage(this.message) instanceof HelloMessage)) {
                this.log.debug(e.getMessage());
            }
            return false;
        }
        return true;
    }
}
