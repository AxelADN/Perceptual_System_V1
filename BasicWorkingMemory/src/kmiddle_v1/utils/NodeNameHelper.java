package kmiddle.utils;
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

import java.lang.reflect.Field;

import kmiddle.utils.fields.IndexConstants;

public class NodeNameHelper implements IndexConstants{
	
	public static final int NULL_NAME = 0;
	public static final int BigNodeMASK 		= 0b11111111111100000000000000000000;
	public static final int TYPEMASK 		= 0b00000000000011111000000000000000;
	public static final int SMALLNODEMASK           = 0b00000000000000000111111111111111;
	
	
	/**
	 * El formato de las BigNode esta definido de la siguente manera
	 * BigNodeID  |  SmallNodeType | smallNodeID
	 *  12 Bytes  |     5 Bytes    |  15 Bytes    
	 */
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	/*
	public static int getBigNodeNameFromMessage(byte[] msg){
		ByteBuffer buffer = ByteBuffer.allocate(msg.length);
		buffer.put(msg);
		return buffer.getInt(I_NAME);
	}
	*/
	
	
	/**
	 * <pre>
	 * Valida su el valor del BigNode contiene un smallnode.
	 * El formato de las BigNode esta definido de la siguente manera
	 * 
	 * BigNodeID  |  SmallNodeType | smallNodeID
	 *  12 Bytes  |     5 Bytes    |  15 Bytes
	 *  
	 * Si alguno de los ultimos 20 bits son diferentes de 0, entonses es un smallNode
	 *  </pre>
	 * @param BigNodeName
	 * @return True si es un smallnode, false en caso contrario.
	 */
	public static boolean isSmallNode(int BigNodeName){
		return (BigNodeName << 12) != 0;
	}
	
	
	
	/**
	 * Valida su el valor del BigNode es de un big node.
	 * El formato de las BigNode esta definido de la siguente manera
	 * BigNodeID  |  SmallNodeType | smallNodeID
	 *  12 Bytes  |     5 Bytes    |  15 Bytes
	 * Si los ultimos 20 bits son 0, entonses es unbig node
	 *  
	 * @param BigNodeName
	 * @return True si es un BigNode, false en caso contrario.
	 */
	public static boolean isBigNode(int BigNodeName){
		return (BigNodeName << 12) == 0;
	}
	

	
	
	
	/********** FUNCIONES QUE AYUDAN A OBTENER EL NOMBRE DE BigNodeS **/
	public static String getNameAsString( Class<?> namer, int name ) {
		if ( namer == null )
				return String.valueOf(name);
		String BigNodeName = getBigNodeName(namer, name);
		String typeName = getTypeName(namer, name);
		String index = String.valueOf(name & SMALLNODEMASK);
		if( typeName.equals("") && index.equals("0"))
			return BigNodeName;
		return BigNodeName + "_" + typeName + "_" + index;
	}
	
	
	
	
	public static String getBigNodeName( Class<?> namer, int name ) {
		//Get the part of the ID and get the BigNode Index
		int BigNode = name & BigNodeMASK;
		if ( namer == null )
			return String.valueOf(BigNode);
		
		Field[] fields = namer.getFields();
		for ( int i = 0; i < fields.length; i++){
			if ( !fields[i].getName().contains("_") ){
				
				try{
					if ( BigNode == fields[i].getInt(null) ){
						return fields[i].getName();
					}
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
		return "";
	}
	
	public static String getTypeName( Class<?> namer, int name ){
		if ( namer == null )
			return String.valueOf((name & TYPEMASK) >> 15);
		if ( (name & TYPEMASK) == 0 )
			return "";
		
		
		int type = name & (BigNodeMASK + TYPEMASK);
		Field[] fields = namer.getFields();
		for ( int i = 0; i < fields.length; i++){
			//Little fix for the hardcoded names of the Plugin
			if ( fields[i].getName().contains("_")){
				try{
					if ( type == fields[i].getInt(null) ){
						String n =  fields[i].getName();
						int separatorIndex = n.indexOf("_");
						if ( separatorIndex != -1 )
							return n.substring(separatorIndex + 1);
						return n;
					}
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
		return "";
	}
	
	
	
	public static int getBigNodeFromID(int nodeName){
		return nodeName >> 20;
	}
	
	public static int getTypeFromID(int nodeName){
		return (nodeName & TYPEMASK) >> 15;
	}
	
	public static int getSmallNodeIndexFromID(int nodeName){
		return nodeName & SMALLNODEMASK;
	}
	
        public static int getBigNodeProcessID(int nodeName){
            return nodeName & (BigNodeMASK | TYPEMASK);
        }
        
        public static int getBigNodeID(int nodeName){
            return nodeName & BigNodeMASK;
        }
	
	
	/**
	 * Obtiene el nombre de un Node, se le proporciona el 
	 * @param BigNodeName
	 * @param nodeType
	 * @param nodeIndex
	 * @return
	 */
	public static int getNodeID( int BigNodeName, int nodeType , int nodeIndex ) {
		int r = BigNodeName << 20;
		r += nodeType << 15;
		r += nodeIndex;
		return r;
	}	
}