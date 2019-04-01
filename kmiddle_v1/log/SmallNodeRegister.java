package kmiddle.log;

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

import kmiddle.net.Address;
import kmiddle.net.messages.OperationCode;
import kmiddle.utils.NodeNameHelper;

public class SmallNodeRegister implements Logable{

	Class<?> namer;
	int smnn;
	
	public SmallNodeRegister(Class<?> namer){
		this.namer = namer;
	}
	
	@Override
	public String header(int name, Address address) {
		String msg = "\n";
		msg+="************************************************\n";
		msg+="MyName:" + NodeNameHelper.getNameAsString(namer,name) + "  \tMyIde: "+ name +"\tID: \n";
		msg+="MyHost: "+address.getIp() +"\tMyPort: "+address.getPort()+"\n";
		msg+="************************************************\n";
		return msg;
	}

	@Override
	public String send(int name, String more) {
		return "[SEND]["+""/*NodeNameHelper.getBigNodeName(name)*/+"]["+more+"]";
	}

	@Override
	public String send(Address addr, String more) {
		return"[SEND]["+addr+"]["+more+"]";
	}

	@Override
	public String receive(int name, String more) {
		return "[RECEIVE]["+ NodeNameHelper.getNameAsString(namer, name) + "]["+more+"]";
	}

	@Override
	public String receive(Address addr, String more) {
		return "[RECEIVE]["+addr+"]["+more+"]";
	}
	
	@Override
	public String saveRequest(int name, String dataType) {
		return "[SAVE_REQUEST]["+  NodeNameHelper.getBigNodeName(namer, name) +"]["+dataType;
	}

	@Override
	public String error(String more) {
		return "[ERROR]["+more+"]";
	}

	@Override
	public String info(String more) {
		return "[INFO]["+more+"]";
	}

	public String send_debug(int name, short type, String more) {
		// TODO Auto-generated method stub
		return "[SEND][" + NodeNameHelper.getNameAsString(namer, name) + "][" + OperationCode.getRealName(type) + "]" + "[" + more + "]";
	}

	@Override
	public String receive_debug(int name, short type, String more) {
		// TODO Auto-generated method stub
		return "[RECEIVE][" + NodeNameHelper.getNameAsString(namer, name) + "][" + OperationCode.getRealName(type) + "]" + "[" + more + "]";		
	}
	
}
