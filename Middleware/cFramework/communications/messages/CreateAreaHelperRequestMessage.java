// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.util.BinaryHelper;
import cFramework.nodes.NodeConf;
import cFramework.communications.messages.base.Message;

public class CreateAreaHelperRequestMessage extends Message
{
    public CreateAreaHelperRequestMessage(final byte[] data) {
        super(data);
    }
    
    public CreateAreaHelperRequestMessage(final long myNodeID, final String myIP, final int myPort, final long idToCreate, final NodeConf nodeConf) {
        this.type = 22;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), new NodeAddress(myNodeID, myIP, myPort).toByteArray(), BinaryHelper.longToByte(idToCreate), BinaryHelper.intToByte(nodeConf.toInt()));
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
