// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.fiels;

public class Address implements NullValueConstants
{
    private String ip;
    private int port;
    
    public Address(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public Address() {
        this.ip = "";
        this.port = -1;
    }
    
    public Address(final byte[] b) {
        this(b, 0);
    }
    
    public Address(final byte[] b, final int startIndex) {
        this.ip = "";
        this.ip = this.ip + Short.toString((short)(b[startIndex] & 0xFF)) + ".";
        this.ip = this.ip + Short.toString((short)(b[startIndex + 1] & 0xFF)) + ".";
        this.ip = this.ip + Short.toString((short)(b[startIndex + 2] & 0xFF)) + ".";
        this.ip += Short.toString((short)(b[startIndex + 3] & 0xFF));
        this.port = (b[startIndex + 4] << 8 & 0xFFFF);
        this.port += (b[startIndex + 5] & 0xFF);
    }
    
    public void setIp(final String i) {
        this.ip = i;
    }
    
    public void setPort(final int p) {
        this.port = p;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public int getPort() {
        return this.port;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o.getClass() == Address.class) {
            final Address a = (Address)o;
            return a.getIp().compareTo(this.ip) == 0 && a.getPort() == this.port;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + ((this.ip != null) ? this.ip.hashCode() : 0);
        hash = 89 * hash + this.port;
        return hash;
    }
    
    @Override
    public String toString() {
        return "[" + this.getIp() + "," + this.getPort() + "]";
    }
    
    public byte[] toByteArray() {
        final byte[] r = new byte[6];
        int start = 0;
        int point = 0;
        for (int i = 0; i < 4; ++i) {
            point = this.ip.indexOf(".", point + 1);
            if (point == -1) {
                r[i] = this.toUnsignedByte(this.ip.substring(start));
            }
            else {
                r[i] = this.toUnsignedByte(this.ip.substring(start, point));
            }
            start = point + 1;
        }
        r[4] = (byte)(this.port >> 8 & 0xFF);
        r[5] = (byte)(this.port & 0xFF);
        return r;
    }
    
    public boolean isNullIp() {
        return this.getIp().compareTo("") == 0;
    }
    
    public boolean isNullPort() {
        return this.getPort() == -1;
    }
    
    private byte toUnsignedByte(final String value) {
        return (byte)((byte)Short.parseShort(value) & 0xFF);
    }
    
    public static void main(final String[] args) {
        System.out.println(new Address(new Address("10.0.5.238", 60390).toByteArray()).toString());
    }
}
