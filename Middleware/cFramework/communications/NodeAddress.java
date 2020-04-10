// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications;

import cFramework.util.BinaryHelper;
import cFramework.communications.fiels.Address;
import cFramework.communications.fiels.NullValueConstants;

public class NodeAddress implements NullValueConstants
{
    protected long name;
    protected Address address;
    protected int bits;
    
    public NodeAddress() {
        this.name = -1L;
        this.bits = 0;
    }
    
    public NodeAddress(final long name) {
        this.name = -1L;
        this.bits = 0;
        this.name = name;
        this.address = new Address();
        this.bits += ((this.name == -1L) ? 0 : 4);
    }
    
    public NodeAddress(final long name, final String host, final int port) {
        this.name = -1L;
        this.bits = 0;
        this.name = name;
        this.address = new Address(host, port);
        this.bits += ((this.name == -1L) ? 0 : 4);
        this.bits += (this.address.isNullIp() ? 0 : 2);
        this.bits += (this.address.isNullPort() ? 0 : 1);
    }
    
    public NodeAddress(final long name, final Address a) {
        this.name = -1L;
        this.bits = 0;
        this.name = name;
        this.address = a;
        this.bits += ((this.name == -1L) ? 0 : 4);
        this.bits += (this.address.isNullIp() ? 0 : 2);
        this.bits += (this.address.isNullPort() ? 0 : 1);
    }
    
    public long getName() {
        return this.name;
    }
    
    public String getHost() {
        return this.address.getIp();
    }
    
    public int getPort() {
        return this.address.getPort();
    }
    
    public int getBits() {
        return this.bits;
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    public byte[] toByteArray() {
        return BinaryHelper.mergeByteArrays(BinaryHelper.longToByte(this.getName()), this.getAddress().toByteArray());
    }
    
    public static byte[] getNode(final NodeAddress n) {
        return BinaryHelper.mergeByteArrays(BinaryHelper.longToByte(n.getName()), n.getAddress().toByteArray());
    }
    
    @Override
    public String toString() {
        return "" + this.name + this.address;
    }
    
    public static String toString(final String tNodeName, final String tHost, final int tPort) {
        return tNodeName + tHost + "$" + tPort + "$";
    }
    
    public boolean isNull() {
        return this.bits == 0;
    }
    
    public void setName(final long name) {
        this.name = name;
    }
}
