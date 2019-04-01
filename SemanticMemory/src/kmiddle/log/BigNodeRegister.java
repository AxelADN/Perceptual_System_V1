package kmiddle.log;

/**
 * Kuayolotl Middleware System
 * @author Karina Jaime <ajaime@gdl.cinvestav.mx>
 * 
 * This file is part of Kuayolotl Middleware.
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
 * 
 */

import kmiddle.net.Address;
import kmiddle.net.messages.OperationCode;
import kmiddle.utils.NodeNameHelper;

public class BigNodeRegister implements Logable{
	
	Class<?> namer;
	
	public BigNodeRegister(Class<?> namer){
		this.namer = namer;
	}

	@Override
	public String header(int name, Address address) {
		String msg = 
			  "<start>"
				+ "<name>" + NodeNameHelper.getNameAsString(namer, name) +"</name>\n"
				+ "<ID>" + name +"</ID>\n"
				+ "<address>\n"
					+ "\t<host>" + address.getIp() +"</host>\n"
					+ "\t<port>" + address.getPort()+"</port>\n"
				+ "</address>\n"
			+ "</start>";
		
		return msg;
	}

	@Override
	public String send(int name, String more) {
		return "<SEND>"
				+ "<to>"+ NodeNameHelper.getNameAsString(namer, name)+ "</to>"
				+ "<data>"+more+"</data>"
			+ "</SEND>";
	}

	@Override
	public String send(Address addr, String more) {
		return "<SEND>"
					+ "<to>" + addr+"</to>"
					+ "<data>" + more+"</data>"
			+ "</SEND>";
	}
	
	@Override
	public String send_debug(int name, short type, String more) {
		return "<send> "
				+ "<to>" + NodeNameHelper.getNameAsString(namer, name) + "</to>"
				+ "<type>" + OperationCode.getRealName(type) + "</type> "
				+ /*"<data>" +*/ more /*+ "</data>"*/
			 +"</send>";
	}
	
	
	@Override
	public String receive(int name, String more) {
		return "<receive> "
					+ "<from>" + NodeNameHelper.getNameAsString(namer, name)+ "</from>" 
					/*+ "<data>"*/ + more/*+ "</data>"*/
			+ "</receive>";
	}

	@Override
	public String receive(Address addr, String more) {
		return "<receive><from>"+addr+"</from>"+more+"</receive>";
	}
	
	@Override
	public String receive_debug(int name, short type, String more) {
		return "<receive> "
				+ "<sender> "
					/*+ "<ID>" + name + "</ID>"*/
					+ "<name>"  + NodeNameHelper.getNameAsString(namer, name)+"</name>"
				+ "</sender> "
				+ "<type>"  + OperationCode.getRealName(type) + "</type> "
				+ more
			  +"</receive>";
	}
	
	

	@Override
	public String saveRequest(int name, String dataType) {
		return "[SAVE_REQUEST]["+""/*NodeNameHelper.getBigNodeName(name)*/+"]["+dataType+ "]";
	}

	@Override
	public String error(String more) {
		return "<ERROR>"+more+"</ERROR>";
	}

	@Override
	public String info(String more) {
		return "<INFO>"+more+"</INFO>";
	}

	
	
}
