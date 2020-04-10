// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.process;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import cFramework.communications.MessageMetadata;
import cFramework.log.NodeLog;

public abstract class Process
{
    private ProcessWrapper core;
    protected long ID;
    protected Class<?> namer;
    protected NodeLog log;
    private float wellnessUmbral;
    protected MessageMetadata currentMetadata;
    
    public Process() {
        this.wellnessUmbral = 1.0f;
        this.currentMetadata = null;
    }
    
    public void init() {
    }
    
    public long getID() {
        return this.ID;
    }
    
    public void setCore(final ProcessWrapper core) {
        this.core = core;
    }
    
    protected boolean send(final long nodeID, final byte[] data) {
        if (this.currentMetadata != null) {
            return this.core.send(nodeID, this.currentMetadata, data);
        }
        return this.core.send(nodeID, new MessageMetadata(0), data);
    }
    
    public abstract void receive(final long p0, final byte[] p1);
    
    public void receive(final long nodeID, final MessageMetadata m, final byte[] data) {
        this.currentMetadata = m;
        if (this.getWellness() >= this.wellnessUmbral) {
            this.receive(nodeID, data);
        }
        else {
            try {
                final Method recovery = this.getClass().getMethod("recovery_receive", Integer.TYPE, byte[].class);
                recovery.invoke(this, nodeID, data);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                final Exception ex = null;
                final Exception e = ex;
                this.receive(nodeID, data);
            }
        }
    }
    
    public void recovery_receive(final long nodeID, final MessageMetadata m, final byte[] data) {
        this.currentMetadata = m;
        this.receive(nodeID, data);
    }
    
    public Class<?> getNamer() {
        return this.namer;
    }
    
    public void setLog(final NodeLog log) {
        this.log = log;
    }
    
    public float getWellness() {
        return this.core.getWellness();
    }
    
    public void setWellness(final float w) {
        this.core.setWellness(w);
    }
    
    public void setWellnessUmbral(final float w) {
        this.wellnessUmbral = w;
    }
}
