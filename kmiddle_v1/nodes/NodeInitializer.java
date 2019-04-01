package kmiddle.nodes;

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


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import kmiddle.net.Node;

public class NodeInitializer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String className = "";
		int name = 0;
		Node father = null;
		Class<?> namer = null;
		int optionsINT = 0;
		
		for ( int  i = 0; i < args.length; i+=2){						//Este ciclo se utliza para extraer los argumentos a sus correspondientes objetos
			if ( args[i].equals("--className") ){
				className = args[i+1];
				
			} else if ( args[i].equals("--name")){
				name = Integer.parseInt( args[i+1] );
				
			}else if ( args[i].equals("--father")){
				father = new Node(Integer.parseInt(args[i+1]), args[i+2], Integer.parseInt(args[i+3]));
				
			}else if ( args[i].equals("--options")){
				optionsINT = Integer.parseInt(args[i+1]);
				
			}else if ( args[i].equals("--namer")){
				try {
					namer = Class.forName(args[i+1]);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
			}
		}		
		
		NodeConfiguration nc = new NodeConfiguration(optionsINT);
		
		if ( className != ""){
			if ( father != null ){
				nc.setChild(true);
				if ( namer != null ){
					
				
				
					try {				
						Class<?> nodeClass = Class.forName(className);										//Conseguir clase
						
                                                /*
                                                * Luis Martin: Le cambie de Integer.class a Integer.TYPE por que la clase SmallNode usa int pero si se usa el reflection con .class no la encuentra y falla
                                                */
                                                
                                                Constructor<?> nodeConstr = nodeClass.getConstructor(					
																	Integer.TYPE, 
																	Node.class,
																	NodeConfiguration.class,
																	Class.class);							//Conseguir constructor
						
                                                
                                                nodeConstr.newInstance(new Object[]{ 
														name, 
														father,
														nc,
														namer});								//Iniciar
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

}
