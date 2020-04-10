// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.communications.MessageMetadata;
import cFramework.communications.messages.base.Message;

public class DataMessage extends Message
{
    public DataMessage(final byte[] data) {
        super(data);
    }
    
    public DataMessage(final long senderID, final long receiverID, final MessageMetadata metadata, final byte[] msg) {
        this.type = 18;
        this.msg = BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(this.type), BinaryHelper.longToByte(senderID), BinaryHelper.longToByte(receiverID), BinaryHelper.MessageMetadataToByte(metadata), msg);
    }
    
    public long getSenderID() {
        return BinaryHelper.byteToLong(this.msg, 2);
    }
    
    public long getReceiverID() {
        return BinaryHelper.byteToLong(this.msg, 10);
    }
    
    public MessageMetadata getMetaData() {
        return BinaryHelper.byteToMessageMetaData(this.msg, 18);
    }
    
    public byte[] getData() {
        return BinaryHelper.subByteArray(this.msg, 22, this.msg.length - 22);
    }
}
