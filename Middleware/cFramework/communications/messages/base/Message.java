// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages.base;

import cFramework.communications.messages.UpdateMessage;
import cFramework.communications.messages.SingInActivityMessage;
import cFramework.communications.messages.SearchNodeRequestMessage;
import cFramework.communications.messages.SearchMulticastMessage;
import cFramework.communications.messages.IgniteEntityListMessage;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.messages.HandShakeMessage;
import cFramework.communications.messages.FindNodeMessage;
import cFramework.communications.messages.DataMessage;
import cFramework.communications.messages.CreateAreaRequestMessage;
import cFramework.communications.messages.CreateAreaHelperRequestMessage;
import cFramework.communications.messages.SingInAreaNotificationMessage;
import cFramework.util.BinaryHelper;
import cFramework.communications.fiels.NullValueConstants;

public class Message implements NullValueConstants, OperationCodeConstants
{
    protected short type;
    protected byte[] msg;
    
    public Message() {
    }
    
    public Message(final byte[] msg) {
        this.type = BinaryHelper.byteToShort(msg, 0);
        this.msg = msg;
    }
    
    public short getOperationCode() {
        return this.type;
    }
    
    public byte[] toByteArray() {
        return this.msg;
    }
    
    public static Message getMessage(final byte[] msg) {
        final short type = BinaryHelper.byteToShort(msg, 0);
        if (type == 2405) {
            return new SingInAreaNotificationMessage(msg);
        }
        if (type == 22) {
            return new CreateAreaHelperRequestMessage(msg);
        }
        if (type == 21) {
            return new CreateAreaRequestMessage(msg);
        }
        if (type == 18) {
            return new DataMessage(msg);
        }
        if (type == 609) {
            return new FindNodeMessage(msg);
        }
        if (type == 49) {
            return new HandShakeMessage();
        }
        if (type == 50) {
            return new HelloMessage();
        }
        if (type == 30) {
            return new IgniteEntityListMessage(msg);
        }
        if (type == 609) {
            return new FindNodeMessage(msg);
        }
        if (type == 2401) {
            return new SearchMulticastMessage(msg);
        }
        if (type == 2402) {
            return new SearchNodeRequestMessage(msg);
        }
        if (type == 2407) {
            return new SingInActivityMessage(msg);
        }
        if (type == 2405) {
            return new SingInAreaNotificationMessage(msg);
        }
        if (type == 23) {
            return new UpdateMessage(msg);
        }
        return new Message(msg);
    }
}
