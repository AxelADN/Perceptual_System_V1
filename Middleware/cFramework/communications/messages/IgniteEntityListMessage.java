// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.nodes.NodeConf;
import cFramework.communications.messages.base.Message;

public class IgniteEntityListMessage extends Message
{
    public IgniteEntityListMessage(final byte[] data) {
        super(data);
    }
    
    public IgniteEntityListMessage(final NodeConf nodeC, final String data) {
        this.type = 30;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), BinaryHelper.intToByte(nodeC.toInt()), data.getBytes());
    }
    
    public NodeConf getNodeConfiguration() {
        return new NodeConf(BinaryHelper.byteToInt(this.msg, 2));
    }
    
    public String getList() {
        return new String(BinaryHelper.subByteArray(this.msg, 6, this.msg.length - 6));
    }
}
