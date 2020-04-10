// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.process;

public class ProcessAndType
{
    private Process activity;
    private long id;
    private String className;
    private ProcessConfiguration pc;
    
    public ProcessAndType(final String className, final ProcessConfiguration pc) {
        this.className = className;
        this.pc = pc;
    }
    
    public int getLenguage() {
        return this.pc.getLenguage();
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public ProcessConfiguration getActivityConfiguration() {
        return this.pc;
    }
    
    public void setActivity(final Process a) {
        this.activity = a;
        this.id = a.getID();
    }
    
    public Process getActivity() {
        return this.activity;
    }
    
    public void setID(final long a) {
        this.id = a;
    }
    
    public long getID() {
        return this.id;
    }
}
