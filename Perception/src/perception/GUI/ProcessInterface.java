/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.GUI;

import kmiddle2.nodes.activities.Activity;

/**
 *
 * @author AxelADN
 */
public interface ProcessInterface {
    
    public void executeProcess(Object src, byte[] data);
}
