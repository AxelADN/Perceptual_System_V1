package kmiddle.net;

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

import kmiddle.utils.BinaryHelper;
import kmiddle.utils.fields.IndexConstants;
import kmiddle.utils.fields.NullValueConstants;


public class Node implements NullValueConstants, IndexConstants {
    protected int name = NULL_INT;
	protected Address address;
    protected int bits=0;
	
	public Node(){
		
	}

    public Node(int name) {
        this.name = name;
		address = new Address();
        bits+= (this.name==NULL_INT)?0:4;
    }

	public Node(int name,String host,int port) {
        this.name = name;
        address = new Address(host, port);
        bits+= (this.name==NULL_INT)?0:4;
        bits+= address.isNullIp()?0:2;
		bits+= address.isNullPort()?0:1;
    }
	
	public Node(int name,Address a) {
        this.name = name;
        address = a;
        bits+= (this.name==NULL_INT)?0:4;
        bits+= address.isNullIp()?0:2;
		bits+= address.isNullPort()?0:1;
    }

    public int getName() {
        return name;
    }

    public String getHost() {
        return address.getIp();
    }

    public int getPort() {
        return address.getPort();
    }

    public int getBits() {
        return bits;
    }

	public Address getAddress(){
		return address;
	}
	
    @Override
    public boolean equals(Object obj) {
		if(obj.getClass()==Node.class){
			Node node = (Node)obj;
			switch (Math.min(getBits(), node.getBits())) {
				case 1: return node.getPort()==getPort();
				case 2: return node.getHost().compareTo(getHost())==0;
				case 3: return node.getHost().compareTo(getHost())==0&&node.getPort()==getPort();
				case 4: return node.getName()==name;
				case 5: return node.getName()==name&& node.getPort()==getPort();
				case 6: return node.getName()==name&& node.getHost().compareTo(getHost())==0;
				case 7: return node.getName()==name&& node.getHost().compareTo(getHost())==0&&
								node.getPort()==getPort(); 
				default: return false;
			}
		}
		return false;
    }

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + this.name;
		hash = 47 * hash + (this.address != null ? this.address.hashCode() : 0);
		hash = 47 * hash + this.bits;
		return hash;
	}

	

   
	public static byte[] getNode(Node n){
		return BinaryHelper.mergeByteArrays(
				BinaryHelper.intToByte(n.getName()),
				n.getAddress().toByteArray()
		);
	}

    @Override
    public String toString() {
        return ""+name+address;
    }

    
    public static String toString(String tNodeName,String tHost,int tPort) {
        return tNodeName+tHost+LIMIT+tPort+LIMIT;
    }
	
	public boolean isNull(){
		return bits==0;
	}

	public void setName(int name){
		this.name = name;		
	}
	
}
