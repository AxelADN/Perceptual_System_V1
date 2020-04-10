// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.util.BinaryHelper;
import cFramework.communications.messages.base.Message;

public class UpdateMessage extends Message
{
    public UpdateMessage(final byte[] data) {
        super(data);
    }
    
    public UpdateMessage(final long nodeID, final String ip, final int port) {
        this.type = 23;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), new NodeAddress(nodeID, ip, port).toByteArray());
    }
    
    public long getNodeID() {
        return BinaryHelper.byteToLong(this.msg, 2);
    }
    
    public String getIP() {
        return BinaryHelper.byteToIP(this.msg, 10);
    }
    
    public int getPort() {
        return BinaryHelper.byteToUnsignedShort(this.msg, 14);
    }
}
