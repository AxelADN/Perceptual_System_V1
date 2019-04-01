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


public class NodeConfiguration {
	
	private Boolean debug = false;
	private boolean child = false;
	private boolean persistent = false;	
        private boolean loadBalance = false;
	
	
	private short BEHAVIOR_SERIAL 			= 0;
	public static short BEHAVIOR_MAPPED 			= 1;
	
	private int behavior = BEHAVIOR_SERIAL;
	private short entityID = 0;
	
	
	
	

	
	private int VAL_DEBUG 		= 0b00000000000000000000000000000001;
	private int VAL_DEVMOD 		= 0b00000000000000000000000000000010;
	private int VAL_CHILD 		= 0b00000000000000000000000000000100;
	private int VAL_PERSISTENT 	= 0b00000000000000000000000000001000;
	private int VAL_BEHAVIOR_S 	= 0b00000000000000000000000000010000;
	private int VAL_BEHAVIOR_M 	= 0b00000000000000000000000000100000;

	
	public NodeConfiguration(){
		this(0);
	}
	
	
	public NodeConfiguration(short behavior ){
		setChild(true);
		setPersistent(true);
		if ( behavior == BEHAVIOR_MAPPED )
			setBehaviorMapped();
	}
	
	
	public NodeConfiguration(boolean isChild, boolean isPersistent, Boolean debug){
		setDebug(debug);
		setChild(isChild);
		setPersistent(isPersistent);
	}
	
	public NodeConfiguration(int val){
		if( (val & VAL_DEVMOD) != 0) 
			setDebug(null);
		else if( (val & VAL_DEBUG) != 0)
			setDebug(true);
		else
			setDebug(false);
		
		if( (val & VAL_CHILD) != 0)
			setChild(true);
		else
			setChild(false);
		
		if( (val & VAL_PERSISTENT) != 0)
			setPersistent(true);
		else
			setPersistent(false);
		
		if( (val & VAL_BEHAVIOR_M) != 0)
			setBehaviorMapped();
		
		if( (val & VAL_BEHAVIOR_S) != 0)
			setBehaviorSerial();
	}
	
	
	
	public void setDebug(Boolean debug){
		this.debug = debug;
	}
	
	
	public Boolean isDebug(){
		return debug;
	}


	public boolean isDev(){
		return debug==null;
	}
	
	public void setChild(boolean child){
		this.child = child;
	}
	
	public boolean ischild(){
		return child;
	}
	
	
	
	
	public void setBehaviorMapped(){
		this.behavior = BEHAVIOR_MAPPED;
	}
	
	public boolean isMapped(){
		return (behavior == BEHAVIOR_MAPPED);
	}
	
	
	public void setBehaviorSerial(){
		this.behavior = BEHAVIOR_SERIAL;
	}
	
	public boolean isSerial(){
		return (behavior == BEHAVIOR_SERIAL);
	}
	
	
	
	public void setPersistent(boolean persistent){
		this.persistent = persistent;
	}
	
	public boolean isPersistent(){
		return persistent;
	}
	
	
	/**
	 * Asigna un ID a la entidad, de esta manera pueden existir multiples instancias del middleware dentro de la misma red
	 * @param id ID de la entidad, el valor por default es 0
	 */
	public void setEntityID(short id){
		this.entityID = id;
	}
	
	
	public short getEntityID(){
		return entityID;
	}
	
	
	
	
	public int toInt(){
		int val=0;
		if ( isDev() ){
			val += VAL_DEVMOD;
		}else if ( isDebug() )
			val += VAL_DEBUG;
				
		if ( ischild() )
			val += VAL_CHILD;
			
		if( isPersistent() )
			val += VAL_PERSISTENT;
		
		
		
		if ( isMapped() )
			val += VAL_BEHAVIOR_M;
		else if ( isSerial() )
			val += VAL_BEHAVIOR_S;
	
		return val;
	}

    /**
     * @return the loadBalance
     */
    public boolean isLoadBalance() {
        return loadBalance;
    }

    /**
     * Evita que se creen nuevas instancias de los SmallNodes para que un solo 
     * nodo atienda las peticiones. Si es true ignorara el setMax que incrementa
     * las instancias de un tipo de SmallNode utilizando siempre la primera instancia
     * disponible
     * @param loadBalance Se establece si se va a balacear la carga del nodo
     */
    public void setLoadBalance(boolean loadBalance) {
        this.loadBalance = loadBalance;
    }
}
