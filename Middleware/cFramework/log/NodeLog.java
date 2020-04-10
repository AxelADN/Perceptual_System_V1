// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.log;

import cFramework.communications.fiels.Address;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.Calendar;
import java.io.File;
import cFramework.util.OSHelper;
import cFramework.util.IDHelper;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class NodeLog
{
    LogRegisterAble register;
    Logger logger;
    Boolean isDebug;
    String className;
    
    public NodeLog(final String name, final Boolean isDebug) {
        this.logger = null;
        this.register = new AreaRegistrer();
        (this.logger = Logger.getLogger(name)).setUseParentHandlers(false);
        final ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new ConsoleFormatter(name));
        if (isDebug == null) {
            this.logger.setLevel(Level.ALL);
            ch.setLevel(Level.ALL);
        }
        else if (isDebug) {
            this.logger.setLevel(Level.FINER);
            ch.setLevel(Level.FINER);
        }
        else {
            this.logger.setLevel(Level.FINE);
            ch.setLevel(Level.FINE);
        }
        this.logger.addHandler(ch);
    }
    
    public NodeLog(final long name, final Class<?> namer, final Boolean isDebug) {
        this.logger = null;
        this.isDebug = isDebug;
        if (IDHelper.isArea(name)) {
            this.register = new AreaRegistrer(namer);
        }
        else {
            this.register = new ActivityRegister(namer);
        }
        this.className = IDHelper.getNameAsString(namer, name);
        (this.logger = Logger.getLogger(this.className)).setUseParentHandlers(false);
        final ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new ConsoleFormatter(this.className));
        if (isDebug == null) {
            this.logger.setLevel(Level.ALL);
            ch.setLevel(Level.ALL);
        }
        else if (isDebug) {
            this.logger.setLevel(Level.FINER);
            ch.setLevel(Level.FINER);
        }
        else {
            this.logger.setLevel(Level.FINE);
            ch.setLevel(Level.FINE);
        }
        this.logger.addHandler(ch);
        final String path = OSHelper.getMainFolder() + "/log";
        if (!new File(path).exists() && !new File(path).mkdir()) {
            System.out.println("Fatal error Could not create log folder");
            System.out.println(path);
        }
        try {
            final Calendar cal = Calendar.getInstance();
            final FileHandler fh = new FileHandler(path + "/" + cal.get(1) + "_" + cal.get(2) + "_" + cal.get(5) + "_" + cal.get(11) + "_" + cal.get(12) + "_" + this.className + ".log");
            this.logger.addHandler(fh);
            fh.setEncoding("UTF-8");
            fh.setFormatter(new XMLFileFormatter());
        }
        catch (SecurityException | IOException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            this.logger.warning(e.getMessage());
        }
    }
    
    public void header(final long name, final Address address) {
        this.logger.severe(this.register.header(name, address));
    }
    
    public void send(final long name, final String more) {
        this.logger.severe(this.register.send(name, more));
    }
    
    public void send_debug(final long name, final String more) {
        this.logger.finer(this.register.send(name, more));
    }
    
    public void send(final long name, final short type, final String more) {
        this.logger.severe(this.register.send(name, more));
    }
    
    public void send(final Address addr, final String more) {
        this.logger.info(this.register.send(addr, more));
    }
    
    public void receive(final long name, final String more) {
        this.logger.info(this.register.receive(name, more));
    }
    
    public void receive(final Address addr, final String more) {
        this.logger.info(this.register.receive(addr, more));
    }
    
    public void receive(final long name, final short type, final String more) {
        this.logger.severe(this.register.receive(name, more));
    }
    
    public void receive_debug(final long name, final String more) {
        this.logger.finer(this.register.receive_debug(name, more));
    }
    
    public void saveRequest(final long name, final String dataType) {
        this.logger.info(this.register.saveRequest(name, dataType));
    }
    
    public void debug(final String more) {
        this.logger.finer(this.register.info(more));
    }
    
    public void debug(final String more, final long to) {
        this.logger.finer(this.register.info(more, to));
    }
    
    public void developer(final String more) {
        this.logger.finest(this.register.developer(more));
    }
    
    public void developer(final String more, final long to) {
        this.logger.finest(this.register.developer(more, to));
    }
    
    public void error(final String more) {
        this.logger.severe(this.register.error(more));
    }
    
    public void message(final String message) {
        this.logger.severe(this.register.info(message));
    }
}
