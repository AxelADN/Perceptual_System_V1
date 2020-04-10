// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.util;

import java.lang.reflect.Field;

public class IDHelper
{
    private static int AreaLeftShift;
    private static long ACTIVITYMASK;
    
    public static boolean isActivitiy(final long ID) {
        return ID << IDHelper.AreaLeftShift != 0L;
    }
    
    public static boolean isArea(final long ID) {
        return ID << IDHelper.AreaLeftShift == 0L;
    }
    
    public static long getAreaID(final long ID) {
        return ID >> IDHelper.AreaLeftShift << IDHelper.AreaLeftShift;
    }
    
    public static final long generateID(final int AreaID, final int ActivityID, final int index) {
        return ((long)AreaID << IDHelper.AreaLeftShift) + ActivityID;
    }
    
    public static final long generateID(final String AreaName, final int ActivityID, final int index) {
        return ((long)AreaName.hashCode() << IDHelper.AreaLeftShift) + ActivityID + index;
    }
    
    public static final long generateID(final String AreaName) {
        return (long)AreaName.hashCode() << IDHelper.AreaLeftShift;
    }
    
    public static final long generateID(final String areaName, final String activityName) {
        return (long)areaName.hashCode() << IDHelper.AreaLeftShift | (IDHelper.ACTIVITYMASK & (long)activityName.hashCode());
    }
    
    public static String getNameAsString(final Class<?> namer, final long name) {
        if (namer == null) {
            return String.valueOf(name);
        }
        final Field[] fields = namer.getFields();
        for (int i = 0; i < fields.length; ++i) {
            try {
                if (name == fields[i].getLong(null)) {
                    return fields[i].getName();
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        return "";
    }
    
    static {
        IDHelper.AreaLeftShift = 32;
        IDHelper.ACTIVITYMASK = 4294967295L;
    }
}
