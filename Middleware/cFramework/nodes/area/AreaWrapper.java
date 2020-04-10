// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.area;

import cFramework.communications.MessageMetadata;
import cFramework.communications.Protocol;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.communications.p2p.AreaProtocols;
import cFramework.communications.p2p.EntityProtocols;
import cFramework.communications.NodeAddress;
import cFramework.nodes.Node;

public class AreaWrapper extends Node
{
    NodeAddress father;
    EntityProtocols entityProtocols;
    AreaProtocols protocols;
    private Area area;
    private ProcessInitializer activities;
    protected NodeConf nc;
    private NodeLog log;
    
    public AreaWrapper(final Area area, final EntityProtocols entityCommunication, final NodeConf nc) {
        this.entityProtocols = entityCommunication;
        this.nc = nc;
        (this.area = area).setLog(this.log = new NodeLog(area.getID(), area.getNamer(), nc.isDebug()));
        area.setCore(this);
        this.log.developer(Long.toString(area.getID()));
    }
    
    public void setUp() {
        this.activities = new ProcessInitializer(this.area.getActivities(), this.log);
        this.protocols = new AreaProtocols(this.area.getID(), this, this.nc, this.log);
    }
    
    public void setupActivities() {
        this.activities.setUp(this.protocols.getNodeAddress(), this.nc);
    }
    
    public void init() {
        System.out.println("YYEEIII");
        if (!this.nc.isLocal()) {
            this.entityProtocols.SendSingInAreaNotification(this.protocols.getNodeAddress());
        }
        this.area.init();
        this.activities.initAll();
    }
    
    @Override
    public Protocol getProtocol() {
        return this.protocols;
    }
    
    public void route(final long sendToID, final long senderID, final MessageMetadata meta, final byte[] data) {
        this.area.route(sendToID, senderID, meta, data);
    }
    
    @Override
    public void receive(final long id, final MessageMetadata m, final byte[] data) {
        System.out.println("How this was called?");
    }
    
    public boolean send(final long nodeID, final MessageMetadata m, final byte[] data) {
        return this.protocols.sendData(nodeID, m, data);
    }
    
    public boolean send(final long nodeID, final long fromID, final MessageMetadata m, final byte[] data) {
        return this.protocols.sendData(nodeID, fromID, m, data);
    }
    
    public void setFather(final NodeAddress father) {
        this.father = father;
    }
    
    public void sendtoEntity(final NodeAddress senderAddress, final byte[] message) {
        this.entityProtocols.receive(senderAddress.getAddress(), message);
    }
    
    public NodeConf getConfiguration() {
        return this.nc;
    }
}
