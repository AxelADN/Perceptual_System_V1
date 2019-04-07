/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.operations;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Luis Martin
 */
public class Image2dRepresentation {

    public static void decode2DString(String string) {

        String sxy[] = string.split(",");

        int columns = 1 + (sxy[0].length() - sxy[0].replace("<", "").length());
        int rows = 1 + (sxy[1].length() - sxy[1].replace("<", "").length());

        int imageMatrix[][] = new int[rows][columns];

        ArrayList<String[]> tokens = new ArrayList<>();
        ArrayList<ArrayList<String[]>> objectsXY = new ArrayList<>();

        ConcurrentHashMap<Integer, int[]> objects = new ConcurrentHashMap<>();

        int PARSING = 0;
        char buffer[] = string.toCharArray();
        int index = 0;

        final int START_OID = 10;

        int state = 0;
        StringBuilder currentToken = new StringBuilder();

        int currentColRow = 0;
        boolean inRow = false;

        while (PARSING < buffer.length) {
            switch (buffer[index]) {
                case '(':
                    state = START_OID;
                    break;
                case ')':
                    switch (state) {
                        case START_OID:
                            tokens.add(new String[]{currentToken.toString(), currentColRow + ""});

                            if (!inRow) {
                                objects.put(Integer.parseInt(currentToken.toString()), new int[]{currentColRow, 0});
                            } else {
                                int key = Integer.parseInt(currentToken.toString());
                                int pos[] = objects.get(key);
                                pos[1] = currentColRow;
                                objects.put(key, pos);
                            }

                            currentToken = new StringBuilder();
                            state = 0;
                            break;
                        default:
                            break;
                    }
                    break;
                case ',':
                    currentColRow = 0;
                    inRow = true;
                    objectsXY.add(tokens);
                    tokens = new ArrayList<>();
                    break;
                case '<':

                    currentColRow++;

                    break;
                default:

                    switch (state) {
                        case START_OID:
                            currentToken.append(buffer[index]);
                            break;
                        default:
                            break;
                    }

                    break;
            }
            index++;
            PARSING++;
        }
        objectsXY.add(tokens);

        ArrayList<String[]> tokensInX = objectsXY.get(0);
        ArrayList<String[]> tokensInY = objectsXY.get(1);

        Set<Integer> keys = objects.keySet();
        
        for (Integer key : keys) {
            int pos[] = objects.get(key);

            imageMatrix[(rows - 1) - pos[1]][pos[0]] = key;

        }

        System.out.println("Assembled matrix");
        
        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[0].length; j++) {
                System.out.print("[" + imageMatrix[i][j] + "]");
            }
            System.out.println("");
        }

    }

    public static String create2DString(int[][] f) {

        StringBuilder tdString = new StringBuilder();

        int columns = f[0].length;
        int rows = f.length;

        int numSimbX = columns - 1;
        int numSimbY = rows - 1;
        int numVar = (rows) * (columns);

        Object xi[] = new Object[numVar + numSimbX];
        Object pi[] = new Object[numVar + numSimbY];

        int posPi = 0;
        int posXi = 0;

        for (int i = rows - 1; i >= 0; i--) {

            posXi = (rows - 1) - i;

            for (int j = 0; j < columns; j++) {

                //if(!Character.isWhitespace(f[i][j])){   
                if (f[i][j] != 0) {
                    xi[posXi + ((posXi) / rows)] = f[i][j] + "";
                    pi[posPi + (posPi / columns)] = f[i][j] + "";
                }

                posPi++;
                posXi += rows;

            }
        }

        for (int i = 1; i < columns; i++) {
            xi[(i * rows) + (i - 1)] = "<";
        }

        for (int i = 1; i < rows; i++) {
            pi[(i * columns) + (i - 1)] = "<";
        }

        for (Object string : xi) {
            if (string != null) {
                if (!string.equals("<")) {
                    tdString.append("(").append(string.toString()).append(")");
                } else {
                    tdString.append(string.toString());
                }
            }
        }

        tdString.append(',');

        for (Object string : pi) {
            if (string != null) {
                if (!string.equals("<")) {
                    tdString.append("(").append(string.toString()).append(")");
                } else {
                    tdString.append(string.toString());
                }
            }
        }

        //tdString.append(xi).append(',').append(pi);
        //System.out.println(tdString.toString());
        return tdString.toString();

    }
}
