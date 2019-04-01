package kmiddle.nodes.bigNode;

/**
 * Kuayolotl Middleware System
 * @author Karina Jaime <ajaime@gdl.cinvestav.mx>
 *
 * This file is part of Kuayolotl Middleware
 *
 * Kuayolotl Middleware is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *	
 * Kuayolotl Middleware is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *	
 * You should have received a copy of the GNU General Public License
 * along with Kuayolotl Middleware.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.LinkedList;
import java.util.Queue;

import kmiddle.log.NodeLog;
import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.net.messages.Message;
import kmiddle.net.messages.OperationCodeConstants;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.Nodeable;
import kmiddle.utils.Languages;
import kmiddle.utils.NodeNameHelper;
import kmiddle.utils.fields.BigNodeNameConstants;
import kmiddle.utils.fields.DataTypeConstants;

public abstract class BigNode implements Nodeable, 
	BigNodeNameConstants ,DataTypeConstants, OperationCodeConstants, Runnable{

	protected BigNodeSocket socket ;
	protected NodeLog log;
	protected Class<?> namer;
	
	
	/**
	 * Constructor de la clase
	 * @param name ID unico del nodo
	 */
	public BigNode(int name) {
		this(name, new NodeConfiguration(), null);
	}
	
	/**
	 * Constructor de la clase
	 * @param name ID unico del nodo
	 * @param namer Clase que contien los nombres de las zonas
	 */
	public BigNode(int name, Class<?> namer) {
		this(name, new NodeConfiguration(), namer);
	}
	
	/**
	 * Sirve para indicar opciones de inicializacion 
	 * @param name ID unico del nodo
	 * @param OPTIONS
	 * @param BigNodeNamesClass Clase que contien los nombres de las zonas
	 */
	public BigNode(int name, NodeConfiguration OPTIONS , Class<?> BigNodeNamesClass){
		this(name, null, OPTIONS, BigNodeNamesClass);
	}
	
	
	/**
	 * Sirve para indicar opciones de inicializacion para un subNodo
	 * @param name ID unico del nodo
	 * @param father Nodo que contiene la informacion del nodo padre en caso de tenerlo
	 * @param options
	 * @param namer Clase que contien los nombres de las zonas
	 */
	public BigNode( int name, Node father, NodeConfiguration options, Class<?> namer){
		
		//Es necesario para transformar el index de BigNode un ID de big node
		if ( (name >> 20) == 0)
			name = (int)(name << 20);
		try {
			this.namer = namer;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		log = new NodeLog(this.getClass().getName(), name, this.namer, options.isDebug());
		socket = new BigNodeSocket(name,this, options, father, log,namer);
		socket.startNode();
		
		try{
			Thread.sleep(1000);
			init();
		} catch(Exception e){
			log.error(e.getMessage());
		}
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
	
	
	
	
	protected void efferents(int nodeName, byte[] data){
		//Validacion nesesaria para identificar el ID de un BigNode y un ID global
		if ( (nodeName >> 20) == 0 )
			nodeName = nodeName << 20;
		efferents( nodeName, OperationCodeConstants.DATA , data);
	}
	
	/**
	 * Envia un paquete de datos
	 * @param nodeName ID del receptor
	 * @param dataType Tipo de dato enviado
	 * @param data Cadena binaria de datos
	 */
	protected void efferents(int nodeName, short dataType, byte[] data){
		//Validacion nesesaria para identificar el ID de un BigNode y un ID global
		if ( (nodeName >> 20) == 0 )
			nodeName = nodeName << 20;
		socket.efferents( nodeName, dataType, data);
	}
	
	
	
	private Queue<Message> messageQueue = new LinkedList<Message>();
	
	public void processInThread(Message msg){
		synchronized (messageQueue) {
			messageQueue.add(msg);
		}
		
		new Thread(this).start();
		//System.out.println("Adding message:" + BinaryHelper.byteToInt(msg.getData(), 0) );
		//msg = (Message) messageQueue.remove();
		//this.afferents(msg.getName(),msg.getDataType(), msg.getData());
	}

	
	@Override
	public void run() {
		Message msg = null;
		synchronized (messageQueue) {
			if ( !messageQueue.isEmpty() ){
				msg = (Message) messageQueue.remove();
				
			}
		}
		if ( msg != null )
			this.afferents(msg.getName(), msg.getData());
		socket.sendFreeNodeNotification();
		
	}
	
	
	public abstract void init();
	@Override
	public abstract void afferents(int senderID,  byte[] data);
	
	
	/**
	 * Envia un paquete de datos a un hijo, <br>
	 * el nodo sera seleccionado de acuerdo al comportamiento de tipo
	 * @param type		Tipo de nodo al cual se enviara el paquete
	 * @param sender	Direccion de quien envio el paquete
	 * @param dataType 	Tipo de dato que contiene el paquete
	 * @param data		paquete
	 */
	public void sendToChild(int type, int sender, byte[] data) {
		socket.sendToChild(type, sender, data);
	}
	
	/**
	 * Envia un paquete de datos a un hijo mappeado
	 * @param type	 	Tipo de nodo al cual se enviara el paquete	
	 * @param key		Llave del nodo al cual se enviara el paquete
	 * @param sender	Direccion de quien envio el paquete
	 * @param dataType 	Tipo de dato que contiene el paquete
	 * @param data		paquete
	 */
	public void sendToChild(int type, Double key,  int sender,  byte[] data) {
		socket.sendToChild(type, key, sender,  data);
	}
	
	public void sendToChild(int type, int key,  int sender,  byte[] data) {
		sendToChild(type, (double) key, sender,  data);
	}
	
	
	
	
	
	/**
	 * Agrega un nuevo tipo de nodo pequeño a ser manejado por el nodo grande<br>
	 * esta opcion asume que el nuevo tipo de nodo sera implementado en JAVA y seran nodos persistentes
	 * @param type			Identificador del nodo pequeño a ser agregado
	 * @param className		Ruta al archivo ejecutable del nodo
	 */
	public void addNodeType(int type, Class<?> className){
		NodeConfiguration nc = new NodeConfiguration();
		nc.setPersistent(true);
		socket.smallNodes.addType(type, className.getName(), Languages.JAVA, nc);
	}
	
	
	/**
	 * Agrega un nuevo tipo de nodo pequeño a ser manejado por el nodo grande<br>
	 * esta opcion asume que el nuevo tipo de nodo sera implementado en JAVA
	 * @param type	Identificador del nodo pequeño a ser agregado
	 * @param className	Ruta al archivo ejecutable del nodo
	 * @param Options Opciones para el comportamiento de los nodos
	 */
	public void addNodeType(int type, Class<?>  className, NodeConfiguration options){
		socket.smallNodes.addType(type, className.getName(), options);
	}
				
	/**
	 * Agrega un nuevo tipo de nodo pequeño a ser manejado por el nodo grande <br>
	 * Esta opcion permite especificar el tipo de lenguaje del ejecutable y las opciones para el comportamiento de los nodos
	 * @param type	Identificador del nodo pequeño a ser agregado
	 * @param className	Ruta al archivo ejecutable del nodo
	 * @param languaje	Lenguaje de programacion del ejecutable, se requeier una opcion de kmiddle.utils.Languages
	 * @param Options Opciones para el comportamiento de los nodos
	 */
	
	
	public void addNodeType(int type,String  className, int languaje){
		NodeConfiguration nc = new NodeConfiguration();
		nc.setPersistent(true);
		socket.smallNodes.addType(type, className, languaje, nc);
	}
	
	public void addNodeType(int type,String  className, int languaje, NodeConfiguration Options){
		socket.smallNodes.addType(type, className, languaje, Options);
	}
	
	/**
	 * Fija el numero de nodos maximo que puede tener una tipo de nodo
	 * @param type	Indice del tipo de nodo
	 * @param max	Valor maximo de nodos que puede tener
	 */
	public void setNodeTypeMaxNodes(int type, int max){
		socket.smallNodes.setMaxNodes(type, (short)max);
	}
	
	
	/**
	 * Crea un nodo del tipo indicado
	 * @param type 	Indice del tipo de nodo a ser creado
	 */
	public void createNode(int type){
		createNode(type, 1);
	}

	
	@Override
	public void createNode(int type, int n ){
		socket.smallNodes.createNode(type, new Node(getName(), getAddress()), namer, n);
	}
	
	/**
	 * Crea un nodo mapeado del Tipo indicado, en caso de que el tipo no sea mapeado, 
	 * el metodo no realizara ninguna acción 
	 * @param type		Tipo de nodo a ser creado
	 * @param key		Indice del nuevo nodo
	 */
	public void createMappedNode(int type, Double key){
		socket.smallNodes.createMappedNode(type, key, new Node(getName(), getAddress()), namer, new NodeConfiguration(), 1);
	}
	
	public void createMappedNode(int type, int key){
		createMappedNode(type, (double)key);
	}
	
	/**
	 * Obtiene el ID del nodo
	 * @return ID del nodo
	 */
	public int getName(){
		return socket.getName();
	}
	
	
	/**
	 * Obtiene un objeto c 	on la direccion y puerto de escucha del nodo
	 * @return Objeto Addres del nodo
	 */
	public Address getAddress(){
		return socket.getAddress();
	}

}
