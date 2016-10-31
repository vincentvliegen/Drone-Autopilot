package DroneAutopilot.GUI;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		this.progressBar = new JProgressBar();
		this.maxValue = 0;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("AUTOPILOT GUI");
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,30,90));
		JLabel progress = new JLabel("Progress: ");
		progress.setFont(new Font("Tahoma", Font.PLAIN, 18));
		progress.setPreferredSize(new Dimension(80, 30));
		panel.add(progress);

		this.progressBar.setPreferredSize(new Dimension(250, 30));
		this.progressBar.setMaximum(100);
		panel.add(this.progressBar);
		
		frame.getContentPane().add(panel);

	}

	public void update(int distance) {
		if (distance > this.maxValue) {
			this.maxValue = distance;
			progressBar.setMaximum(distance);
		} else {
			this.progressBar.setValue(this.maxValue - distance);
		}
	}

	private final JProgressBar progressBar;
	private int maxValue;
}
