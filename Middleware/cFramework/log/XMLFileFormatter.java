// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.log;

import java.util.logging.Handler;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;

public class XMLFileFormatter extends Formatter
{
    private static final String LINE_SEPARATOR;
    
    @Override
    public String format(final LogRecord record) {
        final StringBuilder sb = new StringBuilder();
        final Calendar time = Calendar.getInstance();
        time.setTimeInMillis(record.getMillis());
        sb.append("<record>").append("<time>").append("<hour>").append(time.get(11)).append("</hour>").append("<minute>").append(time.get(12)).append("</minute>").append("<second>").append(time.get(13)).append("</second>").append("<milisecond>").append(time.get(14)).append("</milisecond>").append("</time>").append("<data>").append(this.formatMessage(record)).append("</data>").append("</record>").append(XMLFileFormatter.LINE_SEPARATOR);
        if (record.getThrown() != null) {
            try {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
            catch (Exception ex) {}
        }
        return sb.toString();
    }
    
    @Override
    public String getHead(final Handler h) {
        return "<?xml version=\"1.0\" encoding=\"" + h.getEncoding() + "\" standalone=\"no\"?>" + XMLFileFormatter.LINE_SEPARATOR + "<!DOCTYPE rootElement>" + XMLFileFormatter.LINE_SEPARATOR + "<log>" + XMLFileFormatter.LINE_SEPARATOR;
    }
    
    @Override
    public String getTail(final Handler h) {
        return "</log>";
    }
    
    static {
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
