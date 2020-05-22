/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Config.AreaTemplate;
import Config.Names;
import Processes.AMY.*;

/**
 *
 * @author AxelADN
 */
public class AMY extends AreaTemplate{
    
    public AMY (){
        this.ID =   Names.AMY;
        addProcess(AMY_Associations.class);
        addProcess(AMY_Retrieval.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
