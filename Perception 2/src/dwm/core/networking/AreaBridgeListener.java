/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dwm.core.networking;

/**
 *
 * @author luis_
 */
public interface AreaBridgeListener {

    public void receiveFromBridge(byte data[]);
    
    public void receiveFromBridge(String data);
    
}
