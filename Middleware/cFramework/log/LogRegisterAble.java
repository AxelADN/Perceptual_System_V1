// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.log;

import cFramework.communications.fiels.Address;

public interface LogRegisterAble
{
    String header(final long p0, final Address p1);
    
    String send(final long p0, final String p1);
    
    String send(final Address p0, final String p1);
    
    String send_debug(final long p0, final String p1);
    
    String receive(final long p0, final String p1);
    
    String receive(final Address p0, final String p1);
    
    String receive_debug(final long p0, final String p1);
    
    String saveRequest(final long p0, final String p1);
    
    String error(final String p0);
    
    String info(final String p0);
    
    String info(final String p0, final long p1);
    
    String developer(final String p0);
    
    String developer(final String p0, final long p1);
}
