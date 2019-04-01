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

import java.net.DatagramPacket;

import kmiddle.net.Address;
import kmiddle.net.messages.Message;

public interface P2Pable {
	public int getName();
	public void waitConnections();
	public void stopListening();	
	public void send(Address adress, byte[] msg);
	public void receive(DatagramPacket datagramPacket);
    public void processDatagramPacket(DatagramPacket datagramPacket);
    public void processMessage(Address adress,Message msg);
    
	
    
}
