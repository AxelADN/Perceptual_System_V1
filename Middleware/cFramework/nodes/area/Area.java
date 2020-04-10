// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.area;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import cFramework.nodes.process.ProcessConfiguration;
import cFramework.nodes.process.Process;
import java.util.Iterator;
import cFramework.communications.spikes.SpikeRouter;
import java.util.List;
import java.util.HashMap;
import cFramework.communications.MessageMetadata;
import cFramework.log.NodeLog;
import cFramework.nodes.process.ProcessAndType;
import java.util.ArrayList;

public abstract class Area
{
    private AreaWrapper core;
    private ArrayList<ProcessAndType> process;
    protected long ID;
    protected Class<?> namer;
    protected NodeLog log;
    MessageMetadata currentMetadata;
    protected HashMap<Long, HashMap<Integer, HashMap<Long, byte[]>>> queueSpikes;
    protected HashMap<Long, List<SpikeRouter>> routes;
    private Class<?>[] parameterClasses;
    private Object[] parameterValues;
    
    public Area() {
        this.process = new ArrayList<ProcessAndType>();
        this.namer = null;
        this.currentMetadata = null;
        this.routes = new HashMap<Long, List<SpikeRouter>>();
    }
    
    protected boolean send(final long toID, final byte[] data) {
        if (this.currentMetadata != null) {
            return this.send(toID, this.currentMetadata, data);
        }
        return this.send(toID, new MessageMetadata(0), data);
    }
    
    protected boolean send(final long toID, final MessageMetadata m, final byte[] data) {
        return this.core.send(toID, m, data);
    }
    
    private boolean send(final long toID, final long fromID, final MessageMetadata m, final byte[] data) {
        return this.core.send(toID, fromID, m, data);
    }
    
    public abstract void receive(final long p0, final byte[] p1);
    
    public void process(final long nodeID, final byte[] data) {
        this.receive(nodeID, data);
    }
    
    public void route(final long sendTo, final long fromID, final MessageMetadata m, final byte[] data) {
        this.currentMetadata = m;
        final List<SpikeRouter> routesForThisSender = this.routes.get(fromID);
        if (routesForThisSender == null) {
            if (sendTo == this.ID) {
                this.receive(fromID, data);
            }
            else {
                this.send(sendTo, fromID, m, data);
            }
        }
        else {
            for (final SpikeRouter router : routesForThisSender) {
                if (router.isTargetToID(sendTo)) {
                    if (router.merger == null) {
                        this.send(sendTo, fromID, m, data);
                    }
                    else {
                        HashMap<Long, byte[]> spikeSet = this.queueSpikes.get(router.ROUTERID).get(m.time);
                        if (spikeSet == null) {
                            spikeSet = new HashMap<Long, byte[]>();
                            this.queueSpikes.get(router.ROUTERID).put(m.time, spikeSet);
                        }
                        spikeSet.put(fromID, data);
                        if (spikeSet.size() != router.from.length) {
                            continue;
                        }
                        final byte[] spike = router.merger.merge(spikeSet);
                        for (final long i : router.to) {
                            if (i != this.ID) {
                                this.send(i, this.ID, m, spike);
                            }
                            else {
                                this.receive(fromID, spike);
                            }
                        }
                        this.queueSpikes.get(router.ROUTERID).remove(m.time);
                    }
                }
                else {
                    if (sendTo != this.ID || router.merger != null) {
                        continue;
                    }
                    for (final long j : router.to) {
                        if (j != this.ID) {
                            this.send(j, this.ID, m, data);
                        }
                        else {
                            System.out.println("WHY WOULD YOU SEND IT TO YOURSELF, this will create an infinite Buckle");
                        }
                    }
                }
            }
        }
    }
    
    public void AddRoute(final SpikeRouter r) {
        if (this.queueSpikes == null) {
            this.queueSpikes = new HashMap<Long, HashMap<Integer, HashMap<Long, byte[]>>>();
        }
        r.ROUTERID = this.queueSpikes.size();
        this.queueSpikes.put(r.ROUTERID, new HashMap<Integer, HashMap<Long, byte[]>>());
        for (final long i : r.from) {
            List<SpikeRouter> routesForThisSender = this.routes.get(i);
            if (routesForThisSender == null) {
                routesForThisSender = new ArrayList<SpikeRouter>();
                this.routes.put(i, routesForThisSender);
            }
            routesForThisSender.add(r);
        }
    }
    
    public long getID() {
        return this.ID;
    }
    
    public void setCore(final AreaWrapper core) {
        this.core = core;
    }
    
    public void setLog(final NodeLog log) {
        this.log = log;
    }
    
    public ArrayList<ProcessAndType> getActivities() {
        return this.process;
    }
    
    protected void addProcess(final Class<? extends Process> className) {
        this.addProcess(className, 0);
    }
    
    protected void addProcess(final Class<? extends Process> className, final int configurationValue) {
        this.addProcess(className, new ProcessConfiguration(configurationValue));
    }
    
    protected void addProcess(final Class<? extends Process> className, final ProcessConfiguration nc) {
        this.addProcess(className.getName(), nc);
    }
    
    protected void addProcess(final String className) {
        this.addProcess(className, 0);
    }
    
    protected void addProcess(final String className, final int configurationValue) {
        this.addProcess(className, new ProcessConfiguration(configurationValue));
    }
    
    protected void addProcess(final String className, final ProcessConfiguration nc) {
        this.process.add(new ProcessAndType(className, nc));
    }
    
    public Class<?> getNamer() {
        return this.namer;
    }
    
    public void setInitParameters(final Class<?>[] classes, final Object[] objects) {
        this.parameterClasses = classes;
        this.parameterValues = objects;
    }
    
    public void init() {
        try {
            final Method m = this.getClass().getMethod("init", this.parameterClasses);
            if (!m.toString().contains(Area.class.getName())) {
                m.invoke(this, this.parameterValues);
            }
        }
        catch (NoSuchMethodException | SecurityException ex3) {
            final Exception ex = null;
            final Exception e = ex;
            System.out.println("Error getting init method");
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex4) {
            final Exception ex2 = null;
            final Exception e = ex2;
            System.out.println("Error invoking init method");
            e.printStackTrace();
        }
    }
}
