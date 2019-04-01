package kmiddle.nodes.bigNode.nodeManager;

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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.NodeInitializer;
import kmiddle.utils.Languages;
import kmiddle.utils.NodeNameHelper;

public class SmallNodeManager {
	
	private HashMap<Integer, SmallNodeList> lists;
	private String path;
	private NodeConfiguration fatherOptions;
	
	
	public SmallNodeManager(){
		this(new NodeConfiguration());
	}
	
	public SmallNodeManager(NodeConfiguration fatherOptions){
		lists = new HashMap<Integer, SmallNodeList>();
		path = new File("").getAbsolutePath();
		this.fatherOptions = fatherOptions;
	}
	
	
	

	public void addType(int type, String className, NodeConfiguration options){
		addType(type, className, Languages.JAVA , options);
	}
	
	/**
	 * Agrega un nuevo tipo de SMALLNODE para ser manejado por el Big Node
	 * @param className	Clase del smallnode a ser manejado
	 * @param languaje	Lenguaje en el que la clase esta implementado
	 * @param options Options indica la forma en la que este tipo de smallnode debe ser 
	 * manejada, ya sea creando nodos permantentes o espontaneps
	 * @return
	 */
	public void addType(int type, String className, int languaje, NodeConfiguration options){
		//Si el padre esta en debug los nodos nuevos deben de estar en debug
		//if ()
		options.setDebug(this.fatherOptions.isDebug());
		
		
		if ( options.isMapped() ){																//Si es de tipo mapeado
			options.setPersistent(true);
			
			lists.put(type, new SmallNodeListMapped(className, languaje, options, type));
			
		}else{
			lists.put(type, new SmallNodeListSerial(className, languaje, options, type));
		}
	}


	private int getSmallNodeType(int nodeName){
		return nodeName & (NodeNameHelper.BigNodeMASK + NodeNameHelper.TYPEMASK); 
	}
	
	/**
	 * 
	 * @param nodeName
	 * @param addr
	 */
	public void add(int nodeName, Address addr){
		int type = getSmallNodeType(nodeName);
		
		if ( lists.containsKey(type) ) {
			SmallNodeList typeList = lists.get(type);
			if ( typeList == null )
				return;
			NodeConfiguration typeOptions = typeList.getOptions();
			if ( typeOptions.isPersistent() ){							//Si es persistente
			
				if ( typeOptions.isMapped() ){							//Si es mapeado
					((SmallNodeListMapped) typeList).add( new Node(nodeName,addr) );
					
				}else{
					((SmallNodeListSerial) typeList).add(nodeName,addr);
				}
			}
		}
	}
	
	
	/**
	 * Verifica la existencia de un nodo en alguna de las listas
	 * @param nodeName
	 * @return
	 */
	public boolean contains(int nodeName){
		int type = getSmallNodeType(nodeName);
		
		if ( lists.containsKey(type) ) {
			
			SmallNodeList typeList = lists.get(type);
			NodeConfiguration typeOptions = typeList.getOptions();
			if ( typeOptions.isMapped() ){							//Si es mapeado
				return ((SmallNodeListMapped) typeList).contains(nodeName);
				
			}
			else {
				return typeList.contains(nodeName);
				
			}
		}else
			return false;
	}
	
	/**
	 * Obtiene una lista de nodos
	 * @param type Indice del tipo de lista
	 * @return Objeto Lista de nodos pequeños
	 */
	private SmallNodeList getListOfType(int type){
		return lists.get(type);
	}
	
	
	
