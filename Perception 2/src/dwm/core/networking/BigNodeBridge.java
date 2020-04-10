/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dwm.core.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class BigNodeBridge extends Thread {
    
    private boolean isClosed = false;

    private ServerSocket listener;
    private int serverPort = 10500;
    private AreaBridgeListener areaListener;

    public BigNodeBridge(int serverPort, AreaBridgeListener areaListener) {
        this.serverPort = serverPort;
        this.areaListener = areaListener;
        createServer();
//        start();
    }

    private void createServer() {
        try {
            listener = new ServerSocket(serverPort);
        } catch (IOException ex) {
            Logger.getLogger(BigNodeBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {

            while (!isClosed) {

                System.out.println("Esperando conexiones...");

                Socket socket = listener.accept();

                BigNodeBridgeHandler singleClient = new BigNodeBridgeHandler(socket, areaListener);

            }

        } catch (IOException ex) {
            Logger.getLogger(BigNodeBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            isClosed = true;
            listener.close();
        } catch (IOException ex) {
            Logger.getLogger(BigNodeBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
