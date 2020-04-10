// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.nodes.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import cFramework.nodes.area.Area;
import cFramework.util.IDHelper;
import cFramework.communications.NodeAddress;
import cFramework.nodes.NodeConf;
import cFramework.log.NodeLog;
import cFramework.communications.p2p.EntityProtocols;
import cFramework.nodes.area.AreaWrapper;
import java.util.ArrayList;
import cFramework.communications.routeTables.NodeRouteTable;

public class AreasManager
{
    private NodeRouteTable routeTable;
    private ArrayList<AreaWrapper> areas;
    private EntityProtocols protocols;
    private NodeLog entityLog;
    
    public AreasManager(final NodeRouteTable routeTable, final EntityProtocols protocols, final NodeLog log) {
        this.routeTable = routeTable;
        this.protocols = protocols;
        this.entityLog = log;
        this.areas = new ArrayList<AreaWrapper>();
    }
    
    public void addList(final String[] areasNames, final NodeConf nc) {
        
        for (int i = 0; i < areasNames.length; ++i) {
            this.add(areasNames[i], nc);
            System.out.println("YYEEIII");
        }
        try {
            
            Thread.sleep(3000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        this.initAllAreas();
    }
    
    public void addAndInit(final String areaClassName, final NodeConf nc) {
        final AreaWrapper area = this.add(areaClassName, nc);
        if (area != null) {
            area.init();
        }
    }
    
    public AreaWrapper add(final String areaClassName, final NodeConf nc) {
        this.entityLog.developer("Adding area " + areaClassName + " to entity");
        final String[] areaArray = areaClassName.split(":");
        if (areaArray.length > 1 && areaArray[1].equals("BLACKBOX")) {
            new NodeAddress(IDHelper.generateID(areaArray[0]), "0.0.0.0", 0);
        }
        final Area area = this.getAreaObject(areaArray);
        if (area == null) {
            return null;
        }
        final AreaWrapper core = new AreaWrapper(area, this.protocols, nc);
        core.setUp();
        this.areas.add(core);
        final NodeAddress areaNode = core.getProtocol().getNodeAddress();
        this.routeTable.set(areaNode);
        core.setupActivities();
        return core;
    }
    
    public void initAllAreas() {
        this.entityLog.developer("Runing Area's init methods");
        for (final AreaWrapper area : this.areas) {
            area.init();
        }
    }
    
    private Area getAreaObject(final String[] area) {
        final String areaClassName = area[0];
        try {
            final Class<?>[] parameterClasses = (Class<?>[])new Class[(area.length - 1) / 2];
            final Object[] parametersObjects = new Object[(area.length - 1) / 2];
            for (int i = 1; i + 1 < area.length; i += 2) {
                parameterClasses[(i - 1) / 2] = Class.forName(area[i]);
                parametersObjects[(i - 1) / 2] = this.cast(parameterClasses[(i - 1) / 2], area[i + 1]);
            }
            final Class<?> nodeClass = Class.forName(areaClassName);
            final Constructor<?> nodeConstr = nodeClass.getConstructor((Class<?>[])new Class[0]);
            final Area a = (Area)nodeConstr.newInstance(new Object[0]);
            if (area.length == 1) {
                return a;
            }
            try {
                a.getClass().getMethod("init", parameterClasses);
                a.setInitParameters(parameterClasses, parametersObjects);
                return a;
            }
            catch (NoSuchMethodException | SecurityException ex3) {
                try {
                    final Exception ex = null;
                    final Exception e = ex;
                    final StringBuilder sb = new StringBuilder(parameterClasses[0].getSimpleName());
                    for (int j = 1; j < parameterClasses.length; ++j) {
                        sb.append("," + parameterClasses[j].getSimpleName());
                    }
                    System.out.println("ERROR there is no method init(" + sb.toString() + ") on class " + areaClassName);
                }
                catch (IllegalArgumentException | SecurityException ex4) {
                    final Exception ex2 = null;
                    final Exception e2 = ex2;
                    this.entityLog.error(e2.toString() + ": " + areaClassName);
                    e2.printStackTrace();
                }
            }
            catch (IllegalArgumentException ex5) {}
        }
        catch (NoSuchMethodException ex10) {}
        catch (IllegalArgumentException ex11) {}
        catch (InstantiationException ex12) {}
        catch (IllegalAccessException ex13) {}
        catch (InvocationTargetException ex14) {}
        catch (SecurityException ex15) {}
        catch (ClassNotFoundException ex16) {}
        return null;
    }
    
    public Object cast(final Class<?> goalClass, final String object) {
        if (goalClass.equals(Byte.class)) {
            return Byte.parseByte(object);
        }
        if (goalClass.equals(Short.class)) {
            return Short.parseShort(object);
        }
        if (goalClass.equals(Integer.class)) {
            return Integer.parseInt(object);
        }
        if (goalClass.equals(Long.class)) {
            return Float.parseFloat(object);
        }
        if (goalClass.equals(Float.class)) {
            return Float.parseFloat(object);
        }
        if (goalClass.equals(Double.class)) {
            return Double.parseDouble(object);
        }
        if (goalClass.equals(Boolean.class)) {
            return Boolean.parseBoolean(object);
        }
        if (goalClass.equals(Character.class)) {
            return object.charAt(0);
        }
        if (goalClass.equals(String.class)) {
            return object;
        }
        return null;
    }
}
