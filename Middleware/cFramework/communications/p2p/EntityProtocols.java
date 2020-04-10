// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.p2p;

import cFramework.communications.messages.SingInAreaNotificationMessage;
import java.util.ArrayList;
import cFramework.communications.messages.HandShakeMessage;
import cFramework.communications.NodeAddress;
import cFramework.communications.messages.SearchMulticastMessage;
import cFramework.communications.messages.SearchNodeRequestMessage;
import cFramework.communications.messages.FindNodeMessage;
import cFramework.communications.messages.base.Message;
import cFramework.communications.fiels.Address;
import java.net.BindException;
import cFramework.nodes.NodeConf;
import cFramework.log.NodeLog;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.communications.messages.MessageReceiverable;
import cFramework.communications.multicast.Multicast;
import cFramework.communications.Protocol;
import cFramework.communications.BinaryArrayNotificable;

public class EntityProtocols implements BinaryArrayNotificable, Protocol
{
    P2PCommunications myCommunications;
    Multicast multicastConection;
    MessageReceiverable listener;
    NodeRouteTable routeTable;
    NodeLog log;
    NodeConf conf;
    
    public EntityProtocols(final MessageReceiverable listener, final NodeConf nc, final int port, final NodeRouteTable routeTable, final NodeLog log) throws BindException {
        this.listener = listener;
        this.routeTable = routeTable;
        this.log = log;
        this.conf = nc;
        this.myCommunications = new P2PCommunications(this, nc, port + nc.getEntityID(), log);
        if (!nc.isLocal()) {
            (this.multicastConection = new Multicast(this, nc, log)).setUp();
        }
    }
    
    @Override
    public void receive(final Address address, final byte[] message) {
        final Message m = Message.getMessage(message);
        if (m.getOperationCode() == 609) {
            final FindNodeMessage fnm = (FindNodeMessage)m;
            this.findNode(fnm.getNodes());
        }
        else if (m.getOperationCode() != 23) {
            if (m.getOperationCode() == 2402) {
                if (!this.conf.isLocal()) {
                    this.searchNodeRequest(((SearchNodeRequestMessage)m).getNodeID(), address);
                }
            }
            else if (m.getOperationCode() != 2405) {
                if (m.getOperationCode() == 2401) {
                    final SearchMulticastMessage smm = (SearchMulticastMessage)m;
                    this.SearchNodeMulticast(new Address(smm.getIP(), smm.getPort()), smm.getLookedName());
                }
                else if (m.getOperationCode() == 50) {
                    this.myCommunications.send(new NodeAddress(0L, address), new HandShakeMessage().toByteArray());
                }
            }
        }
    }
    
    private void findNode(final ArrayList<NodeAddress> nodes) {
        if (nodes.size() == 0) {
            return;
        }
        this.routeTable.set(nodes);
    }
    
    private void searchNodeRequest(final long idNode, final Address address) {
        this.multicastConection.send(new SearchMulticastMessage(0L, address.getIp(), address.getPort(), idNode).toByteArray());
    }
    
    @Override
    public P2PCommunications getCommunications() {
        return this.myCommunications;
    }
    
    public void SendSingInAreaNotification(final NodeAddress address) {
        this.multicastConection.send(new SingInAreaNotificationMessage(address.getName(), address.getHost(), address.getPort()).toByteArray());
    }
    
    private void SearchNodeMulticast(final Address address, final long lookedNodeID) {
        final ArrayList<NodeAddress> nodes = this.routeTable.get(lookedNodeID);
        if (nodes != null && nodes.size() != 0) {
            this.log.message(nodes.get(0).toString());
            this.myCommunications.send(new NodeAddress(0L, address), new FindNodeMessage(nodes).toByteArray());
        }
    }
    
    @Override
    public NodeAddress getNodeAddress() {
        return new NodeAddress(0L, this.myCommunications.getAddress());
    }
}
