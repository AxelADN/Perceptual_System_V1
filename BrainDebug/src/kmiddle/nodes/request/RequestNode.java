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


import kmiddle.utils.fields.NullValueConstants;

public class RequestNode implements NullValueConstants{

	private int count = NULL_INT;
	/**
	 * Nombre del nodoX que busca al nodoY
	 */
    private int node = NULL_INT;
	/**
	 * Nombre del nodoY que es buscado por el nodoX
	 */
    private int needed = NULL_INT;
	
    private byte[] data;
	
	private short dataType = NULL_INT;
	
    private int bits=0;

    
	public RequestNode(int count,int node,int needed) {
		this.count = count;
        this.node = node ;
        this.needed = needed;
      
		bits+= (this.count == NULL_INT)?0:4;
        bits+= (this.node == NULL_INT)?0:2;
        bits+= (this.needed == NULL_INT)?0:1;  
    }

	public RequestNode(int count, int needed,short dataType, byte[] data){
		this.count = count;
		this.needed = needed;
		this.dataType=dataType;
		this.data = data;
		
		bits+= (this.count == NULL_INT)?0:4;
		bits+= (this.needed == NULL_INT)?0:1;
	}
	
	public RequestNode(int count, int node,int needed,short dataType, byte[] data){
		this.count = count;
		this.node = node;
		this.needed = needed;
		this.dataType=dataType;
		this.data = data;
		
		bits+= (this.count == NULL_INT)?0:4;
		bits+= (this.node == NULL_INT)?0:2;
		bits+= (this.needed == NULL_INT)?0:1;
	}
	/**
	 * 
	 * @return Nombre del nodoX que busca al nodoY
	 */
    public int getNode() {
        return node;
    }
	
	/**
	 * 
	 * @return Nombre del nodoY que es buscado por el nodoX
	 */
    
	public int getCount(){
		return count;
	}
	public int getNeeded() {
        return needed;
    }

    public int getBits() {
        return bits;
    }

//    public int getTypeData() {
//        return typeData;
//    }

    public byte[] getData() {
        return data;
    }

	public short getDataType(){
		return dataType;
	}

    @Override
    public boolean equals(Object obj) {
		if(obj.getClass()==RequestNode.class){
			RequestNode r = (RequestNode) obj;
			switch (Math.min(getBits(), r.getBits())) {
			case 1: return case1(r);
			case 2: return case2(r);
			case 3: return case3(r);
			case 4: return case4(r);
			case 5: return case5(r);
			case 6: return case6(r);
			case 7: return case7(r);
			default: return false;
			}
		}
        return false;
    }

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + this.node;
		hash = 97 * hash + this.needed;
		return hash;
	}

    @Override
    public String toString() {
        return node+":"+needed;
    }


    private boolean case1(RequestNode r) {
        return r.getNeeded()==needed;
    }

    private boolean case2(RequestNode r) {
        return r.getNode()==node;
    }

    private boolean case3(RequestNode r) {
        return r.getNode()==node&&
               r.getNeeded()==needed;
    }

	private boolean case4(RequestNode r) {
		return r.getCount()==count;
	}

	private boolean case5(RequestNode r) {
		return r.getCount()==count &&
				r.getNeeded()==needed;
	}

	private boolean case6(RequestNode r) {
		return r.getCount()==count &&
				r.getNode() == node;
	}

	private boolean case7(RequestNode r) {
		return r.getCount()==count &&
				r.getNode() == node &&
				r.getNeeded() == needed;
	}
	
	public boolean hasData(){
		return dataType!=NULL_INT;
	}

}
