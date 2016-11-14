package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.world.World;
import simulator.world.World12;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Insets;

public class GUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private static World world;
	private final GridBagConstraints constraintsSpeed, constraintsPosition, constraintsButtonGeneralCameras, constraintsButtonDroneCamera;
	private JLabel position = new JLabel();
	private JLabel speed = new JLabel();
	private JLabel windX, windY, windZ;
	private List<JButton> buttonsGeneralCameras = new ArrayList<>();
	private List<JButton> buttonsDroneCameras = new ArrayList<>();
	private List<JSlider> windSliders = new ArrayList<>();


	/**
	 * Create the panel.
	 */
	public GUI(World world) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
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
		constraintsSpeed = new GridBagConstraints();
		constraintsSpeed.insets = new Insets(1, 1, 1, 1);
		
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
				System.out.println("----simulatorspeed "+currentSpeed);
					//System.out.println("Speed");
					constraintsSpeed.ipady = 20;      //make this component tall
					constraintsSpeed.weightx = 1;
					constraintsSpeed.gridx = 0;
					constraintsSpeed.gridy = 2;
					constraintsSpeed.gridwidth = 3;
					add(speed, constraintsSpeed);
					speed.revalidate();
					speed.repaint();
				}
			}
		});
		timerSpeed.start(); 

		// Position
		constraintsPosition = new GridBagConstraints();
		constraintsPosition.insets = new Insets(1, 1, 1, 1);
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
					position.setText("Position (x, y, z): (" + roundOffPos1 + ", " + roundOffPos2 + ", " + roundOffPos3 + ")" );
					constraintsPosition.ipady = 20;      //make this component tall
					constraintsPosition.weightx = 1;
					constraintsPosition.gridx = 0;
					constraintsPosition.gridy = 3;
					constraintsPosition.gridwidth = 3;
					add(position, constraintsPosition);
					position.revalidate();
					position.repaint();
				}
			}
		});
		timerPosition.start(); 
	
		
		//GrivatySlider
		JPanel panelGravity = new JPanel(new GridLayout(1,2));
		GridBagConstraints constraintsPanelGravity = new GridBagConstraints();
		constraintsPanelGravity.insets = new Insets(1, 1, 1, 1);
		constraintsPanelGravity.gridy = 4;
		int MAXGRAVITY =2000 ;

		panelGravity.add(new JLabel("Gravity: "));

		JSlider gravitySlider = new JSlider(JSlider.HORIZONTAL, 0, MAXGRAVITY, 981);
		gravitySlider.setMajorTickSpacing(100);
		gravitySlider.setPaintTicks(true);

		//Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("0") );
		labelTable.put( new Integer( 981 ), new JLabel("9.81") );
		labelTable.put( new Integer( MAXGRAVITY ), new JLabel("20") );
		gravitySlider.setLabelTable( labelTable );
		gravitySlider.setPaintLabels(true);

		gravitySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JSlider slider = (JSlider) evt.getSource();
				if (!slider.getValueIsAdjusting()) {
					double value = slider.getValue();
					//VALUE moet gedeeld worden door 100 anders veel te groot
					world.getDrones().get(0).setGravity((float)(-value/100));
					//System.out.println("SLIDERGRAVITY: " + -value/100);
				}
			}
		});

		panelGravity.add(gravitySlider);
		add(panelGravity, constraintsPanelGravity);
				
		if(world instanceof World12){
			// Panel WindNams & Sliders

			JPanel panelWindSliders = new JPanel(new GridLayout(2,3));
			GridBagConstraints constraintsPanelWindSliders = new GridBagConstraints();
			constraintsPanelWindSliders.insets = new Insets(1, 1, 1, 1);
			constraintsPanelWindSliders.gridy = 5;

			// Wind-sliders Names
			windX = new JLabel("Wind x-richting: ");
			windY = new JLabel("Wind y-richting: ");
			windZ = new JLabel("Wind z-richting:");
			panelWindSliders.add(windX);
			panelWindSliders.add(windY);
			panelWindSliders.add(windZ);

			// WindSliders 
			int MAXWIND = 10;
			
			//Create the label table
			Hashtable labelTableWind = new Hashtable();
			labelTableWind.put( new Integer( 0 ), new JLabel("-0.5") );
			labelTableWind.put( new Integer( 5 ), new JLabel("0") );
			labelTableWind.put( new Integer( MAXWIND ), new JLabel("0.5") );

			//WindX
			JSlider windXSlider = new JSlider(JSlider.HORIZONTAL, 0, MAXWIND, 5);
			windXSlider.setMajorTickSpacing(1);
			windXSlider.setPaintTicks(true);
			
			windXSlider.setLabelTable( labelTable );
			windXSlider.setPaintLabels(true);

			windXSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider) evt.getSource();
					if (!slider.getValueIsAdjusting()) {
						double value = slider.getValue();
						((World12) world).setWindForceX((float)((value-5)/10));
						//System.out.println("SLIDERX: " + value/10);

					}
				}
			});
			windSliders.add(windXSlider);

			//WindY
			JSlider windYSlider = new JSlider(JSlider.HORIZONTAL, 0, MAXWIND, 5);
			windYSlider.setMajorTickSpacing(1);
			windYSlider.setPaintTicks(true);
			
			windYSlider.setLabelTable( labelTable );
			windYSlider.setPaintLabels(true);

			windYSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider) evt.getSource();
					if (!slider.getValueIsAdjusting()) {
						double value = slider.getValue();
						((World12) world).setWindForceY((float)((value-5)/10));
						//System.out.println("SLIDERY: " + (double)(value/10));

					}
				}
			});
			windSliders.add(windYSlider);

			//WindZ
			JSlider windZSlider = new JSlider(JSlider.HORIZONTAL, 0, MAXWIND, 5);
			windZSlider.setMajorTickSpacing(1);
			windZSlider.setPaintTicks(true);
			
			windZSlider.setLabelTable( labelTable );
			windZSlider.setPaintLabels(true);

			windZSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider) evt.getSource();
					if (!slider.getValueIsAdjusting()) {
						double value = slider.getValue();
						//VALUE moet gedeeld worden door 10 anders veel te groot.
						((World12) world).setWindForceZ((float)((value-5)/10));
						//System.out.println("SLIDERZ: " + value/10);

					}
				}
			});
			windSliders.add(windZSlider);

			panelWindSliders.add(windSliders.get(0));
			panelWindSliders.add(windSliders.get(1));
			panelWindSliders.add(windSliders.get(2));
			add(panelWindSliders, constraintsPanelWindSliders);
		}
	
	}
	
}
