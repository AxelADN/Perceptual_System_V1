// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.communications.messages.base.Message;

public class HandShakeMessage extends Message
{
    public HandShakeMessage() {
        this.type = 49;
        this.msg = BinaryHelper.shortToByte(this.type);
    }
}
