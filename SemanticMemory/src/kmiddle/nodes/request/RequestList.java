package kmiddle.nodes.request;


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
import kmiddle.utils.fields.NullValueConstants;


public class RequestList implements NullValueConstants {

    private ArrayList<RequestNode> list;
	private int count =0;
    public RequestList() {
        list = new ArrayList<>();
    }

    public void add(int node,int needed) {
        list.add(new RequestNode(count++,node, needed));
    }

	public void add(int node,short dataType,byte[] data){
		list.add(new RequestNode(count++,node, dataType, data));
	}
	
	public void add(int node,int needed,short dataType,byte[] data){
		list.add(new RequestNode(count++,node,needed, dataType, data));
	}
	
	public void add(RequestNode r){
		list.add(r);
	}
	
    public boolean contains(RequestNode node) {
        return list.contains(node);
    }
	
	public boolean contains(int name) {
        return list.contains(new RequestNode(NULL_INT,NULL_INT, name));
    }

    public void remove(RequestNode e) {
        while (list.contains(e)) {
			list.remove(e);
		}
    }
	
	public void removeAll(ArrayList<RequestNode> nodes){
		list.removeAll(nodes);
	}

    @Override
    public String toString() {
        return list.toString();
    }

	
   

    /**
     * Dado un BigNode que se encontro se regresa los nodos que lo necesitan.
     *
     * @param name
     * @return Regresa los nodos que necesitan a el nodo con el identificador contenido en la
	 * variable name.
     */
    public ArrayList<RequestNode> getNodeFromNeeded(int name) {
        ArrayList<RequestNode> items = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RequestNode node = list.get(i);
            if (node.equals(new RequestNode(NULL_INT,NULL_INT, name))) {
                items.add(node);
            }
        }
        return items;
	}

}
