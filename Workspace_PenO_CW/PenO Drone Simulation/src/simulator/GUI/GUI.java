package simulator.GUI;

import javax.swing.JPanel;
import simulator.world.World;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Insets;

public class GUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private World world;
	
	/**
	 * Create the panel.
	 */
	public GUI(World world) {
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
		this.world = world;
		
		
		// #buttons
		List<JButton> buttons = new ArrayList<>();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 5);
		
		for(int i=0; i< world.getGeneralCameras().size(); i++){
			buttons.add(new JButton("Camera " + (i+1)));
			buttons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Code for action
				}
			});
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.5;
			c.gridx = 0;
			c.gridy = 0;
			add(buttons.get(i), c);
		}

		
		/*// First button
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
		*/
		
		// Speed
		JLabel speed = new JLabel();
		if(world.getDrones().size()>0){
			
			//insert calculation of speed.
			
			speed.setText("Speed: ");
			//s.ipady = 40;      //make this component tall
			s.weightx = 0.0;
			s.gridx = 0;
			s.gridy = 1;
			add(speed, s);
		}


	    // Position
	    JLabel position = new JLabel();
	    if(world.getDrones().size()>0){
	    	float[] acceleration = world.getDrones().get(0).getPhysics().getAcceleration();
	    	position.setText("Position (x,y,z): (" + acceleration[0] + ", " + acceleration[1] + ", " + acceleration[2] + ")" );
	    	p.ipady = 100;      //make this component tall
	    	p.weightx = 0.0;
	    	p.gridx = 0;
	    	p.gridy = 2;
	    	add(position, p);
	    }
	}
}
