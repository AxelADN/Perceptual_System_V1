/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.main;

import kmiddle.nodes.NodeConfiguration;
import semanticmemory.areas.tester.TesterNode;
import semanticmemory.areas.vai.VisualArea;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.data.SemanticObjects;
import semanticmemory.utils.ObjectGenerator;
import semanticmemory.utils.GeneralUtils;

/**
 *
 * @author karinajaime
 */

//En mac la vm requiere este parámetro para usar solo ipv4 -Djava.net.preferIPv4Stack=true

public class SemanticMemory {
    
    public static final int OBJECT_GENERATOR = 0;
    
    public void start(){
        
        /*
        * Configuración para los nodos, en caso de que se requiera una diferente se creara una por nodo.
        */
        
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        
        
        /*
        * Orden de ejecución de los Nodos
        */
        
        // Se generan los objetos
        
        SemanticObjects sObjects = ObjectGenerator.generate(5,System.currentTimeMillis(),0,0,0);
        
        sObjects.debug();        
        
        VisualArea vai = new VisualArea(AreaNames.VisualArea, conf);
        
        vai.afferents(SemanticMemory.OBJECT_GENERATOR, GeneralUtils.objectToBytes(sObjects));
        
        //TesterNode tester = new TesterNode(AreaNames.TesterNode, conf);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SemanticMemory sMemory = new SemanticMemory();
        
        sMemory.start();
        
    }
    
}
