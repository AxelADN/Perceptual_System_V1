// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications;

import cFramework.nodes.Node;
import cFramework.communications.p2p.P2PCommunications;

public class LocalJVMNodeAddress extends NodeAddress
{
    private P2PCommunications nodeCommunicationsReference;
    
    public LocalJVMNodeAddress(final NodeAddress address, final Node objectReference) {
        this(address.getName(), address.getHost(), address.getPort(), objectReference);
    }
    
    public LocalJVMNodeAddress(final long name, final String host, final int port, final Node objectReference) {
        super(name, host, port);
        this.nodeCommunicationsReference = objectReference.getProtocol().getCommunications();
    }
    
    public P2PCommunications getNodeReference() {
        return this.nodeCommunicationsReference;
    }
}
