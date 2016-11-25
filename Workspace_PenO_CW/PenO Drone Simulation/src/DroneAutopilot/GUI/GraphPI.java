package DroneAutopilot.GUI;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class GraphPI extends JFrame{

	private PICanvas canvas;
	private Graphics graphics;
	private List<Integer> x = new ArrayList<>();
	private List<Integer> y = new ArrayList<>();
	private int oldTimeValue;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphPI window = new GraphPI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GraphPI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		createJFrame();
		createCanvas();
		pack();
		setVisible(true);
	}

	public void createJFrame(){
		setLayout(new BorderLayout());
		setTitle("PID Graph");
		setAlwaysOnTop(true);
		setBounds(100, 300, 800, 500);
	}

	public void createCanvas(){
		canvas = new PICanvas();
		canvas.setPreferredSize(new Dimension(800,500));
		canvas.setVisible(true);
		getContentPane().add(canvas);
	}

	public void update(int x, int y){
		if(this.x.size()==1){
			oldTimeValue = x;
			this.x.add(10);
			this.y.add(300);
		}
		else if(this.x.size()>1){
			int intervalX = (x-oldTimeValue)*10;
			this.x.add(this.x.get(this.x.size()-1)+intervalX);
			this.y.add(this.y.get(0)-y);
			oldTimeValue = x;
		} 

		graphics = canvas.getGraphics();
		canvas.paint(graphics);
		canvas.setVisible(true);
		repaint();
	}


	class PICanvas extends Canvas{
		@Override
		public void paint(Graphics g){
			if(x.size()==0){
				//X-as
				g.drawLine(10, 300, 700, 300);
				x.add(10);
				//Y-as
				g.drawLine(10, 300, 10, 150);
				y.add(300);
				for(int i = 10; i<(700-10); i+=10)
				g.drawRect(i, 300, 2, 2);
			}
			
			

			g.setColor(Color.RED);
			for(int i=0; i<x.size(); i++){
				//System.out.println("x: " + x.get(i));
				//System.out.println("y: " + y.get(i));
				g.fillRect(x.get(i), y.get(i), 2, 2);
			}
		}
	}
}

