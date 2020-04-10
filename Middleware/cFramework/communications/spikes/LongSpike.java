// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.spikes;

import java.io.ObjectInput;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.UnknownFormatConversionException;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.Serializable;

public class LongSpike<L extends Serializable, I extends Serializable, T extends Serializable> implements Serializable
{
    private static final long serialVersionUID = -5586813169286074998L;
    private int modality;
    private L location;
    private I intensity;
    private T timing;
    
    public LongSpike(final int modality) {
        this.modality = modality;
        this.location = null;
        this.intensity = null;
        this.timing = null;
    }
    
    public LongSpike(final int modality, final L location, final I intensity, final T timing) {
        this.modality = modality;
        this.location = location;
        this.intensity = intensity;
        this.timing = timing;
    }
    
    public LongSpike(final byte[] spike) throws Exception {
        int i = 0;
        final byte[] tmpShort = new byte[2];
        final byte[] tmpInt = new byte[4];
        System.arraycopy(spike, i, tmpShort, 0, 2);
        final short mLength = this.shortFromByte(tmpShort);
        i += 2;
        System.arraycopy(spike, i, tmpShort, 0, 2);
        final short lLength = this.shortFromByte(tmpShort);
        i += 2;
        System.arraycopy(spike, i, tmpInt, 0, 4);
        final int iLength = this.intFromByte(tmpInt);
        i += 4;
        System.arraycopy(spike, i, tmpShort, 0, 2);
        final short tLength = this.shortFromByte(tmpShort);
        i += 2;
        byte[] tmp = new byte[mLength];
        System.arraycopy(spike, i, tmp, 0, mLength);
        this.modality = this.intFromByte(tmp);
        i += mLength;
        tmp = new byte[lLength];
        System.arraycopy(spike, i, tmp, 0, lLength);
        this.location = this.objectFromByte(tmp);
        i += lLength;
        tmp = new byte[iLength];
        System.arraycopy(spike, i, tmp, 0, iLength);
        this.intensity = this.objectFromByte(tmp);
        i += iLength;
        tmp = new byte[tLength];
        System.arraycopy(spike, i, tmp, 0, tLength);
        this.timing = this.objectFromByte(tmp);
        i += tLength;
    }
    
    public byte[] getByteArray() throws IOException {
        final byte[] mByte = this.intToByte(this.modality);
        final short mLength = (short)mByte.length;
        final byte[] lByte = this.objectToByte(this.location);
        final short lLength = (short)lByte.length;
        final byte[] iByte = this.objectToByte(this.intensity);
        final int iLength = iByte.length;
        final byte[] tByte = this.objectToByte(this.timing);
        final short tLength = (short)tByte.length;
        final byte[] full = new byte[10 + mLength + lLength + iLength + tLength];
        int i = 0;
        System.arraycopy(this.shortToByte(mLength), 0, full, i, 2);
        i += 2;
        System.arraycopy(this.shortToByte(lLength), 0, full, i, 2);
        i += 2;
        System.arraycopy(this.intToByte(iLength), 0, full, i, 4);
        i += 4;
        System.arraycopy(this.shortToByte(tLength), 0, full, i, 2);
        i += 2;
        System.arraycopy(mByte, 0, full, i, mLength);
        i += mLength;
        System.arraycopy(lByte, 0, full, i, lLength);
        i += lLength;
        System.arraycopy(iByte, 0, full, i, iLength);
        i += iLength;
        System.arraycopy(tByte, 0, full, i, tLength);
        return full;
    }
    
    public static int getModality(final byte[] spike) {
        final byte[] tmp = new byte[4];
        System.arraycopy(spike, 10, tmp, 0, 4);
        final ByteBuffer buffer = ByteBuffer.wrap(tmp);
        return buffer.getInt();
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
    
    public LongSpike<L, I, T> clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean equals(final LongSpike<L, I, T> spike) {
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
    
    private byte[] intToByte(final int num) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(num);
        return buffer.array();
    }
    
    private <M> byte[] objectToByte(final M obj) throws IOException {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = null;
        byte[] a = null;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(obj);
            o.flush();
            a = b.toByteArray();
        }
        catch (IOException ex) {}
        finally {
            b.close();
        }
        return a;
    }
    
    private byte[] shortToByte(final short n) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(n);
        return buffer.array();
    }
    
    private int intFromByte(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }
    
    private short shortFromByte(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getShort();
    }
    
    private <M> M objectFromByte(final byte[] bytes) throws IOException, ClassNotFoundException {
        Object o = null;
        final ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInput i = null;
        try {
            i = new ObjectInputStream(b);
            o = i.readObject();
        }
        finally {
            if (i != null) {
                i.close();
            }
        }
        return (M)o;
    }
}
