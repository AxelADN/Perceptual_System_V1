// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications;

import cFramework.communications.fiels.Address;

public class AddressAndBytes
{
    private Address address;
    private byte[] bytes;
    
    public Address getAddress() {
        return this.address;
    }
    
    public byte[] getBytes() {
        return this.bytes;
    }
    
    public AddressAndBytes(final Address a, final byte[] m) {
        this.address = a;
        this.bytes = m;
    }
}
