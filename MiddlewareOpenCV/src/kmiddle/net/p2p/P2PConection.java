package kmiddle.net.p2p;

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
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class P2PConection extends Thread {
	//+++++++++++++++++++++
	
	/** Objeto que implementa la interface CommunicationBehavior al cual se le enviara el mensaje */
	private P2Pable messageManager;
	
	/** Se encarga de escuchar por mensajes **/
	private DatagramSocket datagramSocket;
	
	/** Objeto para guardar el mensaje resivido **/
	private DatagramPacket receiveMessage;
	
	private boolean run;
	
	
	/**
	 * Se encarga de escuchar mensajes entrantes
	 * @param messageManager 
	 * Objeto que se encargara de manejar el mensaje resivido
	 * @param datagramSocket 
	 * Objeto que escucha por el puerto
	 */
	public P2PConection(P2Pable messageManager, DatagramSocket datagramSocket){
		super("Thread: "+datagramSocket);
		this.messageManager = messageManager;
		this.datagramSocket = datagramSocket;
		run = true;
	}
	
	//+++++++++++++++++++++
	@Override
	public void run(){
		while(run){
			//Escucha por el mensaje
			try {
				byte datagrama[] = new byte[ 64000 ];
				receiveMessage = new DatagramPacket(datagrama,datagrama.length);
				datagramSocket.receive(receiveMessage);	//Waiting to receive, received message will be storage in receiveMessage
			}
			catch(InterruptedIOException interruptedIOException){
				System.out.println(interruptedIOException.getMessage());
				continue;
			}
			catch( IOException ioException) {
				System.out.println(ioException.getMessage());
			}
			
			//Enviar mensaje a Manejador
			messageManager.receive(receiveMessage);
		}
	}
	
	//+++++++++++++++++++++
	public void stopThread(){
		run = false;
	}
}
