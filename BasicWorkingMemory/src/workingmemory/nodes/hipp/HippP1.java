/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.hipp;

import java.util.concurrent.ConcurrentHashMap;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class HippP1 extends SmallNode {

    private ConcurrentHashMap<Integer, String> scenesInTime = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> objectsInTime = new ConcurrentHashMap<>();

    public HippP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.Hippocampus) {
            System.out.println("Iniciando nodo " + this.getClass().getName());
        } else {

            try {
                System.out.println("Ready for object-location integration");

                Spike spike = Spike.fromBytes(data);

                switch (spike.getId()) {
                    case SpikeTypes.SCENE_OBJECTS:

                        Spike<Integer, Integer, Integer, Integer> sizeSpike = (Spike<Integer, Integer, Integer, Integer>) spike;

                        objectsInTime.put(sizeSpike.getDuration(), sizeSpike.getIntensity());

                        System.out.println("Reserve space for: " + sizeSpike.getIntensity());

                        break;

                    case SpikeTypes.ITC_CLASS:

                        Spike<Integer, Integer, Integer, Integer> itcSpike = (Spike<Integer, Integer, Integer, Integer>) spike;
                        
                        System.out.println("Object received: " + itcSpike.getModality() + " " + itcSpike.getIntensity() + " " + itcSpike.getLocation());
                        
                        String sceneString = scenesInTime.get(itcSpike.getDuration());
                        
                        System.out.println("Original scene in time: "+itcSpike.getDuration()+" "+sceneString);
                        
                        String rdString = sceneString.replace("(" + 1 + ")", "(" + itcSpike.getLocation() + ")");
                        scenesInTime.put(itcSpike.getDuration(), rdString);
                        
                        //Decreases the number of expected objects
                        
                        objectsInTime.put(itcSpike.getDuration(), objectsInTime.get(itcSpike.getDuration()) - 1);
                        
                        if(objectsInTime.get(itcSpike.getDuration()) == 0){
                       
                            Spike<Integer, Integer, byte[], Integer> sceneSpike;

                            sceneSpike = new Spike(SpikeTypes.ENCODED_SCENE, "SceneSpike", 0, 0, rdString.getBytes(), itcSpike.getDuration());

                            efferents(AreaNames.PrefrontalCortex, sceneSpike.toBytes());
                            
                            //Mid-term memory
                            
                            efferents(AreaNames.Hippocampus, sceneSpike.toBytes());
                            
                            System.out.println("All objects received, send to PFC");
                        
                        }
                        
                        
                        System.out.println("Spatial scene: " + rdString);
                        
                        break;

                    case SpikeTypes.MTL_SPATIAL:
                        
                        Spike<Integer, Integer, byte[], Integer> mtlSpike = (Spike<Integer, Integer, byte[], Integer>) spike;
                        String dString = new String(mtlSpike.getLocation());
                        //String rdString = dString.replace("("+1+")", "(ABC)");

                        scenesInTime.put(mtlSpike.getDuration(), dString);
                        System.out.println("Spatial scene: " + dString + " " + dString);
                        
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
