package simulatietestjes;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class SimulationGUI extends JInternalFrame {
	private JTextField txtHallo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulationGUI frame = new SimulationGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimulationGUI() {
		setBounds(100, 100, 450, 300);
		
		txtHallo = new JTextField();
		txtHallo.setText("Hallo!");
		getContentPane().add(txtHallo, BorderLayout.CENTER);
		txtHallo.setColumns(10);

	}

}
