/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.ventralvc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.nodes.custom.BigNode;
import workingmemory.nodes.main.MainBigNodeP1;

/**
 *
 * @author Luis Martin
 */
public class VentralVC extends BigNode {

    public VentralVC(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.VentralVCP1, VentralVCP1.class);
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.VentralVCP1, getName(), initialData);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.VentralVCP1, senderID, data);
    }
}
