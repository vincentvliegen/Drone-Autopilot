package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.Timer;

import simulator.world.World;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Insets;

public class GUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private static World world;
	private final GridBagConstraints constraintsButtonGeneralCameras;
	private final GridBagConstraints constraintsButtonDroneCamera;
	private JLabel position = new JLabel();
	private JLabel speed = new JLabel();
	private List<JButton> buttonsGeneralCameras = new ArrayList<>();
	private List<JButton> buttonsDroneCameras = new ArrayList<>();


	/**
	 * Create the panel.
	 */
	public GUI(World world) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		GridBagConstraints s = new GridBagConstraints();
		s.insets = new Insets(1, 1, 1, 1);
		GridBagConstraints p = new GridBagConstraints();
		p.insets = new Insets(1, 1, 1, 1);
		this.world = world;


		// #buttonsGeneralCameras
		JPanel panelButtonsGeneralCameras = new JPanel(new GridLayout(1,3));
		constraintsButtonGeneralCameras = new GridBagConstraints();
		constraintsButtonGeneralCameras.insets = new Insets(1, 1, 1, 1);
		constraintsButtonGeneralCameras.gridy = 0;

		for(int i=0; i< world.getGeneralCameras().size(); i++){
			//System.out.println(world.getGeneralCameras().size());
			buttonsGeneralCameras.add(new JButton("Camera " + (i+1)));
			buttonsGeneralCameras.get(i).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nameButton = e.getActionCommand();
					if(nameButton.equalsIgnoreCase("Camera 1")){
						world.setCurrentCamera(world.getGeneralCameras().get(0));
					}
					else if(nameButton.equalsIgnoreCase("Camera 2")){
						world.setCurrentCamera(world.getGeneralCameras().get(1));
					}
					else if(nameButton.equalsIgnoreCase("Camera 3")){
						world.setCurrentCamera(world.getGeneralCameras().get(2));
					}
				}
			});
			panelButtonsGeneralCameras.add(buttonsGeneralCameras.get(i), constraintsButtonGeneralCameras);
		}
		add(panelButtonsGeneralCameras, constraintsButtonGeneralCameras);

		// #buttonsDroneCameras
		JPanel panelButtonDroneCameras = new JPanel(new GridLayout(1,2));
		constraintsButtonDroneCamera = new GridBagConstraints();
		constraintsButtonDroneCamera.insets = new Insets(1, 1, 1, 1);
		constraintsButtonDroneCamera.gridy = 1;

		
		buttonsDroneCameras.add(new JButton("Left dronecamera"));
		buttonsDroneCameras.add(new JButton("Right dronecamera"));
		buttonsDroneCameras.get(0).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nameButton = e.getActionCommand();
				if(nameButton.equalsIgnoreCase("Left Dronecamera")){
					world.setCurrentCamera(world.getDroneCameras().get(0));
				}
			}
		});
		buttonsDroneCameras.get(1).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nameButton = e.getActionCommand();
				if(nameButton.equalsIgnoreCase("Right Dronecamera")){
					world.setCurrentCamera(world.getDroneCameras().get(1));
				}
			}
		});
		panelButtonDroneCameras.add(buttonsDroneCameras.get(0));
		panelButtonDroneCameras.add(buttonsDroneCameras.get(1));
		add(panelButtonDroneCameras, constraintsButtonDroneCamera);

		// Speed
		Timer timerSpeed = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(world.getDrones().size() > 0){

					//Speed: a*t)
					float currentSpeed;
					float[] velocity = world.getDrones().get(0).getMovement().getVelocity();
					float time = world.getCurrentTime(); //Insert the time of the drone 
					//System.out.println(world.getCurrentTime());
					currentSpeed = (float) Math.sqrt(Math.pow(velocity[0],2)+Math.pow(velocity[1],2)+Math.pow(velocity[2],2));
					BigDecimal bigDecimalSpeed = new BigDecimal(currentSpeed);
					BigDecimal roundOffSpeed = bigDecimalSpeed.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					speed.setText("Speed: " + roundOffSpeed);
					//System.out.println("Speed");
					s.ipady = 50;      //make this component tall
					s.weightx = 1;
					s.gridx = 0;
					s.gridy = 2;
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
					//System.out.println(currentPosition);
					BigDecimal bigDecimalPos1 = new BigDecimal(currentPosition[0]);
					BigDecimal roundOffPos1 = bigDecimalPos1.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal bigDecimalPos2 = new BigDecimal(currentPosition[1]);
					BigDecimal roundOffPos2 = bigDecimalPos2.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal bigDecimalPos3 = new BigDecimal(currentPosition[2]);
					BigDecimal roundOffPos3 = bigDecimalPos3.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					position.setText("Position (x,y,z): (" + roundOffPos1 + ", " + roundOffPos2 + ", " + roundOffPos3 + ")" );
					p.ipady = 50;      //make this component tall
					p.weightx = 1;
					p.gridx = 0;
					p.gridy = 3;
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
