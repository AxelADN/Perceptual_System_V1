/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import kmiddle2.nodes.activities.ActConf;
import perception.config.AreaNames;
import perception.nodes.smallNodes.BufferSwitch;
import perception.nodes.smallNodes.HolisticClassifier;
import perception.nodes.smallNodes.PreObjectBuffer.*;
import perception.nodes.smallNodes.PreObjectPrioritizer.*;
import perception.nodes.smallNodes.RetroReactiveQueuer;

import perception.templates.AreaTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class ITp_h extends AreaTemplate {

    public ITp_h() {
        this.ID = AreaNames.ITp_h;
        addProcess(BufferSwitch.class, ActConf.TYPE_PARALLEL);
        addProcess(PreObjectPrioritizer_fQ1.class);
        addProcess(PreObjectPrioritizer_fQ2.class);
        addProcess(PreObjectPrioritizer_fQ3.class);
        addProcess(PreObjectPrioritizer_fQ4.class);
        addProcess(PreObjectPrioritizer_pQ1.class);
        addProcess(PreObjectPrioritizer_pQ2.class);
        addProcess(PreObjectPrioritizer_pQ3.class);
        addProcess(PreObjectPrioritizer_pQ4.class);
        addProcess(PreObjectBuffer_fQ1.class);
        addProcess(PreObjectBuffer_fQ2.class);
        addProcess(PreObjectBuffer_fQ3.class);
        addProcess(PreObjectBuffer_fQ4.class);
        addProcess(PreObjectBuffer_pQ1.class);
        addProcess(PreObjectBuffer_pQ2.class);
        addProcess(PreObjectBuffer_pQ3.class);
        addProcess(PreObjectBuffer_pQ4.class);
        addProcess(HolisticClassifier.class);
        addProcess(RetroReactiveQueuer.class,ActConf.TYPE_PARALLEL);

    }

    @Override
    public void init() {
        SimpleLogger.log(this, "ITp_h: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {

    }

}
