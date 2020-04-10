// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.microglia;

public class Microglia
{
    private float wellness;
    
    public Microglia() {
        this.wellness = 1.0f;
    }
    
    public void setWellness(final float w) {
        this.wellness = w;
    }
    
    public float getWellness() {
        return this.wellness;
    }
    
    public void limit() {
        if (this.wellness > 1.0f) {
            this.wellness = 1.0f;
        }
    }
    
    public float receive(final byte[] data) {
        if (data == null) {
            this.wellness -= (float)0.05;
        }
        if (this.wellness != 1.0f) {
            this.wellness += (float)0.05;
            this.limit();
        }
        return this.wellness;
    }
    
    public float send() {
        if (this.wellness != 1.0f) {
            this.wellness += 0.5;
            this.limit();
        }
        return this.wellness;
    }
}
