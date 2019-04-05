/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import org.opencv.core.Point;

/**
 *
 * @author Luis Humanoide
 */
public class Matrix {
    
    public Line[][] matrix;
    public int n;
    public int m;
    public int space;
    Line line1;
    Line line2;
    
    public Matrix(int n, int m, int space){
        matrix=new Line[n][m];
        this.n=n;
        this.m=m;
        this.space=space;
    }
    
    public void fill(){
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                matrix[i][j]=new Line(new Point(i*space,j*space),90,LineConfig.radius,LineConfig.stroke0);
            }
        }
    }
    
    public void generate(){
        fill();
        Point[] arPoints={new Point(n/2,m/4),new Point(n/4,m/2),
            new Point(n/2,m*3/4),new Point(n*3/4,m/2)};
        int r1=random(4);
        int r2=random(4);
        if(r1==r2){
            r2++;
        }
        r1=r1%4;
        r2=r2%4;
        line1=new Line(new Point((int)arPoints[r1].x*space,(int)arPoints[r1].y*space),70,LineConfig.radius,LineConfig.strokeS);
        line2=new Line(new Point((int)arPoints[r2].x*space,(int)arPoints[r2].y*space),-30,LineConfig.radius,LineConfig.strokeS);
        matrix[(int)arPoints[r1].x][(int)arPoints[r1].y]=line1;
        matrix[(int)arPoints[r2].x][(int)arPoints[r2].y]=line2;
        
    }
    
    public int random(int size){
        return (int)(Math.random()*size);
    }

    public Line getLine1() {
        return line1;
    }

    public Line getLine2() {
        return line2;
    }
    
}
