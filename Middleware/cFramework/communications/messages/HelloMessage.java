// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages;

import cFramework.util.BinaryHelper;
import cFramework.communications.messages.base.Message;

public class HelloMessage extends Message
{
    public HelloMessage() {
        this.type = 50;
        this.msg = BinaryHelper.shortToByte(this.type);
    }
}
