/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes;

import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.MyLogger;

/**
 *
 * @author Luis
 */
public class ObjectNode extends MemoryNode{

    protected int id;
    protected char attributes[];
    protected int locations[];
    protected byte intensities[];
    
    public ObjectNode(){
        
    }
    
    public ObjectNode(int id){
        this.id = id;
        this.attributes = new char[MemoryConstants.ATTRIBUTES_NUMBER];
        this.locations = new int[MemoryConstants.ATTRIBUTES_NUMBER];
        this.intensities = new byte[MemoryConstants.ATTRIBUTES_NUMBER];
        this.timeInArea = System.currentTimeMillis();
        this.known = false;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void addAttribute(char attribute, int location,byte intensity) {
        this.attributes[location] = attribute;
        this.locations[location] = location;
        this.intensities[location] = intensity;
    }

    public void setAttributes(char attributes[]) {
        this.attributes = attributes;
    }

    public char[] getAttributes() {
        return this.attributes;
    }
    
    public int[] getLocations() {
        return locations;
    }

    public void setLocations(int[] locations) {
        this.locations = locations;
    }

    public byte[] getIntensities() {
        return intensities;
    }

    public void setIntensities(byte[] intensities) {
        this.intensities = intensities;
    }
    
    public boolean equals(Object obj) {
        
        boolean result = false;
        
        if(obj instanceof ObjectNode){
            int matchs = 0;
            char comparedAttributes[] = ((ObjectNode) obj).getAttributes();
            
            for (int i = 0; i < attributes.length; i++) {
                if (comparedAttributes[i] == attributes[i]) {
                    matchs++;
                }
            }

            result = (matchs == attributes.length);
        }
        
        return result;
    }
    
    public void debugAttributes() {

        String atts = "";

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        MyLogger.log("OBJECT [" + id + "] [" + atts + "]");
    }

    public String toAttString() {

        String atts = "";

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        return "[" + atts + "]";
    }
    
}
