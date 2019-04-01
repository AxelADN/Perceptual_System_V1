package kmiddle.nodes.smallNodes;

import kmiddle.nodes.NodeConfiguration;

public class SmallNodeConfiguration extends  NodeConfiguration{
	
	private boolean persistent = false;
	
	private short BEHAVIOR_SERIAL 			= 0;
	private short BEHAVIOR_MAPPED 			= 1;
	private int behavior = BEHAVIOR_SERIAL;
	
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
	
}
