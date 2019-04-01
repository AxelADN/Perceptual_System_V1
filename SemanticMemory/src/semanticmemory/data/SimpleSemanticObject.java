/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.data;

import semanticmemory.utils.Dimension;

/**
 *
 * @author Luis
 */
public class SimpleSemanticObject extends SemanticObject {

    private int type; //Solo para el drawing
    private int objectId;
    private char[] attributes;
    private Dimension dimension;
    private byte intensity;

    public SimpleSemanticObject(int timeStamp) {
        super(timeStamp);
    }
    
    public SimpleSemanticObject(int id,int timeStamp) {
        super(timeStamp);
        this.objectId = id;
    }

    /**
     * @return the dimension
     */
    public Dimension getDimension() {
        return dimension;
    }

    public void setDimensions(int x1, int y1, int x2, int y2) {
        dimension = new Dimension(x1, y1, x2, y2);
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
        
    /**
     * @return the objectId
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    /**
     * @return the intensity
     */
    public byte getIntensity() {
        return intensity;
    }

    /**
     * @param intensity the intensity to set
     */
    public void setIntensity(byte intensity) {
        this.intensity = intensity;
    }

    /**
     * @return the attributes
     */
    public char[] getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(char[] attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        
        String atts = "[ ";
        for (int j = 0; j < attributes.length; j++) {
            atts += attributes[j] + " ";
        }
        atts += "]";

        String debug =  "ID: " + objectId + ": " + atts +", TS: "+getTimeStamp();
        return debug;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
