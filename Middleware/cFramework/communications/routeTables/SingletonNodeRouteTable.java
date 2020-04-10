// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.routeTables;

public class SingletonNodeRouteTable extends NodeRouteTable
{
    private static SingletonNodeRouteTable instance;
    
    public static SingletonNodeRouteTable getInstance() {
        if (SingletonNodeRouteTable.instance == null) {
            SingletonNodeRouteTable.instance = new SingletonNodeRouteTable();
        }
        return SingletonNodeRouteTable.instance;
    }
    
    static {
        SingletonNodeRouteTable.instance = null;
    }
}
