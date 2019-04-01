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


public interface IndexConstants {
	
	/**
	 * La dimension de cada campo es en base a la cantidad de bytes que 
	 * necesita para ser representado.
	 */
	
	/**
	 * CODIGOS DE OPERACION
	 */
	final int I_OPCOD = 0;
    final int OPCOD_LENGTH = 2;
	
	/**
	 * IDENTIDICADORES EN EL PAQUETE
	 */
	final int I_NAME = 2;
	final int NAME_LENGHT = 4;
	
    final int I_MISSING = 2;
	
	final int I_SECOND_NAME = 2;
	
	/**
	 * DATOS DE NODO
	 */
	final int I_PORT = 6;
	final int I_IP = 10;
	
	
	/**
	 * TIPO DE DATO
	 */
	final int I_DATA_TYPE = 2;
	final int DATA_TYPE_LENGTH = 2;
	
	final int I_SECOND_DATA_TYPE = 6;
	/**
	 * DATO
	 */
    final int I_DATA = 4;
	final int I_SECOND_I_DATA = 8;

	
	
}
