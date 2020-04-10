// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.util;

import cFramework.communications.MessageMetadata;

public class BinaryHelper
{
    public static final int MessageMetadataBytesLengh = 4;
    
    public static short byteToShort(final byte[] bin, final int startIndex) {
        short r = (short)(bin[startIndex] << 8 & 0xFFFF);
        r += (short)(bin[startIndex + 1] & 0xFF);
        return r;
    }
    
    public static int byteToUnsignedShort(final byte[] bin, final int startIndex) {
        int r = bin[startIndex] << 8 & 0xFFFF;
        r += (bin[startIndex + 1] & 0xFF);
        return r;
    }
    
    public static MessageMetadata byteToMessageMetaData(final byte[] bin, final int startIndex) {
        int r = 0;
        r += (bin[startIndex] << 24 & -1);
        r += (bin[startIndex + 1] << 16 & 0xFFFFFF);
        r += (bin[startIndex + 2] << 8 & 0xFFFF);
        r += (bin[startIndex + 3] & 0xFF);
        return new MessageMetadata(r);
    }
    
    public static byte[] MessageMetadataToByte(final MessageMetadata meta) {
        final int i = meta.time;
        final byte[] r = { (byte)(i >> 24 & 0xFF), (byte)(i >> 16 & 0xFF), (byte)(i >> 8 & 0xFF), (byte)(i & 0xFF) };
        return r;
    }
    
    public static int byteToInt(final byte[] bin, final int startIndex) {
        int r = 0;
        r += (bin[startIndex] << 24 & -1);
        r += (bin[startIndex + 1] << 16 & 0xFFFFFF);
        r += (bin[startIndex + 2] << 8 & 0xFFFF);
        r += (bin[startIndex + 3] & 0xFF);
        return r;
    }
    
    public static long byteToLong(final byte[] bin, final int startIndex) {
        long r = 0L;
        r += ((long)bin[startIndex] << 56 & -1L);
        r += ((long)bin[startIndex + 1] << 48 & 0xFFFFFFFFFFFFFFL);
        r += ((long)bin[startIndex + 2] << 40 & 0xFFFFFFFFFFFFL);
        r += ((long)bin[startIndex + 3] << 32 & 0xFFFFFFFFFFL);
        r += ((long)bin[startIndex + 4] << 24 & 0xFFFFFFFFL);
        r += ((long)bin[startIndex + 5] << 16 & 0xFFFFFFL);
        r += ((long)bin[startIndex + 6] << 8 & 0xFFFFL);
        r += ((long)bin[startIndex + 7] & 0xFFL);
        return r;
    }
    
    public static byte[] longToByte(final long i) {
        final byte[] r = { (byte)(i >> 56 & 0xFFL), (byte)(i >> 48 & 0xFFL), (byte)(i >> 40 & 0xFFL), (byte)(i >> 32 & 0xFFL), (byte)(i >> 24 & 0xFFL), (byte)(i >> 16 & 0xFFL), (byte)(i >> 8 & 0xFFL), (byte)(i & 0xFFL) };
        return r;
    }
    
    public static String byteToIP(final byte[] b, final int startIndex) {
        String ip = "";
        ip = ip + Short.toString((short)(b[startIndex] & 0xFF)) + ".";
        ip = ip + Short.toString((short)(b[startIndex + 1] & 0xFF)) + ".";
        ip = ip + Short.toString((short)(b[startIndex + 2] & 0xFF)) + ".";
        ip += Short.toString((short)(b[startIndex + 3] & 0xFF));
        return ip;
    }
    
    public static String byteToString(final byte[] bin, final int startIndex, final int lenght) {
        String ret = "";
        for (int i = 0; i < lenght; ++i) {
            ret += (char)bin[startIndex + i];
        }
        return ret;
    }
    
    public static byte[] stringToByte(final String s) {
        return mergeByteArrays(new byte[] { (byte)s.length() }, s.getBytes());
    }
    
    public static byte[] shortToByte(final short s) {
        return new byte[] { (byte)(s >> 8 & 0xFF), (byte)s };
    }
    
    public static byte[] intToByte(final int i) {
        final byte[] r = { (byte)(i >> 24 & 0xFF), (byte)(i >> 16 & 0xFF), (byte)(i >> 8 & 0xFF), (byte)(i & 0xFF) };
        return r;
    }
    
    public static byte[] mergeByteArrays(final byte[] a, final byte[] b) {
        return mergeByteArrays(a, b, new byte[0], new byte[0], new byte[0], new byte[0], new byte[0]);
    }
    
    public static byte[] mergeByteArrays(final byte[] a, final byte[] b, final byte[] c) {
        return mergeByteArrays(a, b, c, new byte[0], new byte[0], new byte[0], new byte[0]);
    }
    
    public static byte[] mergeByteArrays(final byte[] a, final byte[] b, final byte[] c, final byte[] d) {
        return mergeByteArrays(a, b, c, d, new byte[0], new byte[0], new byte[0]);
    }
    
    public static byte[] mergeByteArrays(final byte[] a, final byte[] b, final byte[] c, final byte[] d, final byte[] e) {
        return mergeByteArrays(a, b, c, d, e, new byte[0], new byte[0]);
    }
    
    public static byte[] mergeByteArrays(final byte[] a, final byte[] b, final byte[] c, final byte[] d, final byte[] e, final byte[] f) {
        return mergeByteArrays(a, b, c, d, e, f, new byte[0]);
    }
    
    public static byte[] mergeByteArrays(final byte[] a, final byte[] b, final byte[] c, final byte[] d, final byte[] e, final byte[] f, final byte[] g) {
        final byte[] r = new byte[a.length + b.length + c.length + d.length + e.length + f.length + g.length];
        int p = 0;
        System.arraycopy(a, 0, r, p, a.length);
        if ((p += a.length) >= r.length) {
            return r;
        }
        System.arraycopy(b, 0, r, p, b.length);
        if ((p += b.length) >= r.length) {
            return r;
        }
        System.arraycopy(c, 0, r, p, c.length);
        if ((p += c.length) >= r.length) {
            return r;
        }
        System.arraycopy(d, 0, r, p, d.length);
        if ((p += d.length) >= r.length) {
            return r;
        }
        System.arraycopy(e, 0, r, p, e.length);
        if ((p += e.length) >= r.length) {
            return r;
        }
        System.arraycopy(f, 0, r, p, f.length);
        if ((p += f.length) >= r.length) {
            return r;
        }
        System.arraycopy(g, 0, r, p, g.length);
        return r;
    }
    
    public static byte[] subByteArray(final byte[] bin, final int startIndex, final int length) {
        if (bin.length < startIndex + length) {
            throw new IndexOutOfBoundsException("Error al conseguir subsArray");
        }
        final byte[] c = new byte[length];
        System.arraycopy(bin, startIndex, c, 0, length);
        return c;
    }
}
