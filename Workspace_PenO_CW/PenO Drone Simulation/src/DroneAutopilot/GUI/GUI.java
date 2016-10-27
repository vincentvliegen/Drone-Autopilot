package DroneAutopilot.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import javax.swing.JLabel;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(this.progressBar, BorderLayout.SOUTH);
		this.progressBar.setMaximum(100);

		JLabel progress = new JLabel("progress");
		frame.getContentPane().add(progress, BorderLayout.CENTER);

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
