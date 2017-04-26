package DroneAutopilot.graphicalrepresentation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WindowForEdgeMatching extends JFrame{

    public WindowForEdgeMatching(ArrayList<ArrayList<double[]>> mylist) {
    	setMyList(mylist);
        JPanel panel=new JPanel();
        getContentPane().add(panel);
        setSize(450,450);
   }
    int size = 450;
    int half = size/2;
    
    public ArrayList<ArrayList<double[]>> myList;
    
    public void setMyList(ArrayList<ArrayList<double[]>> myList) {
		this.myList = myList;
	}

    public void paint(Graphics g) {

        for(ArrayList<double[]> arrlist : myList) {
        	g.drawLine((int) (1.5*(arrlist.get(0)[2]) + half), (int) -(1.5*(arrlist.get(0)[1] + half)), (int) (1.5*(arrlist.get(1)[2] + half)),(int) -(1.5*(arrlist.get(1)[1] + half)));
    		System.out.println("draw");
        	System.out.println((int) (1.5*(arrlist.get(0)[2] + half)));
        	System.out.println((int) (1.5*(arrlist.get(0)[1] + half)));
        	System.out.println((int) (1.5*(arrlist.get(1)[2] + half)));
        	System.out.println((int) (1.5*(arrlist.get(1)[1] + half)));
    		System.out.println(Arrays.toString(arrlist.get(0)));
    		System.out.println(Arrays.toString(arrlist.get(1)));
        }
        super.paint(g);
        
    }


}