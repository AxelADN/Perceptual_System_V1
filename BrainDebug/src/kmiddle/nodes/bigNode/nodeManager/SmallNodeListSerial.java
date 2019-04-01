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

import java.util.LinkedList;
import java.util.Queue;

import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.Config;

public class SmallNodeListSerial extends SmallNodeList{
	
	 private short maxNodes = Config.DEFAUTL_MAX_SMALLNODES_SERIAL;

	 protected Queue<Node> freeNodes;
	 

	 public SmallNodeListSerial(String className, int language, NodeConfiguration options, int type) {
	        super(className,language,options, type);
	        freeNodes = new LinkedList<Node>();
	 }
	 
	 public void setMaxNodes( short maxNodes ){
		 this.maxNodes = maxNodes;
	 }
	 
	 
	 public void add( int nodeName, Address addr){
		 Node n = new Node(nodeName, addr);
		 nodeList.add(n);
		 freeNodes.add(n);
	 }
	 
	 
	 
	 /**
	  * Regresa un nodo libre, regresa null en caso de que no exista alguno
	  * @return 
	  */
	 public Node getFreeNode(){
		 if ( options.isPersistent() ){
			 if ( freeNodes.size() == 0 ){
				 return null;
			 }else{
				 return freeNodes.poll();
			 }
		 }
		 return null;
	 }
	 
	 /**
	  * Regresa un nodo libre, regresa null en caso de que no exista alguno
	  * @return 
	  */
	 public Node getFirstNode(){
		 if ( options.isPersistent() ){
			 if ( freeNodes.size() == 0 ){
				 return null;
			 }else{
				 return freeNodes.element();
			 }
		 }
		 return null;
	 }
         
	 public boolean isValidCreate(){
		 if ( options.isPersistent() ){
			 if ( getSize() < maxNodes )
				 return true;
			 else 
				 return false;
		 }
		 return true;
	 }
	 
	 public void receiveFreeMessage(Node n){
		 freeNodes.add(n);
	 }
}