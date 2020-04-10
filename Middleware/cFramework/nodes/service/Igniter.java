// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.service;

import cFramework.util.OSHelper;
import cFramework.nodes.entity.Entity;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import java.util.List;
import cFramework.communications.p2p.IgniterProtocols;

public class Igniter
{
    private IgniterProtocols protocols;
    protected List<String> areas;
    protected NodeConf configuration;
    protected NodeLog log;
    
    public Igniter() {
        this.areas = new ArrayList<String>();
        this.configuration = new NodeConf();
    }
    
    protected void setAreas(final String[] areas) {
        this.areas.addAll(Arrays.asList(areas));
    }
    
    public void addArea(final String areaNames, final Object... parameters) {
        final StringBuilder chain = new StringBuilder();
        chain.append(areaNames);
        for (int i = 0; i < parameters.length; ++i) {
            chain.append(":" + parameters[i].getClass().getName() + ":" + parameters[i]);
        }
        this.areas.add(chain.toString());
    }
    
    public void addBlackBoxArea(final String areaNames) {
        final StringBuilder chain = new StringBuilder();
        chain.append(areaNames);
        chain.append(":BLACKBOX:TRUE");
        this.areas.add(chain.toString());
    }
    
    private void runLocal(final String[] areasNames, final NodeConf nc) {
        new Entity(areasNames, nc);
    }
    
    private void initService() {
        String command = "";
        command = "java -cp " + OSHelper.preparePath(System.getProperty("java.class.path")) + " ";
        command += Service.class.getName();
        this.log.developer("Igniting Service");
        this.log.developer("Running command: " + command);
        OSHelper.exec(command);
    }
    
    protected void run() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        this.log = new NodeLog("Igniter", this.configuration.isDebug());
        if (this.areas.size() == 0) {
            this.log.message("No areas selected");
            return;
        }
        final String[] areasArray = new String[this.areas.size()];
        for (int i = 0; i < this.areas.size(); ++i) {
            areasArray[i] = this.areas.get(i);
        }
        if (this.configuration.isLocal()) {
            this.runLocal(areasArray, this.configuration);
        }
        else {
            this.protocols = new IgniterProtocols(this.log);
            try {
                if (!this.protocols.isServiceUP()) {
                    this.log.developer("Service is down");
                    this.initService();
                    Thread.sleep(500L);
                }
                this.log.developer("Sending Areas List to service");
                this.protocols.sendList(areasArray, this.configuration);
                this.log.developer("Stopping Igniter");
                this.protocols.stop();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
