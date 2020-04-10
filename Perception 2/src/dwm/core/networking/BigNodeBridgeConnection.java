/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dwm.core.networking;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author luis_
 */
public class BigNodeBridgeConnection {

    private Socket socket;
    private PrintWriter out;

    public BigNodeBridgeConnection(String ip, int port) {

        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFakePosition(int time) {
        
        JSONObject obj = new JSONObject();

        obj.put("type", "1");
        obj.put("time", time);
        obj.put("x", "203");
        obj.put("y", "182");
        obj.put("pid", "1");

        System.out.println(obj.toJSONString());
        out.println(obj.toString());

    }

    public void sendClass(ArrayList classFeatures, int spikeType, int time) {

        JSONObject obj = new JSONObject();

        obj.put("type", ""+spikeType+"");
        obj.put("time", time);
        obj.put("features", classFeatures);

        System.out.println(obj.toJSONString());
        out.println(obj.toString());

    }

    public void close() {
        try {
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendReset() {
        JSONObject obj = new JSONObject();

        obj.put("type", "5000");

        System.out.println(obj.toJSONString());
        out.println(obj.toString());
    }
}
