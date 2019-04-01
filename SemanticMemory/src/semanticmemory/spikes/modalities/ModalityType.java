/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

/**
 *
 * @author Luis
 *
 * BASIC_ATT: Representa información que ayuda en el proceso de reconocimiento
 * CLEAN: 
 * ENCODING: Etiqueta de codificación
 * NEW_CODE: Etiqueta de nuevo código
 * NEW_OBJ: Es el identificador de modalidad, significa que el paquete contiene la ubicación de un nuevo objeto
 * OBJ_ANA: Análisis del objeto
 * OBJ_ENC: Es la señal para iniciar la codificación
 * OBJ_EVA: Envía una evaluación del objeto
 * OBJ_PRE: Contiene una predicción del objeto
 * OBJ_RE: Contiene las relaciones de un objeto
 */
public enum ModalityType {
    DEFAULT,
    BASIC_ATT,
    CLEAN,
    ENCODING,
    NEW_CODE,
    NEW_OBJ,
    OBJ_ANA,
    OBJ_ENC,
    OBJ_EVA,
    OBJ_PRE,
    OBJ_RE,
}
