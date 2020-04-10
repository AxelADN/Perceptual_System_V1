// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.process;

import java.lang.reflect.InvocationTargetException;
import cFramework.communications.MessageMetadata;
import cFramework.log.NodeLog;

public class ProcessMessageManager
{
    Class<? extends Process> myClass;
    ProcessConfiguration nc;
    private ProcessWrapper pc;
    private Process p;
    private NodeLog log;
    
    public ProcessMessageManager(final Class<? extends Process> myClass, final ProcessWrapper pc, final ProcessConfiguration nc, final NodeLog log) {
        this.myClass = myClass;
        this.nc = nc;
        this.pc = pc;
        this.log = log;
        if (nc.getType() == 64) {
            try {
                (this.p = (Process)myClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0])).setLog(log);
                this.p.setCore(pc);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void receive(final long nodeID, final MessageMetadata m, final byte[] data) {
        if (this.nc.getType() == 32) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        ProcessMessageManager.this.p = (Process)ProcessMessageManager.this.myClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                        ProcessMessageManager.this.p.setLog(ProcessMessageManager.this.log);
                        ProcessMessageManager.this.p.setCore(ProcessMessageManager.this.pc);
                        ProcessMessageManager.this.p.receive(nodeID, m, data);
                    }
                    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex2) {
                        final Exception ex = null;
                        final Exception e = ex;
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        else if (this.nc.getType() == 64) {
            this.p.receive(nodeID, m, data);
        }
    }
}
