// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.process;

import cFramework.communications.Protocol;
import cFramework.communications.MessageMetadata;
import cFramework.nodes.NodeConf;
import cFramework.communications.NodeAddress;
import cFramework.nodes.microglia.Microglia;
import cFramework.log.NodeLog;
import cFramework.communications.p2p.ActivityProtocols;
import cFramework.nodes.Node;

public class ProcessWrapper extends Node
{
    ActivityProtocols protocols;
    ProcessMessageManager manager;
    Class<? extends Process> activity;
    NodeLog log;
    Microglia microglia;
    
    public ProcessWrapper(final Process p, final NodeAddress father, final ProcessConfiguration nc) {
        this.microglia = new Microglia();
        this.activity = p.getClass();
        p.setLog(this.log = new NodeLog(p.getID(), p.getNamer(), nc.isDebug()));
        this.log.developer(Long.toString(p.getID()));
        this.protocols = new ActivityProtocols(p.getID(), father, this, nc, this.log);
        this.manager = new ProcessMessageManager(this.activity, this, nc, this.log);
    }
    
    public boolean send(final long nodeID, final MessageMetadata m, final byte[] data) {
        this.microglia.send();
        return this.protocols.sendData(nodeID, m, data);
    }
    
    public boolean send(final long nodeID, final byte[] data) {
        this.microglia.send();
        return this.protocols.sendData(nodeID, new MessageMetadata(0), data);
    }
    
    @Override
    public void receive(final long nodeID, final MessageMetadata m, final byte[] data) {
        this.microglia.receive(data);
        this.manager.receive(nodeID, m, data);
    }
    
    @Override
    public Protocol getProtocol() {
        return this.protocols;
    }
    
    public float getWellness() {
        return this.microglia.getWellness();
    }
    
    public void setWellness(final float w) {
        this.microglia.setWellness(w);
    }
}
