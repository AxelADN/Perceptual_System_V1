// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.area;

import java.io.File;
import cFramework.util.OSHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import cFramework.nodes.process.ProcessConfiguration;
import cFramework.nodes.process.Process;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import cFramework.nodes.Node;
import cFramework.communications.LocalJVMNodeAddress;
import cFramework.nodes.process.ProcessWrapper;
import java.util.Iterator;
import cFramework.nodes.NodeConf;
import cFramework.communications.NodeAddress;
import cFramework.log.NodeLog;
import cFramework.nodes.process.ProcessAndType;
import java.util.ArrayList;

public class ProcessInitializer
{
    private ArrayList<ProcessAndType> process;
    private NodeLog log;
    
    public ProcessInitializer(final ArrayList<ProcessAndType> activities, final NodeLog log) {
        this.process = new ArrayList<ProcessAndType>();
        this.log = null;
        this.process = activities;
        this.log = log;
    }
    
    public void setUp(final NodeAddress fatherAddress, final NodeConf nc) {
        for (final ProcessAndType a : this.process) {
            if (a.getLenguage() == 512) {
                this.createJavaActivity(a, fatherAddress, nc);
            }
            else {
                if (a.getLenguage() != 256) {
                    continue;
                }
                this.createPythonActivity(a, fatherAddress, nc);
            }
        }
    }
    
    public void createJavaActivity(final ProcessAndType a, final NodeAddress fatherAddress, final NodeConf nc) {
        this.log.developer("Adding class " + a.getClassName());
        final Process activity = this.getActivityObject(a.getClassName());
        if (activity == null) {
            this.log.developer("Activity " + a.getClassName() + " Does not exist");
            return;
        }
        final ProcessConfiguration pc = a.getActivityConfiguration();
        pc.combine(nc.toInt());
        final ProcessWrapper core = new ProcessWrapper(activity, fatherAddress, pc);
        activity.setCore(core);
        final LocalJVMNodeAddress activityNode = new LocalJVMNodeAddress(core.getProtocol().getNodeAddress(), core);
        SingletonNodeRouteTable.getInstance().set(activityNode);
        a.setActivity(activity);
    }
    
    private Process getActivityObject(final String areaClassName) {
        try {
            final Class<?> nodeClass = Class.forName(areaClassName);
            final Constructor<?> nodeConstr = nodeClass.getConstructor((Class<?>[])new Class[0]);
            return (Process)nodeConstr.newInstance(new Object[0]);
        }
        catch (IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            this.log.error(e.toString());
            return null;
        }
    }
    
    public void createPythonActivity(final ProcessAndType a, final NodeAddress fatherAddress, final NodeConf nc) {
        this.log.developer("Adding Python Proccess " + a.getClassName());
        final String pythonLib = this.getPythonLibrary();
        if (pythonLib == null) {
            System.out.println("Python lib not finded, add it next to your middleware library");
        }
        final ProcessConfiguration pc = new ProcessConfiguration(nc.toInt() | a.getActivityConfiguration().toInt());
        String command = "python ";
        command = command + OSHelper.preparePath(pythonLib + OSHelper.preparePathSegment("/ProcessWrapper.py")) + " ";
        command = command + a.getClassName() + " ";
        command = command + fatherAddress.getAddress().getIp() + ":" + fatherAddress.getAddress().getPort() + " ";
        command = command + pc.toInt() + " ";
        command += OSHelper.preparePath(System.getProperty("java.class.path"));
        OSHelper.exec(command);
    }
    
    private String getPythonLibrary() {
        final String classPath = System.getProperty("java.class.path");
        final String[] split;
        final String[] paths = split = classPath.split(File.pathSeparator);
        for (final String p : split) {
            File f = new File(p);
            if (!f.isDirectory()) {
                f = f.getParentFile();
            }
            f = new File(f.getAbsolutePath() + OSHelper.preparePathSegment("/middlewarePython"));
            if (f.exists()) {
                return f.getAbsolutePath();
            }
        }
        return null;
    }
    
    public void initAll() {
        
        for (final ProcessAndType a : this.process) {
            if (a.getLenguage() == 512) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("YYEEIII");
                            a.getActivity().init();
                        }
                        catch (Exception e) {
                            ProcessInitializer.this.log.developer("Error", a.getID());
                        }
                    }
                }.start();
            }
            else if (a.getLenguage() == 256) {}
        }
    }
}
