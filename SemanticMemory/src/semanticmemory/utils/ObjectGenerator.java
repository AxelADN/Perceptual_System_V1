/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.utils;

import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.data.SemanticObjects;
import semanticmemory.data.SimpleSemanticObject;

/**
 *
 * @author Luis
 */
public class ObjectGenerator {

    private static int totalObjects = 0;
    private static final char AV_ATT[] = new char[]{'A', 'B', 'C', 'D', 'X', 'Z', 'E', 'U', 'W', 'Q', 'P', 'O', 'I', 'R', 'L', 'N'};

    //ITCL
    private static char animated[] = new char[]{'A', 'A', 0, 0, 'B', 0, 'C', 0, 0};

    //ITCM
    private static char inanimated[] = new char[]{0, 0, 'Z', 'Z', 0, 0, 0, 'Z', 'Z'};
    
    //AMBOS
    private static char both[] = new char[]{'A', 'A', 'Z', 'Z', 'B', 0, 'C', 'Z', 'Z'};
    
    private static char objectTypes[][] = new char[][]{animated,inanimated,both};
    
    
    private static int typesStudyCase[] = new int[]{0,2,0,0,1,0,1};
    
    private static char studyCase[][] = new char[][]{
                                                    {'A', 'A', 'X', 'B', 'B', 'Z', 'C', 'C', 'E'},
                                                    {'A', 'A', 'Z', 'Z', 'B', 'W', 'C', 'Z', 'Z'},
                                                    {'A', 'A', 'D', 'U', 'B', 'Q', 'C', 'P', 'O'},
                                                    {'A', 'A', 'R', 'B', 'B', 'E', 'C', 'D', 'I'},
                                                    {'R', 'A', 'Z', 'Z', 'B', 'B', 'P', 'Z', 'Z'},
                                                    {'A', 'A', 'Z', 'X', 'B', 'L', 'C', 'N', 'C'},
                                                    
                                                    {'R', 'A', 'Z', 'Z', 'B', 'B', 'T', 'Z', 'Z'},//2stage
    };
    
    
    
    public static SemanticObjects generate(int amount, long startTime, int x1, int y1, int type) {

        SemanticObjects sso = new SemanticObjects();

        for (int i = 0; i < amount; i++) {

            SimpleSemanticObject so = generate(startTime, x1, y1, type);
            sso.add(so);

        }

        return sso;

    }
    
    
    public static SimpleSemanticObject generate(long startTime, int x1, int y1, int type) {
            
        /*Dimensiones, en caso de necesitarse su uso, implementar*/
            int x2 = 0;
            int y2 = 0;
                        
            char attributes[] = new char[MemoryConstants.ATTRIBUTES_NUMBER];
            char classAttributes[]; 
            
            for (int j = 0; j < attributes.length; j++) {
                int attIndex = (int) (Math.random() * AV_ATT.length);
                attributes[j] = AV_ATT[attIndex];
            }
            
            classAttributes = objectTypes[type];
             
            for (int j = 0; j < classAttributes.length; j++) {
                
                if (classAttributes[j] != 0) {
                    attributes[j] = classAttributes[j];
                }
                
            }
            
            int timeStamp = (int)startTime; //(int)((System.currentTimeMillis()-startTime)/1000);
            byte intensity = (byte)(Math.random()*100);

            SimpleSemanticObject so = new SimpleSemanticObject(timeStamp);

            so.setObjectId(totalObjects);
            so.setAttributes(attributes);
            so.setAttributes(studyCase[totalObjects]);//Quitar
            so.setIntensity(intensity);
            so.setDimensions(x1, y1, x2, y2);
            
            so.setType(type); //Solo para el dibujo
            so.setType(typesStudyCase[totalObjects]);//Quitar
            totalObjects++;
            
            return so;
    }
    
    public static int getNextID(){
        int id = totalObjects;
        totalObjects++;
        return id;
    }
    

}
