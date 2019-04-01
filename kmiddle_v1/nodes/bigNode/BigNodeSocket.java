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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kmiddle.log.NodeLog;
import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.net.messages.Message;
import kmiddle.net.messages.OperationCodeConstants;
import kmiddle.net.multicast.Multicast;
import kmiddle.net.p2p.P2PConection;
import kmiddle.net.p2p.P2Pable;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.NodeList;
import kmiddle.nodes.Nodeable;
import kmiddle.nodes.bigNode.nodeManager.SmallNodeManager;
import kmiddle.nodes.request.RequestList;
import kmiddle.nodes.request.RequestNode;
import kmiddle.utils.NodeNameHelper;
import kmiddle.utils.fields.NullValueConstants;

public class BigNodeSocket  implements P2Pable,  
		  OperationCodeConstants, NullValueConstants{
	/**
	* Identificador del BigNode.
	*/
	protected int myName;
	/**
	* DireccioXn del BigNode.
	*/
	protected Address myAddress;
	
	/**
	 * Opciones de configuracion para el BigNode
	 */
	protected NodeConfiguration OPTIONS;
	
	protected Node father;
	
	
	/**
	* Hilo del BigNode.
	*/
	protected P2PConection listeningThread;
	/**
	* Socket del BigNode.
	*/
	protected DatagramSocket datagramSocket;
	
	/**
	* Paquete que es enviado por la red.
	*/
	protected DatagramPacket datagramPacket;
	
	/**
	 * Clase que recivira los mensajes de entrada de tipo dato
	 */
	protected Nodeable behavior;								
	
	/**
	 * Almacena la IP/puerto de los otros BigNodes
	 */
	protected NodeList BigNodes;								

	/**
	 * Almacena los SmallNode disponibles.
	 */
	protected NodeList freeSmallNodes; 							

	/**
	 * Almacena los mensajes que no se han enviado por falta de direccion de destino
	 */
	protected RequestList waitListMessages ; 					

	/*
	 * Almacena los datos de los SmallNodes.
	 */
	protected SmallNodeManager smallNodes;
	
	
	protected NodeList siblings;
	
	/**
	* Da formato a los mensajes que informan de las actividades del sistema. 
	*/
	protected NodeLog log;
	/**
	* Administra la comunicacion que se distribuye en la red mediante mensajes multicast. 
	*/
	protected Multicast multiCast;
	
	protected Class<?> namer;
	
	/**
	* Cantidad maxima de SmallNodes disponibles que se almacenaran. 
	*/
	protected final int MAX_AVAILABLE_NODE_LENGTH = 20;
	
	/**
	*  Cantidad maxima de SmallNodes trabajando en el  sistema. 
	*/
	protected final int MAX_NOAVAILBLE_NODE_LENGTH= Integer.MAX_VALUE;
	
	

	
	/**
	 * Este constructor almacena el nombre y la funcion que sera desencadenada al llegar un mensaje
	 * @param myName	ID unico del nodo
	 * @param behavior	Clase que se encarga del recivir los mensjaes de tipo Data, debe implementar la interface Nodeable
	 */
	public BigNodeSocket(int myName, Nodeable behavior, NodeLog log, Class<?> namer){
		this(myName, behavior, new NodeConfiguration(), log, namer);
	}	
		
	
	/**
	 * Este constructor almacena el nombre y la funcion que sera desencadenada al llegar un mensaje
	 * @param myName	ID unico del nodo
	 * @param behavior	Clase que se encarga del recivir los mensjaes de tipo Data, debe implementar la interface Nodeable
	 * @param OPTIONS	Opciones para el comportamiento del nodo
	 */
	public BigNodeSocket(int myName, Nodeable behavior, NodeConfiguration OPTIONS, NodeLog log, Class<?> namer){
		this(myName, behavior, OPTIONS, null, log, namer);
	}
		
		
	/**
	 * Este constructor almacena el nombre y la funcion que sera desencadenada al llegar un mensaje
	 * @param myName	ID unico del nodo
	 * @param behavior	Clase que se encarga del recivir los mensjaes de tipo Data, debe implementar la interface Nodeable
	 * @param OPTIONS	Opciones para el comportamiento del nodo
	 * @param father 	Informacion del nodo Padre
	 */
	public BigNodeSocket(int myName, Nodeable behavior, NodeConfiguration OPTIONS, Node father, NodeLog log, Class<?> namer){
		this.myName = myName;
		this.behavior = behavior;
		this.OPTIONS = OPTIONS;
		this.father = father;
		this.log = log;
		this.namer = namer;
		freeSmallNodes = new NodeList();
		BigNodes = new NodeList();
		siblings = new NodeList();
		smallNodes = new SmallNodeManager(OPTIONS);
		waitListMessages = new RequestList();
		multiCast = new Multicast(this, this.log, OPTIONS);
	}
	
	
	
	//----------------------------------------------------------------------------------------------
	/**
	* Inicia el nodo. 
	* Inicia la comunicacion multicast.
	*/
	public void startNode() {
		
		/* Inicializa el puerto y lo pone en modo de escucha */
        waitConnections();
        log.header(myName, myAddress);
        
		multiCast.joinGroup();
		
		/* Envia un mensaje de union a los vecinos*/
		log.developer("[Sending SingInMulticastMessage]");
		multiCast.send(Message.getSingInBigNodeMessage(myName,  myAddress.getIp(),  myAddress.getPort()));
    }
	
	/**
     * Inicialia el hilo que escucha las peticiones en la red.
	 * Inicia la comunicacion multicast. ???
     */
	@Override
	public void waitConnections() {
		myAddress = new Address();
		/* Se abre un puerto */
		try{    
			datagramSocket = new DatagramSocket();
			myAddress.setPort(datagramSocket.getLocalPort());									//Consige numero de puerto dinamicamente
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
				String ip = addr.getHostAddress();
				
				if(ip.equals("0.0.0.0") || ip.equals("127.0.1.1")) {							//Cambia la IP a un formato estandar, esto puede variar debido al SO
					ip = "127.0.0.1";
				}
				myAddress.setIp(ip);
			} catch (UnknownHostException ex) {
				log.error(ex.getMessage());
			}
		} catch (SocketException socketException) {
			log.error(socketException.toString());
		}
		
		listeningThread = new P2PConection(this, datagramSocket);					/* Se este objeto(para recivir el mensaje) y se envia el puerto a un hilo de escucha */
		listeningThread.start();
	}	
	
	/**
	 * Detiene el hilo de Escucha.
	 */
	@Override
	public void stopListening() {
		listeningThread.stopThread();
	}
	

	//----------------------------------------------------------------------------------------------
	
	/**
	 * FuncioXn que permite enviar mensajes datos a otro BigNode en la red. 
	 * @param receiver	ID del nodo que recivira el mensaje
	 * @param dataType	Tag para identificar la data
	 * @param data 		Mensaje codificado como adena de bytes
	 */
	public void efferents(int receiver, short dataType, byte[] data) {
		
		if ( (receiver >> 20) == 0)																//Es necesario para transformar el indice del BigNode un ID de big node
			receiver = (int)(receiver << 20);
		
		
		if ( NodeNameHelper.isBigNode(receiver) ){												//Si la direccion de envio es un nodo grande
			
			if (BigNodes.contains(receiver)) {													//Si es un nodo conocido obtengo su direccion y lo envio
				Node n = BigNodes.getNode(receiver);
				log.send(receiver, DATA, "");
				send(n.getAddress(),Message.getDataMessage(myName,  dataType,data));
			
			} else {																			//En caso de que la direccion del nodo no se conosca, nesesito obtenerla,Se procede a enviar un mensaje para buscar el NODO requrido, El paquete que lo nesesitava, se almacena en una lista de mensajes en espera
				//System.out.println("[ receiver no encontrado ]");
				waitListMessages.add(receiver, dataType,data);
				multiCast.send(Message.getSearchMulticast(myName, myAddress, receiver));
			}
			
		}else{																					//Si el nodo buscado es un littleNode
			
			//Si es un nodo conocido obtengo su direccion y lo envio
			if (smallNodes.contains(receiver)) {
				Node n = smallNodes.getNode(receiver);
				send(n.getAddress(),Message.getDataMessage(myName,  dataType,data));
				log.send(receiver, DATA, "");
				
				
			} 
		}
	}
	
	/**
	 * Esta funcion recive los paquetes de datos entrantes, esta funcion
	 *  NESESITA ser implementada por el desarrollador
	 * @param ID	 Identificador del nodo que lo envio
	 * @param dataType	Tipo de dato enviado
	 * @param data	Cadena binaria con los datos
	 */
	public void afferents(Message msg){
		log.receive(msg.getName(), "");
		behavior.processInThread(msg);
	}
	
	/**
	* Envia informacion y datos a traves de la red. 
	* @param addr	Direccion a la cual se enviaran los datos
	* @param data Cadena binaria con los datos
	*/
	@Override
	public void send(Address addr, byte[] data) {	
		try {
			datagramPacket = new DatagramPacket(data, data.length,
					InetAddress.getByName(addr.getIp()),addr.getPort());
			datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
        } catch (Exception e){
        	System.out.println(e.getMessage());
        }
    }
	
	public void sendToChild(int type, int sender, byte[] data) {
		sendToChild(type, -1.0, sender, data);
	}
		
	public void sendFreeNodeNotification(){
		if ( OPTIONS.ischild() ){
			if ( !OPTIONS.isPersistent()){ 				//Si no es persistente
				send(father.getAddress(), Message.getFreeNodeRequest(myName));
			}
		}
	}
		
		
		
	public void sendToChild(int type, Double key, int sender, byte[] data) {
		
		if ( (sender >> 20) == 0)																//Es necesario para transformar el indice del BigNode un ID de big node
			sender = (int)(sender << 20);																		//Si el nodo buscado es un littleNode
		
		Node receiver = null;
		if ( key == -1 ){
			receiver = smallNodes.getFreeNode(type);
		}else{
			receiver = smallNodes.getMappedNode(type, key);
		}

		
		if ( receiver != null ) {
			log.developer("se cuenta con un hijo para recibir el paquete de datos");
			send(receiver.getAddress(),Message.getDataMessage(sender,  OperationCodeConstants.DATA ,data));
		}else{
			if ( key == -1 ){
				log.developer("No se cuenta con un hijo para recibir el paquete de datos, encolando paquete, creando hijo...");
				waitListMessages.add(sender, type, OperationCodeConstants.DATA ,data);
				if ( smallNodes.isValidCreate(type) ){
					smallNodes.createNode(type, new Node(getName(), getAddress()), namer, 1);
				}
			}
		}
	}
	
	
	
	/**
     * Recibe informacion de la red. 
	 * @param datagramPacket
     */
	@Override
	public void receive(DatagramPacket datagramPacket) {
		processDatagramPacket(datagramPacket);
	}
	
	
	/**
	 * Recibe el paquete de la red y obtiene su direccion. 
	 * @param packet 
	 */
	@Override
	public void processDatagramPacket(DatagramPacket packet) {
		String host = packet.getAddress().toString().substring(1);
		int nodePort = packet.getPort();
		byte[] data = new byte[packet.getLength()];
		System.arraycopy(packet.getData(),0, data, 0, packet.getLength());
		
		Message msg = new Message(data);
		processMessage(new Address(host,nodePort), msg);
	}
	
	
	
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Maneja la informacion que recibe de los nodos. El tratamiento que se le da al paquete es 
	 * en base al codigo de operacion que tiene en la cabecera.
	 * @param addr	Direccion de llegada del mensaje
	 * @param msg 	Objeto Mensaje
	 */
	@Override
	public void processMessage(Address addr, Message msg){
		int opcod = msg.getOperationCode();
        switch (opcod) {
			case DATA:
	            data(addr,msg);
	            break;	
			case FIND_NODE:
				findNode(msg);
				break;
			case FREE_NODE:
				freeNode(addr, msg);
				break;
			case HANDSHAKE:
	            handShake(addr, msg);
	            break;
			case SEARCH_NODE_REQUEST:
	            searchNodeRequest(addr, msg);
	            break;
	        case SINGIN_CHILD:
	            singInChild(addr, msg.getName());
	            break;
	        case SINGIN_BigNode:
	        	singInBigNode(msg, true);
	        	break;
	        case SEARCH_MULTICAST:
	        	searchMulticast(msg);
	        	break;
        }
	}
	//----------------------------------------------------------------------------------------------
	
	/**
	 * El paquete que llego contiene datos que deben de ser procesados por los smallnodes.
	 * @param addr	Direccion del nodo que lo envia
	 * @param msg 	Mensaje
	 */
	int i = 0;
	private void data(Address addr, Message msg) {
		afferents(msg);
		//afferents(msg.getName(), msg.getDataType(), msg.getData());
	}
	
	
	private void freeNode(Address addr, Message msg) {
		int nodeID =  msg.getName();
		int nodeType = nodeID & (NodeNameHelper.BigNodeMASK & NodeNameHelper.TYPEMASK);
		log.receive_debug(msg.getName(), FREE_NODE, "Se cuenta con hijo libre");
		
		if(waitListMessages.contains(nodeType)){
			sendWorkToNode(nodeType, new Node(nodeID, addr));
		}else{
			smallNodes.receiveFreeMessage(nodeID, addr);
		}
		
	}
	
	
	/**
	 * Un nodo recivio mi mensaje de SINGING, me regrese su informacion para conocernos mutuamente
	 * @param addr	Direccion de llegada del mensaje 
	 * @param msg	Mensaje
	 */
	private void handShake(Address addr, Message msg) {
		log.receive_debug(msg.getName(), HANDSHAKE, "Handshake" );
		
		Node n = new Node(msg.getName(), addr);
		BigNodes.add( n );
		 
		if(waitListMessages.contains(n.getName())){
			sendToRequestNodes(n);
		}
	}
	
	
	/**
	 * Esta funcion procesa la llegada de un mensaje de busqueda por multicast, donde este nodo es el nodo que esta siendo buscado
	 * @param msg Mensaje de solicitud
	 */
	private void searchMulticast( Message msg ) {
		
		Node n = msg.getNode();															//Agregar al nodo que me busca
		log.developer("[Enviando FIND_MESSAGE a: " + NodeNameHelper.getNameAsString(namer, n.getName()) + "]");
		send(n.getAddress(), Message.getFindMessage(new Node(myName, myAddress)));											//Enviarle un mensaje FIND con mi direccion
	}
	
	
	
	
	/**
	 * El BigNode recibe una peticion para buscar un nodo.
	 * @param addr
	 * @param msg 
	 */
	private void searchNodeRequest(Address addr, Message msg){
		int missingNode = msg.getName();
		Node searcherNode = smallNodes.getNode(addr);
		if(searcherNode != null){
			
			
			log.developer("[Se recivio una solicitud para buscar a "+ NodeNameHelper.getNameAsString(namer, missingNode) + ", de parte de: " + NodeNameHelper.getNameAsString(namer, searcherNode.getName()) + "]");

			// The searched node is an smallNode
			if(NodeNameHelper.isSmallNode(missingNode)){
				
				//The SMALLNodeID part is 0, so is asking for the type
				if ( (missingNode & NodeNameHelper.SMALLNODEMASK) == 0){
					Node mss = smallNodes.getFreeNode(  missingNode );
					send(addr,Message.getFindMessage( mss ));
					//Add the node to the free queue
					smallNodes.receiveFreeMessage(mss.getName(),mss.getAddress());
					
					
				//Is asking for a specific node
				}else if ( smallNodes.contains(missingNode) ){
					
					log.developer("[El nodo buscado era un smallNode, enviando FIND_MESSAGE a: " + NodeNameHelper.getNameAsString(namer, searcherNode.getName()) + "]");
					Node mss = smallNodes.getNode(missingNode);
					send(addr,Message.getFindMessage( mss ));
					
				} else{
					log.developer("[El nodo buscado era un smallNode no registrado, almacenando menaje]");
					waitListMessages.add(searcherNode.getName(), missingNode);
				}
				
			} else{
				if(BigNodes.contains(missingNode)){
					log.developer("[El nodo buscado era un BigNode, enviando FIND_MESSAGE a: " + NodeNameHelper.getNameAsString(namer,searcherNode.getName()) + "]");
					send(addr, Message.getFindMessage( BigNodes.getNode(missingNode) ));
				} else{
					waitListMessages.add(searcherNode.getName(), missingNode);
					log.developer("[El nodo buscado era un BigNode, no se encuentra]");
					
					//Si no soy un subnodo
					if ( !OPTIONS.ischild()){
						log.send_debug(myName, SEARCH_MULTICAST,"");
						multiCast.send(Message.getSearchMulticast(myName,myAddress, missingNode));
					}else{
						send(father.getAddress(),Message.getSearchNodeRequest(missingNode));
					}
				}
			}
		}
	}
	
	
	/**
	 * Se encontro un nodo que se estaba buscando en la red.
	 * @param msg Mensaje
	 */
	private void findNode(Message msg) {
		log.developer("RECIVIDO mensaje FIND_NODE con informacion de:" + NodeNameHelper.getNameAsString(namer, msg.getName()));
		Node n = msg.getNode();
		
		if(NodeNameHelper.isBigNode(n.getName())){
			singInBigNode(msg, false);
		}else{
			if ( NodeNameHelper.getBigNodeFromID(myName)
			     == NodeNameHelper.getBigNodeFromID(n.getName())){						//Misma BigNode deben de ser hermanos
				singInSibling(n.getAddress(), n.getName());
			}
		}
	}
	
	
	/**
	 * Registra los BigNodes con los que se ha comunica el BigNode.
	 * @param addr
	 * @param nodeName 
	 */
	private void singInChild(Address addr,int nodeName ) {
		log.receive_debug(nodeName, SINGIN_CHILD, "SingIn child");
		if(NodeNameHelper.isSmallNode(nodeName)){
			if(!smallNodes.contains(nodeName)){
				log.developer("agregando hijo");
				smallNodes.add(nodeName, addr);
				int nodeType = nodeName & ( NodeNameHelper.BigNodeMASK + NodeNameHelper.TYPEMASK);
				sendToRequestNodes(new Node(nodeType, addr));
				/*
				if(waitListMessages.contains(nodeType)){
					log.developer("Enviando mensajes pendientes a tipo");
					sendWorkToNode(nodeType, new Node(nodeName,addr));
				}else
					log.developer("No habia mensajes pendientes a mensajes pendientes a tipo: " + nodeType);
				*/
					
			}
		}
	}
	
	
	private void singInSibling(Address addr,int nodeName ) {
		log.receive_debug(nodeName, SINGIN_SIBLING, "Sing in sibling");
		if(NodeNameHelper.isSmallNode(nodeName)){
			//temporal.config("[Recivido SingInMessage " + NodeNameHelper.getNameAsString(namer, nodeName) + " " + addr.toString() + "]");
			if(!siblings.contains(nodeName)){
				siblings.add(nodeName, addr);
			}
		}
		
		if(waitListMessages.contains(nodeName)){
			sendToRequestNodes( new Node(nodeName, addr));
		}
	}
	
	
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Un nodo se registro via Multicast.
	 * @param addr	Direccion del nuevo vecino
	 * @param nodeName  Nombre
	 */
	private void singInBigNode(Message msg, boolean handshake) {
		log.receive_debug(msg.getName(), SINGIN_BigNode, "Sing in BigNode");
		int name = msg.getName();
		Address a = msg.getNode().getAddress();
		if(NodeNameHelper.isBigNode(name)){
			Node n = new Node(name, a);
			
			
			
			if ( BigNodes.contains(name) ){
				BigNodes.remove(new Node(name));
			}
			BigNodes.add(n);
			
			if ( handshake ){
				log.send_debug(name, HANDSHAKE, "");
				send(a, Message.getHandShakeMessage(myName));
			}
			
			if(waitListMessages.contains(n.getName())){
				sendToRequestNodes(n);
			}
		}		
	}
	
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Envia las peticiones pendientes que tiene singinNode.
	 * @param singinNode Nodo que acaba de ser agregado a mi lista
	 */
	private void sendToRequestNodes(Node singinNode) {
		ArrayList<RequestNode> nodesName = waitListMessages.getNodeFromNeeded(singinNode.getName());
		RequestNode rn ;
		Node n;
		for (int i=0;i < nodesName.size();i++){
			rn = nodesName.get(i);
			n = smallNodes.getNode(rn.getNode());
			if(n == null){
				
				if(rn.hasData()){
					log.send_debug(singinNode.getName(), DATA, "");
					send(singinNode.getAddress(),Message.getDataMessage(myName, rn.getDataType(),rn.getData()));
				}
				
			}
			else{
				log.send_debug(singinNode.getName(), FIND_NODE, "");
				send(n.getAddress(),Message.getFindMessage( singinNode ));
			}
		}
		waitListMessages.removeAll(nodesName);
	}
	
	
	/**
	 * Envia UN paquete de data pendiente a un nodo para que lo procese
	 * @param singinNode Nodo que acaba de ser agregado a mi lista
	 */
	private boolean sendWorkToNode(int nodeType, Node node) {
		boolean ret = false;
		ArrayList<RequestNode> nodesName = waitListMessages.getNodeFromNeeded(nodeType);
		RequestNode rn ;
		rn = nodesName.get(0);
		if(rn.hasData()){
			log.send(node.getName(), DATA, "");
			send(node.getAddress(),Message.getDataMessage(rn.getNode(), rn.getDataType(),rn.getData()));
			ret = true;
		}
		log.developer("pendientes " + (nodesName.size() -1) + " para el tipo de nodo");
		waitListMessages.remove(rn);
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Regresa el identificador del BigNode.
	 * @return Identificador del BigNode.
	 */
	@Override
	public int getName() {
		return myName;
	}
	
	/**
	 * Regresa la direccion del BigNode.
	 * @return Objeto Address.
	 */
	public Address getAddress(){
		return myAddress;
	}
	
	/**
	 * Regresa la direccion IP del BigNode.
	 * @return Direccion IP
	 */
	public String getIp(){
		return myAddress.getIp();
	}
	
	/**
	 * Regresa el puerto del BigNde.
	 * @return Numero de puerto.
	 */
	public int getPort(){
		return myAddress.getPort();
	}
}
 