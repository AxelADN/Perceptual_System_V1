// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes;

import cFramework.communications.MessageMetadata;
import cFramework.communications.Protocol;

public abstract class Node
{
    public abstract Protocol getProtocol();
    
    public abstract void receive(final long p0, final MessageMetadata p1, final byte[] p2);
}
