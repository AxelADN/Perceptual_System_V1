package kmiddle.nodes.smallNodes;

/**
 * Kuayolotl Middleware System
 *
 * @author Karina Jaime <ajaime@gdl.cinvestav.mx>
 *
 * This file is part of Kuayolotl Middleware
 *
 * Kuayolotl Middleware is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Kuayolotl Middleware is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Kuayolotl Middleware. If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kmiddle.log.NodeLog;
import kmiddle.net.Address;
import kmiddle.net.Node;
import kmiddle.net.messages.Message;
import kmiddle.net.messages.OperationCodeConstants;
import kmiddle.net.p2p.P2PConection;
import kmiddle.net.p2p.P2Pable;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.NodeList;
import kmiddle.nodes.request.RequestList;
import kmiddle.nodes.request.RequestNode;
import kmiddle.utils.NodeNameHelper;
import kmiddle.utils.fields.NullValueConstants;

public class SmallNodeSocket implements P2Pable, NullValueConstants,
        OperationCodeConstants {

    //-----------------------------------
    protected int myName;
    protected SmallNodeBehavior behavior;
    protected Node father;
    protected Address myAddress;
    protected DatagramPacket datagramPacket;
    protected DatagramSocket datagramSocket;
    protected P2PConection p2pConnection;
    protected NodeList BigNodes;
    protected NodeList siblings;
    protected RequestList requestNodes;
    protected NodeLog log;
    protected Class<?> namer;
    protected NodeConfiguration options;

    //-----------------------------------
    /**
     *
     * @param myName	Nombre del BigNode
     * @param behavior	Objecto que contiene el comportamiento del nodo
     * @param mwIp	La dirreccion de su mw
     * @param mwPort	Puerto de su mw
     */
    public SmallNodeSocket(int myName, SmallNodeBehavior behavior, Node father, NodeLog log, Class<?> namer, NodeConfiguration options) {

        this.myName = myName;
        this.behavior = behavior;
        this.father = father;
        this.namer = namer;
        this.log = log;
        this.options = options;

        BigNodes = new NodeList();
        siblings = new NodeList();
        requestNodes = new RequestList();
    }

    public void startNode() {
        waitConnections();
        log.header(myName, myAddress);
        //System.out.println("[Enviando SINGIN a " + father.toString() + "]");
        send(father.getAddress(), Message.getSingInChildMessage(myName));
    }

    /**
     *
     */
    public int getName() {
        return myName;
    }

    /**
     * Envio de informacion desde el BigNode
     *
     * @param nodeName	Id del nodo al que se envia
     * @param dataType	Tipo de mensaje
     * @param data Mensaje
     */
    public void efferents(int nodeName, short dataType, byte[] data) {
        if (NodeNameHelper.isBigNode(nodeName) && BigNodes.contains(nodeName)) { 			//Si es un BigNode y tengo su direccion
            Node n = BigNodes.getNode(nodeName);
            int senderID = 0;
            if (n.getName() == father.getName()) {
                senderID = myName;
            } else {
                senderID = myName & NodeNameHelper.BigNodeMASK;
            }
            send(n.getAddress(), Message.getDataMessage(senderID, dataType, data));
            log.send(nodeName, DATA, "");

        } else if (NodeNameHelper.isSmallNode(nodeName) && siblings.contains(nodeName)) {	//Si es un litle node y tengo su ubicacion
            Node n = siblings.getNode(nodeName);
            send(n.getAddress(), Message.getDataMessage(myName, dataType, data));
            log.send(nodeName, DATA, "");
            
            /**
             * * Solicita que se le envie la direccion del nodo **
             */
        } else {
            requestNodes.add(nodeName, dataType, data);
            send(father.getAddress(), Message.getSearchNodeRequest(nodeName));
            log.send_debug(father.getName(), SEARCH_NODE_REQUEST, "");

        }
    }

    /**
     *
     * @param addr
     * @param data
     */
    @Override
    public void send(Address addr, byte[] data) {
        try {
            datagramPacket = new DatagramPacket(data, data.length,
                    InetAddress.getByName(addr.getIp()), addr.getPort());
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            //temporal.error(e.getMessage());
        }
    }

    /**
     * Inicialia el hilo que escucha las peticiones en la red
     */
    @Override
    public void waitConnections() {
        myAddress = new Address();
        /* Se abre un puerto */
        try {
            datagramSocket = new DatagramSocket();
            myAddress.setPort(datagramSocket.getLocalPort());
            InetAddress addr;
            try {
                addr = InetAddress.getLocalHost();
                String ip = addr.getHostAddress();

                //Cambia la IP a un formato estandar, esto puede variar debido al SO
                if (ip.equals("0.0.0.0") || ip.equals("127.0.1.1")) {
                    ip = "127.0.0.1";
                }
                myAddress.setIp(ip);
            } catch (UnknownHostException ex) {
                //temporal.error(ex.getMessage());

            }
        } catch (SocketException socketException) {
            //temporal.error(socketException.toString());
        }
        //temporal.header(myName, myAddress);

        p2pConnection = new P2PConection(this, datagramSocket);
        p2pConnection.start();
    }

    /**
     *
     * @param datagramPacket
     */
    @Override
    public void receive(DatagramPacket datagramPacket) {
        processDatagramPacket(datagramPacket);
    }

    /**
     *
     * @param packet
     */
    @Override
    public void processDatagramPacket(DatagramPacket packet) {
        Address addr = new Address(
                packet.getAddress().toString().substring(1),
                packet.getPort()
        );
        byte[] data = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());

        Message msg = new Message(data);
        processMessage(addr, msg);
    }

    //----------------------------------
    /**
     *
     * @param addr
     * @param msg
     */
    @Override
    public void processMessage(Address addr, Message msg) {
        int opcod = msg.getOperationCode();
        switch (opcod) {
            case FIND_NODE:
                findNode(msg);
                break;
            case DATA:
                data(addr, msg);
                break;
            default:
                break;
        }
    }

    /**
     * Manda la informacion necesaria al nodo que se solicito
     *
     * @param msg
     */
    protected void findNode(Message msg) {
        Node n = msg.getNode();
        log.receive_debug(msg.getName(), FIND_NODE, "");

        if (NodeNameHelper.isBigNode(n.getName())) {			//Agregar a Big Nodes
            if (!BigNodes.contains(n)) {
                BigNodes.add(n);
            }

        } else {													//Agregar a Hermanos
            //Change the name to refer to this node every time this node look for this kind of sibling
            n.setName(n.getName() & (NodeNameHelper.BigNodeMASK + NodeNameHelper.TYPEMASK));
            if (!siblings.contains(n)) {
                siblings.add(n);
            }
        }

        //Enviar mensajes pendientes
        if (requestNodes.contains(n.getName())) {
            sendWaitingMessages(n);
        }
    }

    private void sendWaitingMessages(Node n) {
        int senderID = myName;

        if (NodeNameHelper.isBigNode(n.getName()) && n.getName() != father.getName()) {
            senderID = myName & NodeNameHelper.BigNodeMASK;
        }

        ArrayList<RequestNode> indexList = requestNodes.getNodeFromNeeded(n.getName());
        for (int i = 0; i < indexList.size(); i++) {
            RequestNode r = indexList.get(i);
            log.send(n.getName(), DATA, "");
            send(n.getAddress(), Message.getDataMessage(senderID, r.getDataType(), r.getData()));
        }
        requestNodes.removeAll(indexList);

    }

    @Override
    public void stopListening() {
        p2pConnection.stopThread();

    }

    /**
     * Envia la informacion al BigNode para que la interprete.
     *
     * @param addr
     * @param msg
     */
    protected void data(Address addr, Message msg) {
        int nodeName = msg.getName();
        log.receive(msg.getName(), DATA, "");
        behavior.afferents(nodeName, msg.getData());
        if (options.isPersistent()) { 				//Si es persistente
            log.send_debug(father.getName(), FREE_NODE, "Estoy libre");
            send(father.getAddress(), Message.getFreeNodeRequest(myName));
        } else {
            stopListening();
        }
    }
}
