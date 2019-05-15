/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;

/**
 *
 * @author AxelADN
 */
public class RIIC extends StructureTemplate implements Serializable {

    private RIIC_h riic_h;
    private RIIC_c riic_c;

    public RIIC(String loggableObject) {
        super(loggableObject);
        this.riic_c = new RIIC_c("NEW_RIIC_C");
        this.riic_h = new RIIC_h("NEW_RIIC_H");
    }

    public RIIC(RIIC_h riic_h, RIIC_c riic_c, String loggableObject) {
        super(loggableObject);
        this.riic_h = riic_h;
        this.riic_c = riic_c;
    }
    
    public void addRIIC_h(RIIC_h riic_h){
        while(riic_h.isNotEmpty()){
            PreObject preObject = riic_h.nextData();
            this.riic_h.addPreObject(preObject);
        }
        riic_h.retrieveAll();
    }
    
    public void addRIIC_c(RIIC_c riic_c){
        while(riic_c.isNotEmpty()){
            PreObject preObject = riic_c.nextData();
            this.riic_c.addPreObject(preObject);
        }
        riic_c.retrieveAll();
    }

    public void writeRIIC_h(RIIC_h riic_h) {
        this.riic_h = riic_h;
    }

    public void writeRIIC_c(RIIC_c riic_c) {
        this.riic_c = riic_c;
    }

    public RIIC_h readRIIC_h() {
        return riic_h;
    }

    public RIIC_c readRIIC_c() {
        return riic_c;
    }

}
