package kmiddle.utils.fields;

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


public interface DataTypeConstants {
	
	/**
	 * Lista de los valores obtenidos en la memoria
	 * emocional.
	 * Ej. [0,0,1,3,4,6] = [AVERSIVE, AVERSIVE, HIGH_AVERSIVE]
	 */
	final short EMOTIONAL_VALUE_LIST = 1;
	/**
	 * Lista de clases a la que puede pertenecer
	 * un objeto.
	 * Ej. A-98.5% B-100.% C-45.6% 
	 */
	final short OBJECT_CLASS_LIST = 2;
	/**
	 * Lista con los nombres de los objetos contenidos en
	 * una escena.
	 * Ej. [BEACH, SAND, PALM, COCONUT]
	 */
	final short OBJECT_NAME_LIST = 3;
	/**
	 * Tipo singular 
	 * Ej. 0100101
	 */
	final short SINGULAR_TYPE = 4;
	
	/**
	 * Lista de tipos singulares a clasificar
	 * Ej. 0101 1111 0000 0111
	 */
	final short SINGULAR_TYPE_LIST = 5;
	final short SINGULAR_TYPE_LENGTH = 5;
}