	/**
	 * Retorna un nodo 
	 * @param nodeName	ID de nodo
	 * @return
	 */
	public Node getNode(int nodeName){
		int type = getSmallNodeType(nodeName);
		
		if ( lists.containsKey(type) ) {
			
			SmallNodeList typeList = lists.get(type);
			NodeConfiguration typeOptions = typeList.getOptions();
			
			if ( typeOptions.isMapped() ){							//Si es mapeado
				return ((SmallNodeListMapped) typeList).getNode(nodeName);
				
			}else{
				return typeList.getNode(nodeName);
				
			}
		}else
			return null;
	}
	
	
	public Node getNode(Address addr){
		Node r = null;
		for(Entry<Integer, SmallNodeList> entry : lists.entrySet()){
			
			if ( entry.getValue().getOptions().isMapped() ){	 //Si es de tipo mapeado
				r = ((SmallNodeListMapped) entry.getValue() ) .getNode(addr);
			
			}else{
				r = entry.getValue().getNode(addr);
			
			}
			if ( r != null)
				break;
		}
		return r;
	}
	
	
	public Node getFreeNode(int nodeType){
            
		SmallNodeList nodeTypelist = getListOfType(nodeType);	
		
                if ( nodeTypelist == null )
			return null;
                
		NodeConfiguration typeOptions = nodeTypelist.getOptions();
		
		if ( typeOptions.isSerial() ){
                    
			SmallNodeListSerial list = (SmallNodeListSerial) nodeTypelist;
                        
                        Node n = null;
                        
                        if(fatherOptions.isLoadBalance()){
                        
                            n = list.getFreeNode();
                            
                        }else{
                            
                            n = list.getFirstNode();
                            
                        }
                        
			return n;
			
		}else{
			SmallNodeListMapped list = (SmallNodeListMapped) nodeTypelist;
			Node n = list.getRandomNode();
			return n;
			
		}
	}
	
	public Node getMappedNode(int nodeType, Double key){
		SmallNodeList nodeTypelist = getListOfType(nodeType);
		if ( nodeTypelist == null )
			return null;
		NodeConfiguration typeOptions = nodeTypelist.getOptions();
		
		if ( typeOptions.isMapped() ){							//Si es de tipo mapeado
			return ((SmallNodeListMapped) nodeTypelist).getNodeByKey(key);
		}
		return null;
	}
	
	
	public boolean isValidCreate(int nodeType){
		SmallNodeList nodeTypelist = getListOfType(nodeType);
		if ( nodeTypelist == null )
			return false;
		NodeConfiguration typeOptions = nodeTypelist.getOptions();
		
		if ( typeOptions.isMapped() ){							//Si no es un tipo de manejo mapeado
			return false;
		}else{
			SmallNodeListSerial list = (SmallNodeListSerial) nodeTypelist;
			return list.isValidCreate();
		}
	}
	
	
	public void setMaxNodes(int type, short max){
		SmallNodeList l =  lists.get(type);
		if ( l instanceof SmallNodeListSerial ) {
			((SmallNodeListSerial) l).setMaxNodes(max);
		}
	}
	
	
	
