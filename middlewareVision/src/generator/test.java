/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

/**
 *
 * @author HumanoideFilms
 */
public class test {
    
    public static void main(String [] args){
        double a=5.0/2;
        System.out.println(a);
    }
    
    public static int label(double d){
        int divisions=3;
        double div=1/(double)divisions;
        return (int) (d/div);
    }
    
}
