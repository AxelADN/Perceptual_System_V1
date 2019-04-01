/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

/**
 *
 * @author Luis
 */
public class AMYModality extends SimpleModality{
    
    public static enum EvaluationType{POSITIVE,NEGATIVE}
    
    private EvaluationType evaluation;
    
    public AMYModality(int objectId,EvaluationType evaluation){
        setModalityType(ModalityType.OBJ_EVA);
        setObjectId(objectId);
        setEvaluation(evaluation);
    }

    /**
     * @return the evaluation
     */
    public EvaluationType getEvaluation() {
        return evaluation;
    }

    /**
     * @param evaluation the evaluation to set
     */
    public void setEvaluation(EvaluationType evaluation) {
        this.evaluation = evaluation;
    }
    
}
