package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField txtSecondTest;

	/**
	 * Create the panel.
	 */
	public GUI() {
		setLayout(new GridLayout(2, 2, 0, 0));
		
		JButton btnTest = new JButton("Test");
		add(btnTest);
		
		txtSecondTest = new JTextField();
		txtSecondTest.setText("second test");
		add(txtSecondTest);
		txtSecondTest.setColumns(10);

	}

}
