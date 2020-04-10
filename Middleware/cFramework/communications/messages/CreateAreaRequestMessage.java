// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.util.BinaryHelper;
import cFramework.nodes.NodeConf;
import cFramework.communications.messages.base.Message;

public class CreateAreaRequestMessage extends Message
{
    public CreateAreaRequestMessage(final byte[] data) {
        super(data);
    }
    
    public CreateAreaRequestMessage(final long myNodeID, final String myIP, final int myPort, final int idToCreate, final NodeConf nodeConf) {
        this.type = 21;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), new NodeAddress(myNodeID, myIP, myPort).toByteArray(), BinaryHelper.intToByte(idToCreate), BinaryHelper.intToByte(nodeConf.toInt()));
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
    
    public long getIDNodetoCreate() {
        return BinaryHelper.byteToLong(this.msg, 16);
    }
    
    public NodeConf getNodeConfiguration() {
        return new NodeConf(BinaryHelper.byteToInt(this.msg, 24));
    }
}
