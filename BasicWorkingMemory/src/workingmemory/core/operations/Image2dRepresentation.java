/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.operations;

/**
 *
 * @author Luis Martin
 */
public class Image2dRepresentation {

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
