// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes;

public class NodeConf
{
    protected final int MASK_DEBUG = 3;
    public static final int DEBUG_FALSE = 3;
    public static final int DEBUG_TRUE = 2;
    public static final int DEBUG_DEVELOPER = 1;
    protected final int MASK_PROTOCOL = 12;
    public static final int PROTOCOL_TCP = 4;
    public static final int PROTOCOL_UDP = 8;
    protected final int MASK_LOCAL = 16;
    protected final int MASK_ENTITY = -16777216;
    protected int val;
    
    public NodeConf() {
        this(0);
    }
    
    public NodeConf(final int val) {
        this.val = 0;
        this.val = val;
    }
    
    public NodeConf(final int val, final byte entityID) {
        this.val = 0;
        this.val = val;
        this.setEntityID(entityID);
    }
    
    public void setDebug(final Boolean debug) {
        if (debug == null) {
            this.val = ((this.val & 0xFFFFFFFC) | 0x1);
        }
        else if (debug) {
            this.val = ((this.val & 0xFFFFFFFC) | 0x2);
        }
        else {
            this.val = ((this.val & 0xFFFFFFFC) | 0x3);
        }
    }
    
    public Boolean isDebug() {
        if ((this.val & 0x3) == 0x1) {
            return null;
        }
        if ((this.val & 0x3) == 0x2) {
            return true;
        }
        return false;
    }
    
    public boolean isUDP() {
        return (this.val & 0xC) == 0x8;
    }
    
    public void setUDP() {
        this.val = ((this.val & 0xFFFFFFF3) | 0x8);
    }
    
    public boolean isTCP() {
        return (this.val & 0xC) == 0x0 || (this.val & 0xC) == 0x4;
    }
    
    public void setTCP() {
        this.val = ((this.val & 0xFFFFFFF3) | 0x4);
    }
    
    public byte getEntityID() {
        return (byte)((this.val & 0xFF000000) >> 24);
    }
    
    public void setEntityID(final byte id) {
        this.val = ((this.val & 0xFFFFFF) | id << 24);
    }
    
    public void setLocal(final boolean local) {
        if (local) {
            this.val &= 0xFFFFFFEF;
        }
        else {
            this.val = ((this.val & 0xFFFFFFEF) | 0x10);
        }
    }
    
    public boolean isLocal() {
        return (this.val & 0x10) == 0x0;
    }
    
    public int toInt() {
        return this.val;
    }
    
    protected void setDebug(final int v) {
        if ((v & 0x3) == 0x1) {
            this.val = ((this.val & 0xFFFFFFFC) | 0x1);
        }
        else if ((v & 0x3) == 0x2) {
            this.val = ((this.val & 0xFFFFFFFC) | 0x2);
        }
        else if ((v & 0x3) == 0x3) {
            this.val = ((this.val & 0xFFFFFFFC) | 0x3);
        }
    }
    
    protected void setProtocol(final int v) {
        if ((v & 0xC) == 0x4) {
            this.val = ((this.val & 0xFFFFFFF3) | 0x4);
        }
        else if ((v & 0xC) == 0x8) {
            this.val = ((this.val & 0xFFFFFFF3) | 0x8);
        }
    }
    
    public void combine(final int v) {
        this.setDebug(v);
        this.setProtocol(v);
    }
}
