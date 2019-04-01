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

import kmiddle.utils.fields.IndexConstants;

public class Data implements IndexConstants{
	/*
	public static byte[] getData(byte[] msg){
		return getData(I_DATA,msg);
	}

	static byte[] getSecondData(byte[] msg) {
		return getData(I_SECOND_I_DATA,msg);
	}
	
	private static byte[] getData(int index,byte[] msg){
		int offSet;
		switch(index){
			case I_DATA:
				offSet = msg.length-4;
				break;
			case I_SECOND_I_DATA:
				offSet = msg.length- 8;
				break;
			default:
				offSet = 0;
				break;
		}
		byte[] data = new byte[offSet];
		System.arraycopy(msg, index, data, 0, offSet);
		return data;
	}
	*/
}
