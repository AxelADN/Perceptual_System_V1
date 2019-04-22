/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import perception.config.AreaNames;
import perception.nodes.smallNodes.CandidatesBuffer.*;
import perception.nodes.smallNodes.CandidatesPrioritazer.*;
import perception.nodes.smallNodes.ComponentClassifier;

import perception.templates.AreaTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class ITp_c extends AreaTemplate {
    
    public ITp_c() {
        this.ID = AreaNames.ITp_c;
        addProcess(CandidatesPrioritizer_fQ1.class);
        addProcess(CandidatesPrioritizer_fQ2.class);
        addProcess(CandidatesPrioritizer_fQ3.class);
        addProcess(CandidatesPrioritizer_fQ4.class);
        addProcess(CandidatesPrioritizer_pQ1.class);
        addProcess(CandidatesPrioritizer_pQ2.class);
        addProcess(CandidatesPrioritizer_pQ3.class);
        addProcess(CandidatesPrioritizer_pQ4.class);
        addProcess(CandidatesBuffer_fQ1.class);
        addProcess(CandidatesBuffer_fQ2.class);
        addProcess(CandidatesBuffer_fQ3.class);
        addProcess(CandidatesBuffer_fQ4.class);
        addProcess(CandidatesBuffer_pQ1.class);
        addProcess(CandidatesBuffer_pQ2.class);
        addProcess(CandidatesBuffer_pQ3.class);
        addProcess(CandidatesBuffer_pQ4.class);
        addProcess(ComponentClassifier.class);
        
        
        
    }
    
    @Override
    public void init() {
        SimpleLogger.log(this, "ITp_c: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
         
    }
    
}
