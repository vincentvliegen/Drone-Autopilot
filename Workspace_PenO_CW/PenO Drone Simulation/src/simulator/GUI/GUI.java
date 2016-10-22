package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public GUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// First button
		JButton camera1 = new JButton("Camera 1");
		camera1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Code for action
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		add(camera1, c);
		
		// Second button
		JButton camera2 = new JButton("Camera 2");
		camera2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// CODE
			}
		});c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		add(camera2, c);
		
		// Speed
		JLabel speed = new JLabel();
		speed.setText("Speed: ");
		c.fill = GridBagConstraints.HORIZONTAL;
	    //c.ipady = 40;      //make this component tall
	    c.weightx = 0.0;
	    c.gridx = 0;
	    c.gridy = 1;
	    add(speed, c);
		
		// Position
		JLabel position = new JLabel();
		position.setText("Position: ");
		c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipady = 100;      //make this component tall
	    c.weightx = 0.0;
	    c.gridx = 1;
	    c.gridy = 1;
	    add(position, c);

	}

}
