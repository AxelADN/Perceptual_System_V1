// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.communications.messages.base.Message;

public class SearchNodeRequestMessage extends Message
{
    public SearchNodeRequestMessage(final byte[] data) {
        super(data);
    }
    
    public SearchNodeRequestMessage(final long nodeID) {
        this.type = 2402;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), BinaryHelper.longToByte(nodeID));
    }
    
    public long getNodeID() {
        return BinaryHelper.byteToLong(this.msg, 2);
    }
}
