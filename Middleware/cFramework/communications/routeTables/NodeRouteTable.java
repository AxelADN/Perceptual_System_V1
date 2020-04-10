// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.routeTables;

import cFramework.communications.LocalJVMNodeAddress;
import cFramework.nodes.Node;
import cFramework.communications.NodeAddress;
import java.util.ArrayList;
import java.util.Hashtable;

public class NodeRouteTable
{
    protected Hashtable<Long, ArrayList<NodeAddress>> routeTable;
    
    public NodeRouteTable() {
        this.routeTable = new Hashtable<Long, ArrayList<NodeAddress>>();
    }
    
    public void set(final long name, final String host, final int port, final Node objectReference) {
        this.set(new LocalJVMNodeAddress(name, host, port, objectReference));
    }
    
    public void set(final ArrayList<NodeAddress> nodes) {
        for (int i = 0; i < nodes.size(); ++i) {
            this.set(nodes.get(i));
        }
    }
    
    public void set(final NodeAddress node) {
        ArrayList<NodeAddress> addresses = this.routeTable.get(node.getName());
        if (addresses == null) {
            addresses = new ArrayList<NodeAddress>();
            this.routeTable.put(node.getName(), addresses);
        }
        addresses.add(node);
    }
    
    public ArrayList<NodeAddress> get(final long id) {
        return this.routeTable.get(id);
    }
    
    public boolean exist(final long id) {
        return this.routeTable.containsKey(id);
    }
}
