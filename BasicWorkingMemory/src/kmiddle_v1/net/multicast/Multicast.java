package kmiddle.net.multicast;

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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import kmiddle.log.NodeLog;
import kmiddle.net.Address;
import kmiddle.net.messages.Message;
import kmiddle.net.messages.OperationCodeConstants;
import kmiddle.net.p2p.P2Pable;
import kmiddle.nodes.NodeConfiguration;

public class Multicast extends Thread implements OperationCodeConstants{
	
	private MulticastSocket socket;					//Socket que escuha el puerto
	
	private P2Pable BigNode;				//Clase que tiene el comportamiento para recivir mensajes
	
	private InetAddress group;						//Direccion ip del grupo
	
	private Address address;						//Direccion ip del grupo
	
	private NodeLog logger;						
	
	//private NodeConfiguration nc;

	
	/**
	 * Contructor de la clase		
	 * @param BigNode 	Objeto que se encargara de procesar los mensajes
	 * @param temporal	Log
	 */
	public Multicast(P2Pable BigNode, NodeLog temporal, NodeConfiguration nc){
		this.BigNode = BigNode; 
		this.logger = temporal;
		//this.nc = nc;
		address = new Address("228.5.6.7", 6789 + nc.getEntityID());
	}
	
	/**
	 * Esta funcion levanta el puerto de escucha y crea un hilo en segundo plano 
	 * para que este a la espera de mensajes 
	 */
	public void joinGroup(){
		try{
			group = InetAddress.getByName(address.getIp()); 
			socket = new MulticastSocket(address.getPort());
			start();
			socket.joinGroup(group); 
		} catch (IOException ex) {
		   logger.error(ex.getMessage());
		}
	}
	
	
	/**
	 * Deja el grupo multicast
	 */
	public void leaveGroup(){ 
		try {
			socket.leaveGroup(group);
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
	
	
	/**
	 * Envia informacion al grupo multicast
	 * @param m Cadena binaria a ser enviada
	 */
	public void send(byte[] m){
		try {
			logger.developer("Send data to multicast group");
			DatagramPacket output = new DatagramPacket(m, m.length,group,address.getPort());
			socket.send(output);
			
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
	
	
	
	//Usado para escuchar por mensajes
	@Override
	public void run(){
		DatagramPacket input = null;
		while (true) {
			byte[] buffer = null;
			try {
				buffer = new byte[1024];
				input = new DatagramPacket(buffer, buffer.length);
				socket.receive(input); //Se epera a que llegue un mensaje
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
			Message m = new Message(buffer);
			/* Un nodo se unio al grupo */
			if(m.getOperationCode()==SINGIN_BigNode && m.getName() != BigNode.getName()){
				BigNode.receive(input);
				
				
			/* Un vecino solicita una direcciones */
			} else if (m.getOperationCode()==SEARCH_MULTICAST){
				
				/* Es mi direccion la que solicita */
				if(m.getLookedName() == BigNode.getName()){
					
					/* Envia mi informacion a cierto nodo */
					BigNode.receive(input);
				}
			}
		}
	}
}
