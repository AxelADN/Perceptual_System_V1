/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.connections;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Luis
 */
public class ImageSender extends Thread {

    private int port = 10000;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private LegoSocketListener listener;
    private boolean clienteDesconecto;
    private OutputStream oStream;

    private String address = "10.0.5.150";

    public ImageSender(String address, int port) throws IOException {
        this.address = address;
        this.port = port;

    }

    public void sendImage(String imagePath) {
        try {
            
            socket = new Socket();
            socket.connect(new InetSocketAddress(this.address, port), port);
            oStream = socket.getOutputStream();
            out = new PrintWriter(socket.getOutputStream(), true);

            BufferedImage img = ImageIO.read(new File(imagePath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            baos.flush();
            byte[] imageInBytes = baos.toByteArray();
            baos.close();

            oStream.write(imageInBytes);
            oStream.flush();

            System.out.println(imageInBytes.length);

        } catch (IOException ex) {
            Logger.getLogger(ImageSender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static byte[][] divideArray(byte[] source, int chunksize) {

        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunksize)][chunksize];

        int start = 0;

        for (int i = 0; i < ret.length; i++) {
            if (start + chunksize > source.length) {
                System.arraycopy(source, start, ret[i], 0, source.length - start);
            } else {
                System.arraycopy(source, start, ret[i], 0, chunksize);
            }
            start += chunksize;
        }

        return ret;
    }

    public void run() {
        try {
            while (true) {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String finalResult = "";
                String responseLine;
                System.out.println("Esperando...");
                while ((responseLine = in.readLine()) != null) {
                    finalResult += responseLine;
                }

                if (finalResult.isEmpty()) {
                    socket.close();
                    in.close();
                    out.close();
                    in = null;
                    out = null;
                    socket = null;
                }
            }

        } catch (Exception ex) {
            if (!clienteDesconecto) {
                listener.chairDisconnected();
            }
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception ex) {
            Logger.getLogger(ImageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isClienteDesconecto() {
        return clienteDesconecto;
    }

    public void setClienteDesconecto(boolean clienteDesconecto) {
        this.clienteDesconecto = clienteDesconecto;
    }

}
