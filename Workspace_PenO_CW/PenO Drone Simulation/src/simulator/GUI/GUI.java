package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
	private static World world;
	private final GridBagConstraints constraintsButton;
	private JLabel position = new JLabel();
	private JLabel speed = new JLabel();
	private List<JButton> buttons = new ArrayList<>();
	

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
		
		constraintsButton = new GridBagConstraints();
		constraintsButton.insets = new Insets(0, 0, 5, 5);

		for(int i=0; i< world.getGeneralCameras().size(); i++){
			//System.out.println(world.getGeneralCameras().size());
			buttons.add(new JButton("Camera " + (i+1)));
			buttons.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nameButton = e.getActionCommand();
					if(nameButton.equalsIgnoreCase("Camera 1")){
						//System.out.println("Camera 1");
						world.setCurrentCamera(world.getGeneralCameras().get(0));
					}
					else if(nameButton.equalsIgnoreCase("Camera 2")){
						//System.out.println("Camera 2");
						world.setCurrentCamera(world.getGeneralCameras().get(1));
					}
					else if(nameButton.equalsIgnoreCase("Camera 3")){
						//System.out.println("Camera 3");
						world.setCurrentCamera(world.getGeneralCameras().get(2));
					}
				}
			});
			add(buttons.get(i), constraintsButton);
		}



		// Speed
		Timer timerSpeed = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(world.getDrones().size() > 0){
					
					//Speed: a*t)
					float currentSpeed;
					float[] acceleration = world.getDrones().get(0).getPhysics().getAcceleration();
					float time = world.getCurrentTime(); //Insert the time of the drone 
					//System.out.println(world.getCurrentTime());
					acceleration[0] *= time;
					acceleration[1] *= time;
					acceleration[2] *= time;
					currentSpeed = (float) Math.sqrt(acceleration[0]*acceleration[0]+acceleration[1]*acceleration[1]+acceleration[2]*acceleration[2]);
					speed.setText("Speed: " + currentSpeed);
					//System.out.println("Speed");
					s.ipady = 50;      //make this component tall
					s.weightx = 0.0;
					s.gridx = 0;
					s.gridy = 1;
					s.gridwidth = 3;
					add(speed, s);
					speed.revalidate();
					speed.repaint();
				}
			}
		});
		timerSpeed.start(); 

		// Position
		Timer timerPosition = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(world.getDrones().size()>0){
					double[] currentPosition = world.getDrones().get(0).getMovement().getCurrentPosition();
					System.out.println(currentPosition);
					position.setText("Position (x,y,z): (" + currentPosition[0] + ", " + currentPosition[1] + ", " + currentPosition[2] + ")" );
					p.ipady = 50;      //make this component tall
					p.weightx = 0.0;
					p.gridx = 0;
					p.gridy = 2;
					p.gridwidth = 3;
					add(position, p);
					position.revalidate();
					position.repaint();
				}
			}
		});
		timerPosition.start(); 
    }
}
