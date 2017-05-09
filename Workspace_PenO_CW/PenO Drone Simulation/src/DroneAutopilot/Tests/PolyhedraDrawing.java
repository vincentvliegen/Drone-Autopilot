package DroneAutopilot.Tests;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class PolyhedraDrawing {
	public static void main(String[] args) throws Exception{
		TestImageProcessing tst = new TestImageProcessing();
		String coordin = tst.testPixelCoordinates();		
		
		String[] str = coordin.split(" ");
		List<Integer> intList = new ArrayList<>();
		for (String x : str)
			intList.add(Integer.parseInt(x));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(intList);
			}
		});
	}

	private static void createAndShowGUI(List<Integer> intList) {
		System.out.println("Created GUI on EDT? "
				+ SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Swing Paint Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new MyPanel(intList));
		f.setSize(400, 400);
		f.setVisible(true);
	}
}

class MyPanel extends JPanel {
	private List<Integer> lst;

	public MyPanel(List<Integer> intList) {
		setBorder(BorderFactory.createLineBorder(Color.black));
		this.lst = intList;
		repaint();
	}

	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int x = 0;
		int y = 0;
		for (int i = 0; i < lst.size(); i++) {
			if (i % 2 == 0)
				x = lst.get(i);
			else {
				y = lst.get(i);
				g2d.drawOval(x, y, 1, 1);
			}
		}
	}
}
