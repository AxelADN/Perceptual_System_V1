// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.communications.NodeAddress;
import java.util.ArrayList;
import cFramework.communications.messages.base.Message;

public class FindNodeMessage extends Message
{
    public FindNodeMessage(final byte[] data) {
        super(data);
    }
    
    public FindNodeMessage(final ArrayList<NodeAddress> a) {
        this.type = 609;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), BinaryHelper.intToByte(a.size()));
        for (int i = 0; i < a.size(); ++i) {
            this.msg = BinaryHelper.mergeByteArrays(this.msg, a.get(i).toByteArray());
        }
    }
    
    public ArrayList<NodeAddress> getNodes() {
        final ArrayList<NodeAddress> r = new ArrayList<NodeAddress>();
        for (int size = BinaryHelper.byteToInt(this.msg, 2), i = 0; i < size; ++i) {
            r.add(new NodeAddress(BinaryHelper.byteToLong(this.msg, 6 + 14 * i), BinaryHelper.byteToIP(this.msg, 14 + 14 * i), BinaryHelper.byteToUnsignedShort(this.msg, 18 + 14 * i)));
        }
        return r;
    }
}
