// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p.sockets;

import cFramework.communications.fiels.Address;

public interface Port extends Runnable
{
    Address setUp(final int p0);
    
    void startListening();
    
    void stopListening();
    
    boolean send(final Address p0, final byte[] p1);
}
