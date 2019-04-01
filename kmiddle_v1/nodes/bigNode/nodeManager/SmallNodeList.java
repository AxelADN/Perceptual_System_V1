package kmiddle.nodes.bigNode.nodeManager;

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

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.NodeList;


public class SmallNodeList extends NodeList {

    private String className;
    private int language;
    protected NodeConfiguration options;
    private int type;

    public SmallNodeList(String className, int language, NodeConfiguration options, int type) {
        super();
        this.className = className;
        this.language = language;
        this.options = options;
        this.type = type;
    }

    
    /**
     * Regresa la direccion del archivo ejectutable para este tipo de nodo
     * @return String que contiene el path hacia el arcivo ejecutable
     */
    
    public String getClassName(){
    	return className;
    }
    
    /**
     * Regresa el lenguaje de programacion sobre el cual esta programado el ejecutable
     * @return
     */
    public int getLanguage(){
    	return language;
    }
    
    
    public NodeConfiguration getOptions(){
    	return options;
    }
    
    public int getType(){
    	return type;
    }
}