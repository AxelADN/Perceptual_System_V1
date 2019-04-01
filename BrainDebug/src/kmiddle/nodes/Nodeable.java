package kmiddle.nodes;

import kmiddle.net.messages.Message;

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

public interface Nodeable {
	
	/**
	 * Crea nodos del tipo indicado
	 * @param type	Indice del tipo
	 * @param n		Numero de nodos a crear
	 */
	public void createNode(int type, int n);
	
	/**
	 * Funcion que recive los paquetes de entrada
	 * @param senderID	ID del nodo que envio el mensaje
	 * @param dataType	Tipo de data del mensaje
	 * @param data		Mensaje
	 */
	public void afferents(int senderID,  byte[] data);
	
	
	//public void processInThread(int senderID, short dataType, byte[] data);
	public void processInThread(Message msg);
	
}
