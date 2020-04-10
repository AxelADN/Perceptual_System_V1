// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p.sockets;

import java.util.Iterator;
import java.io.IOException;
import cFramework.communications.fiels.Address;
import java.util.ArrayList;
import java.io.DataInputStream;
import cFramework.communications.BinaryArrayNotificable;
import java.net.Socket;

public class TCPSocketListener extends Thread
{
    Socket socket;
    BinaryArrayNotificable listener;
    
    public TCPSocketListener(final Socket sock, final BinaryArrayNotificable listener) {
        this.socket = sock;
        this.listener = listener;
    }
    
    @Override
    public void run() {
        try {
            final DataInputStream in = new DataInputStream(this.socket.getInputStream());
            final int incomingPort = in.readUnsignedShort();
            final int fragmentSize = 32000;
            final byte[] messageFragment = new byte[fragmentSize];
            int readed = 0;
            int messageLengt = 0;
            final ArrayList<byte[]> messContent = new ArrayList<byte[]>();
            while ((readed = in.read(messageFragment)) > 0) {
                final byte[] temp = new byte[readed];
                System.arraycopy(messageFragment, 0, temp, 0, readed);
                messageLengt += readed;
                messContent.add(temp);
            }
            int start = 0;
            final byte[] message = new byte[messageLengt];
            for (final byte[] fragment : messContent) {
                System.arraycopy(fragment, 0, message, start, fragment.length);
                start += fragment.length;
            }
            final Address addr = new Address(this.socket.getInetAddress().toString().substring(1), incomingPort);
            in.close();
            this.socket.close();
            this.listener.receive(addr, message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
