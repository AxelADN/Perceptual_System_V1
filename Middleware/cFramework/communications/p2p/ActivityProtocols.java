// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p;

import java.util.List;
import cFramework.util.IDHelper;
import cFramework.communications.MessageMetadata;
import cFramework.communications.messages.SearchNodeRequestMessage;
import java.util.ArrayList;
import cFramework.communications.messages.FindNodeMessage;
import cFramework.communications.messages.SingInActivityMessage;
import cFramework.communications.messages.DataMessage;
import cFramework.communications.messages.base.Message;
import cFramework.communications.fiels.Address;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import java.net.BindException;
import cFramework.log.NodeLog;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.nodes.NodeConf;
import cFramework.communications.NodeAddress;
import cFramework.nodes.process.ProcessWrapper;
import cFramework.communications.Protocol;

public class ActivityProtocols implements Protocol
{
    ProcessWrapper proccessCore;
    long myNodeID;
    NodeAddress father;
    NodeConf nc;
    P2PCommunications myCommunications;
    NodeRouteTable routeTable;
    NodeRouteTable helpers;
    NodeLog log;
    
    public ActivityProtocols(final long myNodeID, final NodeAddress father, final ProcessWrapper process, final NodeConf nc, final NodeLog log) {
        this.myNodeID = myNodeID;
        this.father = father;
        this.proccessCore = process;
        this.nc = nc;
        this.log = log;
        try {
            this.myCommunications = new P2PCommunications(this, nc, log);
        }
        catch (BindException e) {
            System.out.println("Port already in use");
            e.printStackTrace();
        }
        this.routeTable = SingletonNodeRouteTable.getInstance();
        this.helpers = new NodeRouteTable();
    }
    
    @Override
    public void receive(final Address address, final byte[] message) {
        final Message m = Message.getMessage(message);
        final int OPCode = m.getOperationCode();
        if (OPCode == 18) {
            this.log.receive_debug(((DataMessage)m).getSenderID(), "");
            this.data(address, (DataMessage)m);
        }
        else if (OPCode == 2407) {
            this.singInActivity(((SingInActivityMessage)m).getNodeID(), address);
        }
        else if (OPCode == 609) {
            final FindNodeMessage fnm = (FindNodeMessage)m;
            this.findNode(fnm.getNodes());
        }
    }
    
    public void singInActivity(final long idNode, final Address address) {
        this.helpers.set(new NodeAddress(idNode, address));
    }
    
    public void findNode(final ArrayList<NodeAddress> nodes) {
        if (nodes.size() == 0) {
            return;
        }
        this.routeTable.set(nodes);
    }
    
    private void data(final Address address, final DataMessage msg) {
        this.proccessCore.receive(msg.getSenderID(), msg.getMetaData(), msg.getData());
    }
    
    private void searchNodeRequest(final long node) {
        this.myCommunications.send(this.father, new SearchNodeRequestMessage(node).toByteArray());
    }
    
    public boolean sendData(final long sendToID, final MessageMetadata meta, final byte[] data) {
        this.log.send_debug(sendToID, "");
        final long senderID = this.myNodeID;
        List<NodeAddress> node;
        if (IDHelper.getAreaID(sendToID) != this.father.getName()) {
            node = this.routeTable.get(IDHelper.getAreaID(sendToID));
        }
        else {
            node = this.routeTable.get(sendToID);
        }
        if (node == null) {
            this.log.debug("NOT FOUND, Looking for:", sendToID);
            this.searchNodeRequest(sendToID);
            return false;
        }
        boolean sended = true;
        for (int i = 0; i < node.size(); ++i) {
            sended &= this.myCommunications.send(node.get(i), new DataMessage(senderID, sendToID, meta, data).toByteArray());
        }
        return sended;
    }
    
    @Override
    public P2PCommunications getCommunications() {
        return this.myCommunications;
    }
    
    @Override
    public NodeAddress getNodeAddress() {
        return new NodeAddress(this.myNodeID, this.myCommunications.getAddress());
    }
}
