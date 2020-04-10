// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications;

import cFramework.communications.p2p.P2PCommunications;

public interface Protocol extends BinaryArrayNotificable
{
    P2PCommunications getCommunications();
    
    NodeAddress getNodeAddress();
}
