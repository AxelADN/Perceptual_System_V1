/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

/**
 *
 * @author AxelADN
 */
public class ObserverNode extends AreaTemplate{
    
    public ObserverNode () {
        this.ID =   Names.ObserverNode;
        addProcess(StartingNode.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
