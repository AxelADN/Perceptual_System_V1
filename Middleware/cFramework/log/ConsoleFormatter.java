// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.log;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

public class ConsoleFormatter extends Formatter
{
    private String className;
    private static final String LINE_SEPARATOR;
    
    public ConsoleFormatter(final String className) {
        this.className = className;
    }
    
    @Override
    public String format(final LogRecord record) {
        return this.className + " : " + this.formatMessage(record) + ConsoleFormatter.LINE_SEPARATOR;
    }
    
    static {
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
