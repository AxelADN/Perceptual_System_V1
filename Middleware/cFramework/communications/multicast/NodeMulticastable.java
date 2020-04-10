// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.multicast;

import cFramework.communications.fiels.Address;

public interface NodeMulticastable
{
    int getName();
    
    void sendMyInfoToNeighbor(final Address p0);
    
    void singInMulticast(final Address p0, final int p1);
}
