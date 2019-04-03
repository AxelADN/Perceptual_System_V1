/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Luis Martin
 */
public class ImageTransferUtils {

    private static final int MAX_KB = 30;
    public static final int CHUNK_SIZE = 1024 * MAX_KB;

    public static void saveImage(byte image[], String name) {
        
        try {
            
            InputStream in = new ByteArrayInputStream(image);
            BufferedImage bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "png", new File(name));

        } catch (Exception ex) {
            Logger.getLogger(ImageTransferUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /***
     * Byte operations
     */
    
    public static byte[] intToBytes(int number){
        
        ByteBuffer dbuf = ByteBuffer.allocate(4);
        byte []result = dbuf.putInt(number).array();

        return result;
    }
    
    public static int toInt(byte b[]){
        return ByteBuffer.wrap(b).getInt();
    }
}
