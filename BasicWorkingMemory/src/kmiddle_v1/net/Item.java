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

import kmiddle.utils.fields.NullValueConstants;

public class Item implements NullValueConstants{

	/*
    private int key = NULL_INT;
    private int value = NULL_INT;
	private int bits = 0;

    public Item(int key,int value) {
        this.key = key;
        this.value = value;
		
		bits+= (this.key==NULL_INT)?0:2;
		bits+= (this.value==NULL_INT)?0:1;
    }
	
    @Override
    public boolean equals(Object obj) {
		if(obj.getClass()==Item.class){
			Item t = (Item)obj;
			switch(Math.min(getBits(), t.getBits())){
				case 1: return t.getValue()==value;
				case 2: return t.getKey()==key;
				case 3: return t.getKey()==key&& t.getValue()==value;
			}
			return t.getValue()==value;
		}
        return false;
    }

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + this.key;
		hash = 11 * hash + this.value;
		return hash;
	}    

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "\""+key+":\""+value+"\"";
    }
	
	public int getBits(){
		return bits;
	}
	*/
}
