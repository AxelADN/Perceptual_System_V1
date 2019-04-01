/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmiddle.net.messages;

/**
 * 
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
public interface OperationCodeConstants {

	/*
	 * 
	 * *******************
	 * ESTRUCTURA DE LOS VALORES DE LAS CONSTANTES
	 * 
	 * /** ::= <codop>
	 */
	final short ACK = 1;

	/**
	 * <codop>
	 */
	final short AVAILABLE = 2;
	/**
	 * //TODO ::= <codop>
	 */
	final short CREATE = 17;

	/**
	 * ::= <codop><dataType><data>
	 */
	final short DATA = 18;

	/**
	 * //TODO ::= <codop>
	 */
	final short DEAD = 19;

	/**
	 * //TODO ::= <codop>
	 */
	final short DELETE = 20;

	/**
	 * ::= <codop><nodeID><port><host>
	 */
	final short FIND_NODE = 609;
	/**
	 * ::= <codop>
	 */
	final short HANDSHAKE = 49;
	/**
	 * ::= <codop><nodeID>
	 */
	final short HELLO = 50;

	final short IM_DEAD = 1041;
	/**
	 * 
	 */
	final short NEW_NODE = 1633;
	/**
	 * ::= <codop><nodeID>
	 */
	final short NOFIND_NODE = 1634;

	final short OK_AVAILABLE = 1793;

	/**
	 * ::=<codop><node>
	 */
	final short SEARCH_MULTICAST = 2401;
	/**
	 * <codop><nodeID><port><host>
	 */
	final short SEARCH_NODE_REQUEST = 2402;

	/**
	 * ::=<codop><nodeId><dataType><data>
	 */
	final short SECOND_DATA = 2323;
	/**
	 * ::=<codop>
	 */
	final short SEND_ME = 2404;
	/**
	 * ::=<codop><nodeID>
	 */
	final short SINGIN_BigNode = 2405;

	/**
	 * ::=<codop><nodeID>
	 */
	final short SINGIN_SIBLING = 2406;
	
	/**
	 * ::=<codop><nodeID>
	 */
	final short SINGIN_CHILD = 2407;
	

	/**
	 * ::=<codop>
	 */	
	final short FREE_NODE = 2408;

}
