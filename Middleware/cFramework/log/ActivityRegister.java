// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.log;

import cFramework.util.IDHelper;
import cFramework.communications.fiels.Address;

public class ActivityRegister implements LogRegisterAble
{
    Class<?> namer;
    int smnn;
    
    public ActivityRegister(final Class<?> namer) {
        this.namer = namer;
    }
    
    @Override
    public String header(final long name, final Address address) {
        final String msg = "<start><name>" + IDHelper.getNameAsString(this.namer, name) + "</name>\n<ID>" + name + "</ID>\n<address>\n\t<host>" + address.getIp() + "</host>\n\t<port>" + address.getPort() + "</port>\n</address>\n</start>";
        return msg;
    }
    
    @Override
    public String send(final long name, final String more) {
        return "<SEND><to>" + IDHelper.getNameAsString(this.namer, name) + "</to>" + (more.equals("") ? "" : ("<data>" + more + "</data>")) + "</SEND>";
    }
    
    @Override
    public String send(final Address addr, final String more) {
        return "<SEND><to>" + addr.toString() + "</to>" + (more.equals("") ? "" : ("<data>" + more + "</data>")) + "</SEND>";
    }
    
    @Override
    public String send_debug(final long name, final String more) {
        return "<send> <to>" + IDHelper.getNameAsString(this.namer, name) + "</to>" + (more.equals("") ? "" : ("<data>" + more + "</data>")) + "</send>";
    }
    
    @Override
    public String receive(final long name, final String more) {
        return "<receive> <from>" + IDHelper.getNameAsString(this.namer, name) + "</from>" + (more.equals("") ? "" : ("<data>" + more + "</data>")) + "</receive>";
    }
    
    @Override
    public String receive(final Address addr, final String more) {
        return "<receive><from>" + addr + "</from>" + more + "</receive>";
    }
    
    @Override
    public String receive_debug(final long name, final String more) {
        return "<receive> <sender> <name>" + IDHelper.getNameAsString(this.namer, name) + "</name></sender> " + (more.equals("") ? "" : ("<data>" + more + "</data>")) + "</receive>";
    }
    
    @Override
    public String error(final String more) {
        return "<ERROR>" + more + "</ERROR>";
    }
    
    @Override
    public String info(final String more) {
        return "<INFO>" + more + "</INFO>";
    }
    
    @Override
    public String info(final String more, final long node) {
        return "<INFO>" + more + IDHelper.getNameAsString(this.namer, node) + "</INFO>";
    }
    
    @Override
    public String developer(final String more) {
        return "<developer>" + more + "</developer>";
    }
    
    @Override
    public String developer(final String more, final long node) {
        return "<developer>" + more + IDHelper.getNameAsString(this.namer, node) + "</developer>";
    }
    
    @Override
    public String saveRequest(final long name, final String more) {
        return null;
    }
}