	public void receiveFreeMessage(int nodeID, Address addr){
		int type = getSmallNodeType(nodeID);
		if ( lists.containsKey(type) ) {
		
			SmallNodeList nodeTypelist = getListOfType(type);	
			NodeConfiguration typeOptions = nodeTypelist.getOptions();
			
			if ( typeOptions.isSerial() ){							//Si no es de tipo mapeado
				((SmallNodeListSerial) nodeTypelist).receiveFreeMessage(new Node(nodeID, addr));
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	public void createMappedNode(int type, Double key,  Node father, Class<?> namer, NodeConfiguration options, int n ){
		SmallNodeList nodeType = getListOfType(type);
		if ( nodeType.getOptions().isMapped() ){							//Si es un tipo de manejo mapeado
			((SmallNodeListMapped) nodeType).initializing(key);
			createNode(type, father, namer, options, n);
		}
	}
	
	
	
	public void createNode(int type, Node father, Class<?> namer, int n ){
		createNode( type, father, namer, new NodeConfiguration(), n );
	}
	
	public void createNode(int type, Node father, Class<?> namer, NodeConfiguration options, int n ){
		
		SmallNodeList nodeType = getListOfType(type);	
                
		String className = nodeType.getClassName();
                
		int lenguaje = nodeType.getLanguage();
		NodeConfiguration typeOptions = nodeType.getOptions();
		
		
		options.setDebug(typeOptions.isDebug());
		options.setChild(true);
		
		if ( typeOptions.isPersistent() )
			options.setPersistent(true);
		
		
		
		
		
		String classNameArgs = 	" --className " + className;
		String fatherArgs = 	" --father "  + father.getName() + " " + father.getHost() + " " + father.getPort() + " ";
		String namerArgs = 		" --namer " + namer.getName();
		String optionsArgs = 	" --options " + options.toInt() + " ";
		
		//The new processName have integrated the BigNodeName
		//prepara type para agregarlo al nombre del nuevo nodo
		/*if ( (type & NodeNameHelper.TYPEMASK ) == 0 )
			type = type << 15;
		*/
		
		
		/*int newNodeNameBase = 
				(father.getName() & NodeNameHelper.BigNodeMASK) + 
				type;
		*/
		
		String command = "";
		
		// Si el lenguaje es  PYTHON
		if (lenguaje == Languages.PYTHON){
			
			String libLocation = "";
			
			String OS = System.getProperty("os.name", "generic").toLowerCase();
			if ((OS.contains("mac") ) || (OS.contains("darwin") )) {
				//command = "java -cp " + path + ":";
				
			} else if (OS.contains("win") ) {
				libLocation = " --libLocation \"" + path + "/libraries/middlewarePython/\" ";
				libLocation += "\"" + path + "/src/\" ";
				
			} else if (OS.contains("nux") ){
				libLocation = " --libLocation " + path.replace(" ", "\\ ") + "/libraries/middlewarePython/ ";
				libLocation += path.replace(" ", "\\ ") + "/src/ ";
			}
			
			 
			
			command = "python ";
			command += "libraries/middlewarePython/SmallNode/SmallNodeInitializer.py ";
			command += libLocation;
			command += classNameArgs;
			command += fatherArgs;
			command += optionsArgs;
			command += namerArgs;
			
			// Ejecuta el comando
			
			if ( options.isDebug() == null )
				System.out.println(command + " --name " + (type + 1));
			
			exec(command + " --name " + (type + nodeType.getNextNodeIndex()), OS);
			
			
		//Si el lenguaje es JAVA
		}else if (lenguaje == Languages.JAVA){
			
		
			 
			// Manipula la direccion para evitar conflictos en diferentes OS
			String OS = System.getProperty("os.name", "generic").toLowerCase();
			if ((OS.contains("mac") ) || (OS.contains("darwin") )) {
				command = "java -cp " + System.getProperty("java.class.path").replace(" ", "\\ ") + " ";
				
			} else if (OS.contains("win") ) {
				command = "java -cp \"" + System.getProperty("java.class.path") + "\" ";
				
			} else if (OS.contains("nux") ){
				command = "java -cp " + System.getProperty("java.class.path").replace(" ", "\\ ") + " ";
			}
			
			command += " " + NodeInitializer.class.getName() + " ";
			command += classNameArgs;
			command += fatherArgs;
			command += optionsArgs;
			command += namerArgs;

			
			// Ejecuta el comando
			
			for ( int i = 0; i < n; i++){
				if ( options.isDebug() == null)
					System.out.println(command + " --name " + (type + 1));
				
				exec(command + " --name " + (type + nodeType.getNextNodeIndex()), OS);
			}
		}
			
		
	}
	
	
	private void exec(String command, String OS){
		try{
			if ((OS.contains("mac") ) || (OS.contains("darwin") )) {
				//Runtime.getRuntime().exec(new String[]{"open", "-a", "terminal " + command});
				final ProcessBuilder processBuilder = new ProcessBuilder(
	                    "/usr/bin/osascript",
	                    "-e", "tell app \"Terminal\"",
	                    "-e", "set currentTab to do script " + "(\"" + command + "\")",
	                    "-e", "end tell");
				processBuilder.start();
				
			} else if (OS.contains("win") ) {
				Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start " + command});
				
			} else if (OS.contains("nux") ){
				Runtime.getRuntime().exec(new String[]{"bash", "-c", "gnome-terminal -x " + command});
			}
		} catch (IOException e) {
			System.out.println("[Error creando SmallNode]");
		}
		
	}
	
}
