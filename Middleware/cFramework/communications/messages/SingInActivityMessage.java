// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.communications.messages.base.Message;

public class SingInActivityMessage extends Message
{
    public SingInActivityMessage(final byte[] data) {
        super(data);
    }
    
    public SingInActivityMessage(final long nodeID) {
        this.type = 2407;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), BinaryHelper.longToByte(nodeID));
    }
    
    public long getNodeID() {
        return BinaryHelper.byteToLong(this.msg, 2);
    }
}
