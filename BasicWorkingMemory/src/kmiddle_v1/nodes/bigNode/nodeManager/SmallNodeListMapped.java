package kmiddle.nodes.bigNode.nodeManager;

import java.util.Enumeration;

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


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;

import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;

public class SmallNodeListMapped extends SmallNodeList{
	
	 private Hashtable <Double, Node> nodes;
	 private Queue<Double> initializingNodes;


	 /**
	  * 
	  * @param className
	  * @param language
	  * @param options
	  * @param type
	  */
	 public SmallNodeListMapped(String className, int language, NodeConfiguration options, int type) {
	        super(className,language,options, type);
	        nodes = new Hashtable <>();
	        initializingNodes = new LinkedList<Double>();
	 }
	 
	 
	 public void initializing( Double key ){
		 initializingNodes.add(key);
		 
	 }
	 
	 public void add( Node node ){
		 if ( initializingNodes.size() != 0 ){
			 Double key = initializingNodes.poll();  
			 nodes.put(key, node);
		 }
			 
	 }
	 
	 /**
	  * Search for a node, it compairs the FULL ID (BigNode, type AND index)
	  */
	 public boolean contains( int nodeName ){
		 
		for(Entry<Double, Node> entry : nodes.entrySet()){
			if ( entry.getValue().getName() == nodeName ){
				return true;
			}
		}
		return false;		
	 }
	 
	 
	 /**
	  * 
	  * @param key
	  * @return
	  */
	 public Node getNodeByKey(Double key){
		 if ( nodes.containsKey(key) )
			 return nodes.get(key);
		 return null;
	 }
	 
	 
	 public Node getNode( int nodeName ){
		for(Entry<Double, Node> entry : nodes.entrySet()){
			if ( entry.getValue().getName() == nodeName ){
				return entry.getValue();
			}
		}
		return null;
	 }
	 
	 public Node getNode( Address addr ){
		 
		for(Entry<Double, Node> entry : nodes.entrySet()){
			if ( entry.getValue().getAddress().equals(addr)){
				return entry.getValue();
			}
		}
		return null;
	 }
	 
	 public Node getRandomNode(){
		 int size = nodes.size();
		 if ( size == 0 )
			 return null;
		 
		 int n = new Random().nextInt(size);
		 Enumeration<Node> nodeEnumeration =  nodes.elements();
		 for ( int i = 0; i < n; i++)
			 nodeEnumeration.nextElement();
		 
		 return nodeEnumeration.nextElement();
	 }

}
