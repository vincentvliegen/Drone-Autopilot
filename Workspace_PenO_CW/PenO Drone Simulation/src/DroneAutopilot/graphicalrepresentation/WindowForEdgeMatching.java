package DroneAutopilot.graphicalrepresentation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class WindowForEdgeMatching extends JFrame{

    public WindowForEdgeMatching() {
        JPanel panel=new JPanel();
        getContentPane().add(panel);
        setSize(450,450);

        JButton button =new JButton("press");
        panel.add(button);
    }
    
    public ArrayList<ArrayList<double[]>> myList;
    
    public void setMyList(ArrayList<ArrayList<double[]>> myList) {
		this.myList = myList;
	}

    public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        for(ArrayList<double[]> arrlist : myList) {
    		Line2D lin = new Line2D.Double(50*arrlist.get(0)[2], 50*arrlist.get(0)[1], 50*arrlist.get(1)[2],50* arrlist.get(1)[1]);
    		g2.draw(lin);
        }
    }


}