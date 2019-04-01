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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import kmiddle.net.Address;
import kmiddle.utils.NodeNameHelper;



/*
 * 
 * 
The Loggler class has the next levels 
    SEVERE (highest)
    WARNING
    INFO
    CONFIG
    FINE
    FINER
    FINEST
 * 
 * 
 */
public class NodeLog{
	
	Logable register;
	Logger logger; 
	Class<?> namer = null;
	Boolean isDebug;
	
	
	public NodeLog(String className, int name, Class<?> Namer, Boolean isDebug ){
	
		this.namer = Namer;
		this.isDebug = isDebug;
		if ( NodeNameHelper.isBigNode(name) )
			register = new BigNodeRegister(namer);
		else
			register = new SmallNodeRegister(namer);
		logger = Logger.getLogger(className);
		
		
		if ( isDebug == null )
			logger.setLevel(Level.ALL);
		else if ( isDebug )
			logger.setLevel(Level.FINER);
		else 
			logger.setLevel(Level.FINE);
		
		Handler h = new ConsoleHandler();
		h.setLevel(logger.getLevel());
		logger.setUseParentHandlers(false);
		logger.addHandler(h);
		
		
		
		String path = new File("").getAbsolutePath() + "/log/";
		
		if (! new File(path).exists())
			if (! new File(path).mkdir() )				
				System.out.println("Fatal error Could not create log file");
		
		
		try {
			Calendar cal = Calendar.getInstance();
			//Name of the LOG file
			FileHandler fh = new FileHandler(path + cal.get(Calendar.YEAR) + "_" + cal.get(Calendar.MONTH) + "_" + 
										cal.get(Calendar.DAY_OF_MONTH) + "_" + cal.get(Calendar.HOUR_OF_DAY) + "_" + cal.get(Calendar.MINUTE) + "_" + NodeNameHelper.getNameAsString(Namer, name) + ".log");
			logger.addHandler(fh);
			fh.setEncoding("UTF-8");
			fh.setFormatter(new XMLFileFormatter());
			
		} catch (SecurityException | IOException e) {
			logger.warning(e.getMessage());
		}
	}

	
	public void header(int name, Address address) {
		logger.severe(register.header(name, address));
	}

	
	
	//Regist
	public void send(int name, String more) {
		logger.severe(register.send(name, more));
	}
	
	public void send_debug(int name, String more){
		logger.finer(register.send(name, more));
	}
	
	public void send(int name, short type, String more) {
		logger.severe(register.send(name, more));
	}
	public void send_debug(int name, short type, String more) {
		logger.finer(register.send_debug(name, type, more));
	}

	
	public void send(Address addr, String more) {
		logger.info(register.send(addr, more));
	}

	
	public void receive(int name, String more) {
		logger.info(register.receive(name, more));
	}

	
	public void receive(Address addr, String more) {
		logger.info(register.receive(addr, more));
	}
	
	public void receive(int name, short type, String more) {
			logger.severe(register.receive(name, more));
	}
	
	public void receive_debug(int name, short type, String more) {
			logger.finer(register.receive_debug(name, type, more));
	}
	
	
	public void saveRequest(int name, String dataType) {
		logger.info(register.saveRequest(name, dataType));
	}

	
	
	public void debug(String more) {
		logger.finer(register.info(more));
	}
	
	public void developer(String more){
		logger.finest(register.info(more));
	}
	
	
	public void error(String more) {
		logger.severe(register.error(more));
	}
	
	public void message(String message){
		logger.severe(register.info(message));
	}
	
	/*
	public void config( String more) {
		logger.config(register.info( more));
	}

	
	public void info(String more) {
		logger.info(register.info(more));
	}
	*/
	
}
