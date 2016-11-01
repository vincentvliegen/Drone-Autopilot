package DroneAutopilot.GUI;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
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
		frame.setBounds(100, 100, 530, 115);
		frame.setMinimumSize(new Dimension(530,115));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Font font = new Font("Tahoma", Font.PLAIN, 18);
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panel = new JPanel(new GridLayout(2,2));
		
		JLabel select = new JLabel("Select mission: ");
		select.setFont(font);
		panel.add(select);
		
		String[] list = { " ", "Fly to red orb" };
		JComboBox menu = new JComboBox(list);
		menu.setPreferredSize(new Dimension(250,30));
		panel.add(menu);
		menu.setFont(font);
		menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox menu = (JComboBox) e.getSource();

				Object selected = menu.getSelectedItem();
				if (selected.toString().equals("Fly to red orb")){
					redOrbEnabled = true;
				}
				else if(selected.toString().equals(" "))
					redOrbEnabled = false;
			}
		});

		JLabel progress = new JLabel("Progress: ");
		progress.setFont(font);
//		progress.setPreferredSize(new Dimension(80, 30));
		panel.add(progress);

		this.progressBar.setPreferredSize(new Dimension(250, 30));
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("0%");
		this.progressBar.setFont(font);

		panel.add(this.progressBar);

		panel2.add(panel);
		frame.getContentPane().add(panel2);
		frame.setVisible(true);

	}

	public void update(int distance) {
		if (distance > this.maxValue) {
			this.maxValue = distance;
			this.progressBar.setMaximum(distance);
		} else {
			this.progressBar.setValue(this.maxValue - distance);
		}
		this.progressBar.setString((this.maxValue - distance) / this.maxValue + "%");
	}

	private final JProgressBar progressBar;
	private int maxValue;
	public boolean redOrbEnabled;
}
