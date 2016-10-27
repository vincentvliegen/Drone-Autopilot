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
	private final GridBagConstraints constraintsButton;
	
	
	/**
	 * Create the panel.
	 */
	public GUI(World world) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		GridBagConstraints s = new GridBagConstraints();
		s.insets = new Insets(0, 0, 5, 5);
		GridBagConstraints p = new GridBagConstraints();
		p.insets = new Insets(0, 0, 5, 5);
		this.world = world;
		
		
		// #buttons
		List<JButton> buttons = new ArrayList<>();
		constraintsButton = new GridBagConstraints();
		constraintsButton.insets = new Insets(0, 0, 5, 5);
		
		for(int i=0; i< world.getGeneralCameras().size(); i++){
			System.out.println(world.getGeneralCameras().size());
			buttons.add(new JButton("Camera " + (i+1)));
			buttons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Code for action
				}
			});
			add(buttons.get(i), constraintsButton);
		}
		
		
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
