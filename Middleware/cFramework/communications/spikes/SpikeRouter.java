// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.spikes;

import java.util.Arrays;

public class SpikeRouter
{
    private long[] to_sorted;
    public long ROUTERID;
    public long[] from;
    public long[] to;
    public SpikeMerger merger;
    
    public SpikeRouter(final long[] from, final long[] to) {
        this.ROUTERID = 0L;
        this.merger = null;
        this.from = from;
        this.to = to;
        this.to_sorted = to.clone();
        Arrays.sort(to.clone());
    }
    
    public SpikeRouter(final long[] from, final long[] to, final SpikeMerger merger) {
        this.ROUTERID = 0L;
        this.merger = null;
        this.from = from;
        this.to = to;
        this.to_sorted = to.clone();
        Arrays.sort(to.clone());
        this.merger = merger;
    }
    
    public boolean isTargetToID(final long sendToID) {
        return Arrays.binarySearch(this.to_sorted, sendToID) >= 0;
    }
}
