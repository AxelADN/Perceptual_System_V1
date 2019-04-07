/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import kmiddle2.nodes.activities.Activity;
import perception.GUI.ProcessInterface;
import perception.config.AreaNames;

/**
 *
 * @author AxelADN
 */
public class GenericActivity extends Activity implements ProcessInterface {

    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeProcess(Object src, byte[] data) {
        send(AreaNames.Categorization,data.toString().getBytes());
    }
    
}
