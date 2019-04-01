package kmiddle.net.messages;

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

import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.utils.BinaryHelper;
import kmiddle.utils.fields.IndexConstants;
import kmiddle.utils.fields.NullValueConstants;

public class Message implements IndexConstants, NullValueConstants, OperationCodeConstants{
	
	
	private short type;
	private byte msg[];
	
	
	/**
	 * Constructor que crea un mensaje a partir de una cadena de bytes
	 * @param msg Arreglo de bytes que constituye el mensaje
	 */
	public Message(byte[] msg){
		type = BinaryHelper.byteToShort(msg, 0);
		this.msg = new byte[msg.length - 2];
		System.arraycopy(msg, 2, this.msg, 0, msg.length - 2);
	}
	
	/**
	 * Esta funcion regresa el codigo de operacion del mensaje
	 * @return Codigo de operacion
	 */
	public short getOperationCode(){
		return type;
	}	

	/**
	 * Devuelbe en nombre que se incluye en el mensaje, esta fucion solo
	 * es valida para los mensajes de tipo SingInBigNode, SingInSmallNode y Handshake
	 * @return Nombre del BigNode incluido en el mensaje
	 */
	public int getName(){
		if ( type == SINGIN_BigNode || type == SINGIN_SIBLING || type == SINGIN_CHILD || type == HANDSHAKE || type == SEARCH_NODE_REQUEST || type == SEARCH_MULTICAST || type == FIND_NODE || type == DATA || type == FREE_NODE)
			return BinaryHelper.byteToInt(msg, 0);
		return -1;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getIp(){
		if ( type == SINGIN_BigNode || type == SEARCH_MULTICAST || type == FIND_NODE){
			return new Address(msg, 4).getIp(); 
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	private int getPort(){
		if ( type == SINGIN_BigNode || type == SEARCH_MULTICAST || type == FIND_NODE)
			return new Address(msg, 4).getPort(); 
		return 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public Node getNode(){
		return new Node(getName(), getIp(), getPort());
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getLookedName(){
		if ( type == SEARCH_MULTICAST )
			return BinaryHelper.byteToInt(this.msg, 10);
		return -1;
	}
		
	
	
	/**
	 * 
	 * @return
	 */
	public short getDataType(){
		if( type == DATA )
			return BinaryHelper.byteToShort(msg, 4);
		return 0;
	} 
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] getData() {
		if( type == DATA )
			return BinaryHelper.subByteArray(msg, 6, msg.length - 6);
		return null;
    }
	
	//----------------------------------------------------------------
	//-------- MESSAGE
	//----------------------------------------------------------------
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] getMessage(){
		return BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(type), this.msg);
	}
	
	private static byte[] getMessage(short opCode, int BigNodeName ){
		return  BinaryHelper.mergeByteArrays(
				BinaryHelper.shortToByte(opCode), 
				BinaryHelper.intToByte(BigNodeName) 
	);
	}
	
	
	/**
	 * 
	 * @param opCode
	 * @param BigNodeName
	 * @param IP
	 * @param PORT
	 * @return
	 */
	private static byte[] getMessage(short opCode, int BigNodeName, String IP, int PORT ){
		return  BinaryHelper.mergeByteArrays(
					BinaryHelper.shortToByte(opCode), 
					Node.getNode( new Node(BigNodeName, IP, PORT)) 
		);
	}
	
	
	/**
	 * 
	 * @param BigNodeName
	 * @return
	 */
	public static byte[] getSearchNodeRequest( int BigNodeName ){
		return getMessage(SEARCH_NODE_REQUEST, BigNodeName);
	}
	
	public static byte[] getFreeNodeRequest( int BigNodeName ){
		return getMessage(FREE_NODE, BigNodeName);
	}
	
	
	/**
	 * 
	 * @param BigNodeName
	 * @return
	 */
	public static byte[] getSearchMulticast( int BigNodeName, Address addr, int lookedNode ){
		
		return  BinaryHelper.mergeByteArrays(
					BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(SEARCH_MULTICAST), 
						Node.getNode( new Node(BigNodeName, addr.getIp(), addr.getPort()))
					),
					BinaryHelper.intToByte(lookedNode)
		);
		
	}
	
	
	
	public static byte[] getFindMessage( Node n ){
		return getMessage(FIND_NODE, n.getName(), n.getAddress().getIp(), n.getAddress().getPort());
	}
	
	/**
	 * 
	 * @param BigNodeName
	 * @param IP
	 * @param PORT
	 * @return
	 */
	public static byte[] getSingInBigNodeMessage( int BigNodeName, String IP, int PORT ){
		return getMessage( SINGIN_BigNode , BigNodeName, IP, PORT);
	}
	
	/**
	 * 
	 * @param BigNodeName
	 * @return
	 */
	public static byte[] getSingInChildMessage( int BigNodeName ){
		return getMessage( SINGIN_CHILD , BigNodeName );
	}

	/**
	 * 
	 * @param BigNodeName
	 * @param IP
	 * @param PORT
	 * @return
	 */
	public static byte[] getHandShakeMessage( int BigNodeName){
		return getMessage( HANDSHAKE, BigNodeName);
	}
	
	/**
	 * @param senderID
	 * @param dataType
	 * @param data
	 * @return
	 */
	
	public static byte[] getDataMessage( int senderID,  short dataType, byte[] data){
		return BinaryHelper.mergeByteArrays(
					BinaryHelper.shortToByte( DATA ),
					BinaryHelper.mergeByteArrays(
						BinaryHelper.intToByte(senderID),
						BinaryHelper.mergeByteArrays(
								BinaryHelper.shortToByte(dataType), 
								data
						)
					)
		);
	}
	
	
	
	
	/**
	 ******** Deprecado en la actualizacion 1.1 *********************
	
	public byte[] getSecondMessage(int nodeName){
		byte[] a = copyByteArrays(getShortArray(SECOND_DATA), getIntArray(nodeName));
		byte[] b = copyByteArrays(a, getShortArray(getDataType()));
		return copyByteArrays(b, getData());
	}
		
	public static byte[] getMessage(short opCod){
		return getShortArray(opCod);
	}
	
	public static byte[] getMessage(short opCod,Node node){
		return copyByteArrays(getShortArray(opCod), Node.getNode(node));
	}
	
	public static byte[] getMessage(short opCod,short dataType, byte[] data){
		byte[] a = copyByteArrays(getShortArray(opCod), getShortArray(dataType));
		return copyByteArrays(a, data);
	}
	
	public static byte[] getMessage(short opCod,byte[] data){
		return copyByteArrays(getShortArray(opCod), data);
	}
	
	public static byte[] getMessage(short opCod,int areName,short dataType, byte[] data){
		byte[] a = copyByteArrays(getShortArray(opCod), getIntArray(areName));
		byte[] b = copyByteArrays(a, getShortArray(dataType));
		return copyByteArrays(b, data);
	}
	
	public byte[] getSmallNodeMessage(){ 
		byte[] r = new byte[msg.length-2];
		System.arraycopy(msg, 2, r, 0,msg.length-2) ;
		return r;
	}	
	
	//----------------------------------------------------------------
	//-------- HELP
	//----------------------------------------------------------------
	
	public static byte[] getIntArray(int BigNodeName){
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(BigNodeName);
		return buffer.array();
	}
	
	public static byte[] getShortArray(short opeCod){
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(opeCod);
		return buffer.array();  
	}
	
	public int getMissingNodeName(){
		return BigNodeName.getMissingNode(msg);
	}

	public int getSecondName(){
		return BigNodeName.getSecondBigNodeName(msg);
	}

	public byte[] getSecondData(){
		return Data.getSecondData(msg);
	}
	
	
	
	public static byte[] copyByteArrays(byte[] a,byte[] b){
		byte[] c = new byte[a.length+b.length];
		
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	public short getSecondDataType(){
		return DataType.getSecondDataType(msg);
	}
		
	*/

}
