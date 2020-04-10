// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.process;

import cFramework.nodes.NodeConf;

public class ProcessConfiguration extends NodeConf
{
    private final int MASK_TYPE = 224;
    public static final int TYPE_SINGLETON = 64;
    public static final int TYPE_PARALLEL = 32;
    private final int MASK_LENG = 1792;
    public static final int LENG_JAVA = 512;
    public static final int LENG_PYTHON = 256;
    
    public ProcessConfiguration() {
        this(0);
    }
    
    public ProcessConfiguration(final int val) {
        this(val, (byte)0);
    }
    
    public ProcessConfiguration(final int val, final byte entityID) {
        super(val, entityID);
    }
    
    public void setType(final int mode) {
        this.val = ((this.val & 0xFFFFFF1F) | mode);
    }
    
    public int getType() {
        int ret = this.val & 0xE0;
        if (ret == 0) {
            ret = 64;
        }
        return ret;
    }
    
    public void setLenguage(final int leng) {
        this.val = ((this.val & 0xFFFFF8FF) | leng);
    }
    
    public int getLenguage() {
        int ret = this.val & 0x700;
        if (ret == 0) {
            ret = 512;
        }
        return ret;
    }
}
