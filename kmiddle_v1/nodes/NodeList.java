package kmiddle.nodes;

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

import java.util.ArrayList;

import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.utils.fields.NullValueConstants;

public class NodeList implements NullValueConstants {
    protected ArrayList<Node> nodeList;
    
	protected int lastIndex = 0;
    public NodeList() {
        nodeList = new ArrayList<>();
    }

    public void add(int nodeName,String host,int nodePort) {
        nodeList.add(new Node(nodeName,host,nodePort));
    }

    public void add(int nodeName) {
		nodeList.add(new Node(nodeName,NULL_STRING,NULL_INT));
    }

	public void add(int nodeName,Address addr){
		nodeList.add(new Node(nodeName,addr));
	}
	
    public void add(Node n) {
        nodeList.add(n);
    }

	public boolean contains(int nodeName){
		return nodeList.contains(new Node(nodeName));
	}
    
	public boolean contains(Node n) {
        return nodeList.contains(n);
    }

    public void remove(Node e) {
        while (nodeList.contains(e)) {
            nodeList.remove(e);
        }
    }
	
	public int getSize(){
		return nodeList.size();
	}
	
	public Node getAndRemove(){
		return nodeList.remove(0);
	}

    public Node getNode(int name) {
        Node n = new Node(name);
		Node node;
        for (int i = 0; i < nodeList.size(); i++) {
			node = nodeList.get(i);
            if (node.equals(n)) {
				return node;
			}
        }
        return null;
    }

	/**
	 * Regresa el nodo registrado en la ip y puerto dado
	 * @param addr
	 * @return Regresa el nodo que pertenece a esa direccion.
	 */
    public Node getNode(Address addr) {
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            //if (node.getHost().compareTo(addr.getIp())==0 && node.getPort()==addr.getPort()) {
			if(node.getAddress().equals(addr)){
                return node;
            }
        }
        return null;
    }
    
	public int getNodeName(Address addr) {
		Node node;
        for (int i = 0; i < nodeList.size(); i++) {
            node = nodeList.get(i);
            //if (node.getHost().compareTo(ip)==0 && node.getPort()==port) {
			if(node.getAddress().equals(addr)){
                return node.getName();
            }
        }
        return NULL_INT;
    }
	
	public Node getNodeAt(int i){
		return nodeList.get(i);
	}
	
	
		
	public int getNextNodeIndex(){
		return ++lastIndex;
	}
	
	@Override
	public String toString(){
		return nodeList.toString();
	}
	
	
	public static void main(String args[]){
		NodeList list = new NodeList();
		Node n1 = new Node(1,"1.1.1.1",20);
		Node n1C = new Node(1);
		Node n2 = new Node(1,"1.1.1.1",201);
		list.add(n1);
		list.add(2);
		
		list.remove(n2);
		System.out.println(list.contains(n1C));
		//
		//System.out.println(list.contains(n2));
	}
}