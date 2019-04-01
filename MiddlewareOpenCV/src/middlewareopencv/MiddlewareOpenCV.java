/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareopencv;

import kmiddle.nodes.NodeConfiguration;
import middlewareopencv.core.config.AreaNames;
import middlewareopencv.core.nodes.BigNodeExample;
import middlewareopencv.core.nodes.BigNodeExample2;

/**
 *
 * @author Luis
 */
public class MiddlewareOpenCV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(false);
        conf.setLoadBalance(false);

        BigNodeExample vai1 = new BigNodeExample(AreaNames.VisualArea1, conf);
        
        BigNodeExample2 vai2 = new BigNodeExample2(AreaNames.VisualArea2, conf);


    }
    
}
