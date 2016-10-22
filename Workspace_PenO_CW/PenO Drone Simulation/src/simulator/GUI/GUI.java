package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Insets;

public class GUI extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public GUI() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(0, 0, 5, 5);
		GridBagConstraints c2 = new GridBagConstraints();
		c2.insets = new Insets(0, 0, 5, 5);
		GridBagConstraints c3 = new GridBagConstraints();
		c3.insets = new Insets(0, 0, 5, 5);
		GridBagConstraints s = new GridBagConstraints();
		s.insets = new Insets(0, 0, 5, 5);
		GridBagConstraints p = new GridBagConstraints();
		p.insets = new Insets(0, 0, 5, 5);
		
		
		// First button
		JButton camera1 = new JButton("Camera 1");
		camera1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Code for action
			}
		});
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.weightx = 0.5;
		c1.gridx = 0;
		c1.gridy = 0;
		add(camera1, c1);
		
		
		// Second button
		JButton camera2 = new JButton("Camera 2");
		camera2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// CODE
			}
		});
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.weightx = 0.5;
		c2.gridx = 1;
		c2.gridy = 0;
		add(camera2, c2);
		
		// Third button
				JButton camera3 = new JButton("Camera 3");
				camera3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// CODE
					}
				});
				c3.fill = GridBagConstraints.HORIZONTAL;
				c3.weightx = 0.5;
				c3.gridx = 2;
				c3.gridy = 0;
				add(camera3, c3);
		
		
		// Speed
		JLabel speed = new JLabel();
		speed.setText("Speed: ");
	    //s.ipady = 40;      //make this component tall
	    s.weightx = 0.0;
	    s.gridx = 0;
	    s.gridy = 1;
	    add(speed, s);
	    
	    
	    // Position
	    JLabel position = new JLabel();
	    position.setText("Position: ");
	    p.ipady = 100;      //make this component tall
	    p.weightx = 0.0;
	    p.gridx = 0;
	    p.gridy = 2;
	    add(position, p);
	}
}
