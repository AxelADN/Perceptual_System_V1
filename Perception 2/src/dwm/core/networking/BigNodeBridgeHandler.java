/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dwm.core.networking;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis
 */
public class BigNodeBridgeHandler extends Thread {

    private Socket socket;
    private AreaBridgeListener areaListener;

    public BigNodeBridgeHandler(Socket socket, AreaBridgeListener areaListener) {
        this.socket = socket;
        this.areaListener = areaListener;
        start();
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;

            while ((input = in.readLine()) != null) {
                
                areaListener.receiveFromBridge(input);

            }

            in.close();
            socket.close();

        } catch (Exception ex) {
            Logger.getLogger(BigNodeBridgeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(BigNodeBridgeHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
