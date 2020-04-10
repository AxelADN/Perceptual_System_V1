// 
// Decompiled by Procyon v0.5.36
// 

package cFramework.communications.messages.base;

import java.nio.ByteBuffer;

public class OperationCode implements OperationCodeConstants
{
    public static String getOperationCode(final short index) {
        return getRealName(index);
    }
    
    public static short getOperationCode(final byte[] msg) {
        final ByteBuffer buffer = ByteBuffer.allocate(msg.length);
        buffer.put(msg);
        return buffer.getShort(0);
    }
    
    public static String getRealName(final short opCod) {
        switch (opCod) {
            case 1: {
                return "ACK";
            }
            case 17: {
                return "CREATE";
            }
            case 21: {
                return "CREATE_AREA_REQUEST";
            }
            case 22: {
                return "CREATE_AREA_HELPER_REQUEST";
            }
            case 18: {
                return "DATA";
            }
            case 19: {
                return "DEAD";
            }
            case 20: {
                return "DELETE";
            }
            case 609: {
                return "FIND_NODE";
            }
            case 49: {
                return "HANDSHAKE";
            }
            case 50: {
                return "HELLO";
            }
            case 30: {
                return "IGNITE_ENTITY_LIST";
            }
            case 1633: {
                return "NEW_NODE";
            }
            case 1634: {
                return "NOFIND_NODE";
            }
            case 2401: {
                return "SEARCH_MULTICAST";
            }
            case 2402: {
                return "SEARCH_NODE";
            }
            case 2404: {
                return "SEND_ME";
            }
            case 2323: {
                return "SECOND_DATA";
            }
            case 2405: {
                return "SINGIN_AREA";
            }
            case 2406: {
                return "SINGIN_SIBLING";
            }
            case 2407: {
                return "SINGIN_CHILD";
            }
            case 23: {
                return "UPDATE";
            }
            default: {
                return "N/D";
            }
        }
    }
}
