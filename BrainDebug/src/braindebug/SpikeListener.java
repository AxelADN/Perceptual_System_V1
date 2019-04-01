/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braindebug;

import braindebug.gui.BrainAreaLog;
import braindebug.gui.BrainPanel;
import braindebug.gui.ImagePanel;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import kmiddle.utils.NodeNameHelper;

/**
 *
 * @author Luis
 */
public class SpikeListener extends BigNode{
    
    private ImagePanel panel;
    private BrainPanel brainPanel;
    private LogConfig logConfig;
    private BrainDebugFrame frame;

    public SpikeListener(int name, NodeConfiguration config, ImagePanel panel,LogConfig logConfig,BrainPanel brainPanel) {
        super(name, config, AreaNames.class);
        this.panel = panel;
        this.logConfig = logConfig;
        this.brainPanel = brainPanel;
    }

    public BrainDebugFrame getFrame() {
        return frame;
    }

    public void setFrame(BrainDebugFrame frame) {
        this.frame = frame;
    }
    
    @Override
    public void init() {
      
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        
    
        int sender = NodeNameHelper.getBigNodeProcessID(senderID);
        
        BrainAreaLog area = logConfig.getAreaLog(sender);
        
        if(area != null){
        
            String logString = new String(data).trim();
            String info[] = logString.split(",");
            int spikeType = Integer.parseInt(info[0]);
            
            if(spikeType != SpikeNames.Debug){
                panel.addEvent(area);
                //brainPanel.addEvent(sender);
            
                String spikeName = logConfig.getSpikeName(spikeType);
                String logLine = logConfig.getAreaNameString(sender)+" send spike type "+spikeName+" to "+logConfig.getAreaNameString(Integer.parseInt(info[1]));
                System.out.println(logLine);
                frame.addEvent(logLine);
            }
        
        }
    }
        
}
