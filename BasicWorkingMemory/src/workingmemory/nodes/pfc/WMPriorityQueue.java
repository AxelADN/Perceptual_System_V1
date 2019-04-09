/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

import java.util.Comparator;
import java.util.PriorityQueue;
import workingmemory.core.entities.Percept;

/**
 *
 * @author Luis Martin
 */

class PerceptComparator implements Comparator<Percept>
{
    @Override
    public int compare(Percept x, Percept y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.getTime() < y.getTime())
        {
            return -1;
        }
        if (x.getTime() > y.getTime())
        {
            return 1;
        }
        return 0;
    }
}

public class WMPriorityQueue {
    
    private PriorityQueue<Percept> queue;
    
    public WMPriorityQueue(){
        PerceptComparator comparator = new PerceptComparator();
        queue = new PriorityQueue<Percept>(10, comparator);
    }
    
    public void add(Percept percept){
        queue.add(percept);
    }
}
