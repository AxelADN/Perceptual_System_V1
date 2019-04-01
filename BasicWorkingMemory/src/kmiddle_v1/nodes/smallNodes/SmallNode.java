package kmiddle.nodes.smallNodes;

import kmiddle.log.NodeLog;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import kmiddle.utils.fields.BigNodeNameConstants;
import kmiddle.utils.fields.DataTypeConstants;

public class SmallNode implements SmallNodeBehavior, BigNodeNameConstants, DataTypeConstants{

	protected SmallNodeSocket socket;
	protected NodeLog log;
	//protected final static Logger logger = Logger.getLogger(SmallNode.class.getName());
	protected final int OBJECTS_LENGHT = 4;

	
	/**
	 * Constructor estandar
	 * @param myName	Mi nombre
	 * @param father	Informacion de mi padre
	 * @param options	Opciones de creacion
	 * @param BigNodeNamesClass		Classe encargado de nombres de BigNode
	 */
	public SmallNode(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass){
		
		log = new NodeLog(this.getClass().getName(), myName, BigNodeNamesClass, options.isDebug());
		socket = new SmallNodeSocket(myName, this, father, log,  BigNodeNamesClass, options);
		socket.startNode();
		
		if ( options.isDev() ){
			log.debug("Options, isdebug: " + options.isDebug() + "\t ispersistent:" + options.isPersistent());
		}
	}
	
	@Override
	public void afferents(int nodeName, byte[] data) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void efferents(int receiver, byte[] data){
		//Validacion nesesaria para identificar el ID de un BigNode y un ID global
		if ( (receiver >> 20) == 0 )
			receiver = receiver << 20;
		socket.efferents(receiver, (short)0, data);
	}


	/**
	 * Regresa el ID del nodo
	 * @return	ID del nodo
	 */
	public int getName(){
		return socket.getName(); 
	}
	
	
	protected boolean isFrom(int senderID, int ID ){
		if ( NodeNameHelper.isBigNode( ID ) ){
			return ID == NodeNameHelper.getNodeID(NodeNameHelper.getBigNodeFromID(senderID),0,0);
		
		}else if ( NodeNameHelper.isSmallNode( ID )){
			
			if ( NodeNameHelper.getSmallNodeIndexFromID(ID) == 0)
				return ID == NodeNameHelper.getNodeID(
								NodeNameHelper.getBigNodeFromID(senderID),
								NodeNameHelper.getTypeFromID(senderID),
								0);
			else
				return ID == senderID;
		}
		return false;
	}
}
