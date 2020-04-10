// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p;

import cFramework.nodes.Node;
import cFramework.communications.LocalJVMNodeAddress;
import java.util.List;
import cFramework.util.IDHelper;
import cFramework.communications.MessageMetadata;
import java.util.ArrayList;
import cFramework.communications.NodeAddress;
import cFramework.communications.messages.SearchNodeRequestMessage;
import cFramework.communications.messages.FindNodeMessage;
import cFramework.communications.messages.SingInActivityMessage;
import cFramework.communications.messages.DataMessage;
import cFramework.communications.messages.base.Message;
import cFramework.communications.fiels.Address;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import java.net.BindException;
import cFramework.log.NodeLog;
import cFramework.nodes.area.AreaWrapper;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.nodes.NodeConf;
import cFramework.communications.Protocol;

public class AreaProtocols implements Protocol
{
    long myNodeID;
    NodeConf nc;
    P2PCommunications myCommunications;
    NodeRouteTable routeTable;
    AreaWrapper areaWrapper;
    NodeLog log;
    
    public AreaProtocols(final long areaID, final AreaWrapper wrapper, final NodeConf nc, final NodeLog log) {
        this.myNodeID = areaID;
        this.areaWrapper = wrapper;
        this.log = log;
        try {
            this.myCommunications = new P2PCommunications(this, nc, this.log);
        }
        catch (BindException e) {
            log.debug("Port already in use");
            e.printStackTrace();
        }
        this.routeTable = SingletonNodeRouteTable.getInstance();
    }
    
    @Override
    public void receive(final Address address, final byte[] message) {
        final Message m = Message.getMessage(message);
        if (m.getOperationCode() == 18) {
            final DataMessage data = (DataMessage)m;
            this.routeData(data.getReceiverID(), data.getSenderID(), data.getMetaData(), data.getData());
        }
        else if (m.getOperationCode() == 2407) {
            this.singInActivity(((SingInActivityMessage)m).getNodeID(), address);
        }
        else if (m.getOperationCode() == 609) {
            final FindNodeMessage fnm = (FindNodeMessage)m;
            this.findNode(fnm.getNodes());
        }
        else if (m.getOperationCode() != 23) {
            if (m.getOperationCode() == 2402) {
                this.searchNodeRequest(((SearchNodeRequestMessage)m).getNodeID(), address);
            }
        }
    }
    
    public void singInActivity(final long idNode, final Address address) {
        this.routeTable.set(new NodeAddress(idNode, address));
    }
    
    public void findNode(final ArrayList<NodeAddress> nodes) {
        if (nodes.size() == 0) {
            return;
        }
        this.routeTable.set(nodes);
    }
    
    public void addedToRouteTable(final long idNode, final Address address) {
    }
    
    public void searchNodeRequest(final long idNode, final Address address) {
        final ArrayList<NodeAddress> node = this.routeTable.get(idNode);
        if (node != null && node.size() != 0) {
            this.myCommunications.send(new NodeAddress(0L, address), new FindNodeMessage(node).toByteArray());
        }
        else {
            this.areaWrapper.sendtoEntity(new NodeAddress(0L, address), new SearchNodeRequestMessage(idNode).toByteArray());
        }
    }
    
    public void update() {
    }
    
    public void routeData(final long sendToID, final long senderID, final MessageMetadata meta, final byte[] data) {
        this.areaWrapper.route(sendToID, senderID, meta, data);
    }
    
    public boolean sendData(final long sendToID, final MessageMetadata meta, final byte[] data) {
        return this.sendData(sendToID, 0L, meta, data);
    }
    
    public boolean sendData(final long sendToID, long senderID, final MessageMetadata meta, final byte[] data) {
        if (senderID == 0L) {
            senderID = this.myNodeID;
        }
        List<NodeAddress> node;
        if (IDHelper.isActivitiy(sendToID) && IDHelper.getAreaID(sendToID) != this.myNodeID) {
            node = this.routeTable.get(IDHelper.getAreaID(sendToID));
        }
        else {
            node = this.routeTable.get(sendToID);
        }
        if (node == null) {
            this.log.debug("NOT FOUND, Looking for:", sendToID);
            this.areaWrapper.sendtoEntity(new NodeAddress(this.myNodeID, this.myCommunications.getAddress()), new SearchNodeRequestMessage(sendToID).toByteArray());
            return false;
        }
        boolean sended = true;
        for (int i = 0; i < node.size(); ++i) {
            if (node.get(i).getHost().equals("0.0.0.0")) {
                this.log.message("Blakcbox");
            }
            else {
                this.log.send_debug(sendToID, "");
                sended &= this.myCommunications.send(node.get(i), new DataMessage(senderID, sendToID, meta, data).toByteArray());
            }
        }
        return sended;
    }
    
    @Override
    public P2PCommunications getCommunications() {
        return this.myCommunications;
    }
    
    @Override
    public NodeAddress getNodeAddress() {
        return new LocalJVMNodeAddress(new NodeAddress(this.myNodeID, this.myCommunications.getAddress()), this.areaWrapper);
    }
}
