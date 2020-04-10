// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.spikes;

import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.UnknownFormatConversionException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.Serializable;

public class Spike<L extends Serializable, I extends Serializable, T extends Serializable> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int modality;
    private L location;
    private I intensity;
    private T timing;
    
    public Spike(final int modality) {
        this.modality = modality;
        this.location = null;
        this.intensity = null;
        this.timing = null;
    }
    
    public Spike(final int modality, final L location, final I intensity, final T timing) {
        this.modality = modality;
        this.location = location;
        this.intensity = intensity;
        this.timing = timing;
    }
    
    public Spike(final byte[] spike) throws Exception {
        short count = 0;
        byte[] tmp = new byte[2];
        System.arraycopy(spike, count, tmp, 0, 2);
        final short mShort = this.shortFromByte(tmp);
        count += 2;
        System.arraycopy(spike, count, tmp, 0, 2);
        final short lShort = this.shortFromByte(tmp);
        count += 2;
        System.arraycopy(spike, count, tmp, 0, 2);
        final short iShort = this.shortFromByte(tmp);
        count += 2;
        System.arraycopy(spike, count, tmp, 0, 2);
        final short tShort = this.shortFromByte(tmp);
        count += 2;
        tmp = new byte[mShort];
        System.arraycopy(spike, count, tmp, 0, mShort);
        this.modality = this.modalityFromByte(tmp);
        count += mShort;
        tmp = new byte[lShort];
        System.arraycopy(spike, count, tmp, 0, lShort);
        this.location = this.objectFromByte(tmp);
        count += lShort;
        tmp = new byte[iShort];
        System.arraycopy(spike, count, tmp, 0, iShort);
        this.intensity = this.objectFromByte(tmp);
        count += iShort;
        tmp = new byte[tShort];
        System.arraycopy(spike, count, tmp, 0, tShort);
        this.timing = this.objectFromByte(tmp);
        count += tShort;
    }
    
    public byte[] getByteArray() throws IOException {
        final byte[] mByte = this.modalityToByte(this.modality);
        final short mShort = (short)mByte.length;
        final byte[] lByte = this.objectToByte(this.location);
        final short lShort = (short)lByte.length;
        final byte[] iByte = this.objectToByte(this.intensity);
        final short iShort = (short)iByte.length;
        final byte[] tByte = this.objectToByte(this.timing);
        final short tShort = (short)tByte.length;
        final byte[] full = new byte[8 + mShort + lShort + iShort + tShort];
        int i = 0;
        System.arraycopy(this.shortToByte(mShort), 0, full, i, 2);
        i += 2;
        System.arraycopy(this.shortToByte(lShort), 0, full, i, 2);
        i += 2;
        System.arraycopy(this.shortToByte(iShort), 0, full, i, 2);
        i += 2;
        System.arraycopy(this.shortToByte(tShort), 0, full, i, 2);
        i += 2;
        System.arraycopy(mByte, 0, full, i, mShort);
        i += mShort;
        System.arraycopy(lByte, 0, full, i, lShort);
        i += lShort;
        System.arraycopy(iByte, 0, full, i, iShort);
        i += iShort;
        System.arraycopy(tByte, 0, full, i, tShort);
        i += tShort;
        return full;
    }
    
    public static int getModality(final byte[] spike) {
        final byte[] tmp = new byte[4];
        System.arraycopy(spike, 8, tmp, 0, 4);
        final ByteBuffer buffer = ByteBuffer.wrap(tmp);
        return buffer.getInt();
    }
    
    public static Object getLocation(final byte[] spike) throws IOException, ClassNotFoundException {
        byte[] tmp = new byte[2];
        System.arraycopy(spike, 2, tmp, 0, 2);
        final ByteBuffer buffer = ByteBuffer.wrap(tmp);
        final short length = buffer.getShort();
        tmp = new byte[length];
        System.arraycopy(spike, 12, tmp, 0, length);
        final ByteArrayInputStream b = new ByteArrayInputStream(tmp);
        final ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }
    
    public void setModality(final int modality) {
        this.modality = modality;
    }
    
    public void setLocation(final L location) {
        this.location = location;
    }
    
    public void setIntensity(final I intensity) {
        this.intensity = intensity;
    }
    
    public void setTiming(final T timing) {
        this.timing = timing;
    }
    
    public int getModality() {
        return this.modality;
    }
    
    public L getLocation() {
        return this.location;
    }
    
    public I getIntensity() {
        return this.intensity;
    }
    
    public T getTiming() {
        return this.timing;
    }
    
    public Spike<L, I, T> clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean equals(final Spike<L, I, T> spike) {
        try {
            return spike.getModality() == this.modality && spike.getLocation().equals(this.location) && spike.getIntensity().equals(this.intensity) && spike.getTiming().equals(this.timing);
        }
        catch (Exception ex) {
            throw new UnknownFormatConversionException("Unsoported data types");
        }
    }
    
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private byte[] modalityToByte(final int modality) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(modality);
        return buffer.array();
    }
    
    private <M> byte[] objectToByte(final M obj) throws IOException {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }
    
    private byte[] shortToByte(final short n) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(n);
        return buffer.array();
    }
    
    private int modalityFromByte(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }
    
    private <M> M objectFromByte(final byte[] bytes) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        final ObjectInputStream o = new ObjectInputStream(b);
        return (M)o.readObject();
    }
    
    private short shortFromByte(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getShort();
    }
}
