// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.util.BinaryHelper;
import cFramework.communications.messages.base.Message;

public class SingInAreaNotificationMessage extends Message
{
    public SingInAreaNotificationMessage(final byte[] data) {
        super(data);
    }
    
    public SingInAreaNotificationMessage(final long nodeID, final String ip, final int port) {
        this.type = 2405;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), new NodeAddress(nodeID, ip, port).toByteArray());
    }
    
    public SingInAreaNotificationMessage(final NodeAddress address) {
        this.type = 2405;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), address.toByteArray());
    }
    
    public long getNodeID() {
        return BinaryHelper.byteToLong(this.msg, 2);
    }
    
    public NodeAddress getAddress() {
        return new NodeAddress(this.getNodeID(), this.getIP(), this.getPort());
    }
    
    public String getIP() {
        return BinaryHelper.byteToIP(this.msg, 10);
    }
    
    public int getPort() {
        return BinaryHelper.byteToUnsignedShort(this.msg, 14);
    }
}
