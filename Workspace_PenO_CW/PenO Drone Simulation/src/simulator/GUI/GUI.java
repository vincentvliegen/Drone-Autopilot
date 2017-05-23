package simulator.GUI;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DroneAutopilot.GUI.MissionType;
import simulator.objects.HollowCubePolyhedron;
import simulator.objects.LetterLPolyhedron;
import simulator.objects.PolyhedronType;
import simulator.world.World;
import simulator.world.World11;
import simulator.world.World12;
import simulator.world.World13;
import simulator.world.WorldParser;
import simulator.objects.Pyramid;
import simulator.objects.TwinkleTwinkleLittleStarPolyhedron;
import simulator.objects.PolyhedronType;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
	private final GridBagConstraints constraintsSpeed, constraintsPosition, constraintsComboboxGeneralCameras, constraintsButtonDroneCamera, constraintsAddButton; //constraintsPanelGravity; //constraintsAddButton;
	private JLabel position = new JLabel();
	private JLabel speed = new JLabel();
	private List<JButton> buttonsDroneCameras = new ArrayList<>();
	private List<JButton> buttonsAdd = new ArrayList<>();
	private List<JSlider> windSlidersX = new ArrayList<>();
	private List<JSlider> windSlidersY = new ArrayList<>();
	private List<JSlider> windSlidersZ = new ArrayList<>();
	private List<String> listNameButtons = new ArrayList<>();
	private int MAXWIND = 10;
	int MAXGRAVITY = 2000;
	private int type;
	
	private JLabel windX = new JLabel();
	private JLabel windY = new JLabel();
	private JLabel windZ = new JLabel();

	/**
	 * Create the panel.
	 */
	public GUI(World world) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		this.world = world;

		// #buttonsGeneralCameras
		JPanel panelComboboxGeneralCameras = new JPanel(new GridLayout(2,1));
		constraintsComboboxGeneralCameras = new GridBagConstraints();
		makeConstraints(constraintsComboboxGeneralCameras,new Insets(1, 1, 1, 1), 0, 0);

		JLabel select = new JLabel("Select general camera: ");
		panelComboboxGeneralCameras.add(select);

		for(int i=0; i < world.getGeneralCameras().size(); i++){
			final int j =i;
			listNameButtons.add("camera " + Integer.toString(j+1));
		}

		JComboBox comboboxGeneralCameras = new JComboBox(listNameButtons.toArray());

		for(int i=0; i < world.getGeneralCameras().size(); i++){
			final int j =i;
			comboboxGeneralCameras.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox comboboxGeneralCameras = (JComboBox) e.getSource();
					Object selected = comboboxGeneralCameras.getSelectedItem();
					if(selected.toString().equalsIgnoreCase("Camera " + Integer.toString(j+1))){
						world.setCurrentCamera(world.getGeneralCameras().get(j));
					}
				}
			});
		}
		panelComboboxGeneralCameras.add(comboboxGeneralCameras);
		add(panelComboboxGeneralCameras, constraintsComboboxGeneralCameras);


		// #buttonsDroneCameras
		constraintsButtonDroneCamera = new GridBagConstraints();
		JPanel panelButtonDroneCameras = new JPanel(new GridBagLayout());

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
				if(nameButton.equalsIgnoreCase("Third-person camera")){
					world.setCurrentCamera(world.getDroneCameras().get(1));
				}
			}
		});
		this.makeConstraints(constraintsButtonDroneCamera, new Insets(1, 1, 1, 1), 0, 0, 1, 5, 0, 1);
		panelButtonDroneCameras.add(buttonsDroneCameras.get(0), constraintsButtonDroneCamera);
		this.makeConstraints(constraintsButtonDroneCamera, new Insets(1, 1, 1, 1), 1, 0, 1, 5, 0, 1);
		panelButtonDroneCameras.add(buttonsDroneCameras.get(1), constraintsButtonDroneCamera);
		this.makeConstraints(constraintsButtonDroneCamera,new Insets(1, 1, 1, 1), 0, 1);
		add(panelButtonDroneCameras, constraintsButtonDroneCamera);

		// Speed
		constraintsSpeed = new GridBagConstraints();
		this.makeConstraints(constraintsSpeed, new Insets(1, 1, 1, 1), 0, 2, 1, 5);

		Timer timerSpeed = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ev) {
				if(world.getDrones().size() > 0){
					double currentSpeed;
					float[] velocity = world.getDrones().get(0).getMovement().getVelocity();
					float time = world.getCurrentTime(); //Insert the time of the drone 
					//System.out.println(world.getCurrentTime());
					currentSpeed = Math.sqrt(Math.pow(velocity[0],2)+Math.pow(velocity[1],2)+Math.pow(velocity[2],2));
					BigDecimal bigDecimalSpeed = new BigDecimal(currentSpeed);
					BigDecimal roundOffSpeed = bigDecimalSpeed.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					speed.setText("Speed: " + roundOffSpeed + " m/s");
					//System.out.println("simulatorspeed: "+currentSpeed);
					//System.out.println("Speed");
					add(speed, constraintsSpeed);
					speed.revalidate();
					speed.repaint();
				}
			}
		});
		timerSpeed.start(); 

		// Position
		constraintsPosition = new GridBagConstraints();
		this.makeConstraints(constraintsPosition, new Insets(1, 1, 1, 1), 0, 3, 1, 5);
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
					//System.out.println("----distanceSimulator " + Math.sqrt(Math.pow(currentPosition[0], 2)+Math.pow(currentPosition[1], 2)+Math.pow(-10-currentPosition[2], 2)));
					add(position, constraintsPosition);
					position.revalidate();
					position.repaint();
				}
			}
		});
		timerPosition.start(); 


//		//GrivatySlider
//		constraintsPanelGravity = new GridBagConstraints();
//		this.makeConstraints(constraintsPanelGravity, new Insets(1, 1, 1, 1), 0, 0, 1, 0, 0, 0.6);
//		JPanel panelGravity = new JPanel(new GridBagLayout());
//
//		panelGravity.add(new JLabel("Gravity:               "), constraintsPanelGravity);
//
//		//Create the label table
//		Hashtable labelTable = new Hashtable();
//		labelTable.put( new Integer( 0 ), new JLabel("0") );
//		labelTable.put( new Integer( 981 ), new JLabel("9.81") );
//		labelTable.put( new Integer( MAXGRAVITY ), new JLabel("20") );
//
//		JSlider gravitySlider = new JSlider(JSlider.HORIZONTAL, 0, MAXGRAVITY, 981);
//		this.createSlider(gravitySlider, 200, labelTable);
//		
//		gravitySlider.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent evt) {
//				JSlider slider = (JSlider) evt.getSource();
//				if (!slider.getValueIsAdjusting()) {
//					double value = slider.getValue();
//					//VALUE moet gedeeld worden door 100 anders veel te groot
//					world.getDrones().get(0).setGravity((float)(-value/100));
//					//System.out.println("SLIDERGRAVITY: " + -value/100);
//				}
//			}
//		});
//
//		this.makeConstraints(constraintsPanelGravity, new Insets(1, 1, 1, 1), 1, 0, 1, 0, 0, 0.4);
//		panelGravity.add(gravitySlider, constraintsPanelGravity);
//		this.makeConstraints(constraintsPanelGravity, new Insets(1, 1, 1, 1), 0, 4);
//		add(panelGravity, constraintsPanelGravity);

		if(world instanceof World13 || world instanceof World12){
			// Panel WindNams & Sliders
			GridBagConstraints constraintsPanelWindSlidersX = new GridBagConstraints();
			JPanel panelWindSlidersX = new JPanel(new GridBagLayout());
			this.makeConstraints(constraintsPanelWindSlidersX, new Insets(1, 1, 1, 1), 0, 0, 1, 0, 0, 0.6);

			GridBagConstraints constraintsPanelWindSlidersY = new GridBagConstraints();
			JPanel panelWindSlidersY = new JPanel(new GridBagLayout());
			this.makeConstraints(constraintsPanelWindSlidersY, new Insets(1, 1, 1, 1), 0, 0, 1, 0, 0, 0.6);

			GridBagConstraints constraintsPanelWindSlidersZ = new GridBagConstraints();
			JPanel panelWindSlidersZ= new JPanel(new GridBagLayout());
			this.makeConstraints(constraintsPanelWindSlidersZ, new Insets(1, 1, 1, 1), 0, 0, 1, 0, 0, 0.6);

			// Wind-sliders Names
			panelWindSlidersX.add(new JLabel("Wind x-richting: "), constraintsPanelWindSlidersX);
			panelWindSlidersY.add(new JLabel("Wind y-richting: "), constraintsPanelWindSlidersY);
			panelWindSlidersZ.add(new JLabel("Wind z-richting: "), constraintsPanelWindSlidersZ);

			// WindSliders 
			int MAXWIND = 10;

			//Create the label table
			Hashtable labelTableWind = new Hashtable();
			labelTableWind.put( new Integer( 0 ), new JLabel("-0.5") );
			labelTableWind.put( new Integer( 5 ), new JLabel("0") );
			labelTableWind.put( new Integer( MAXWIND ), new JLabel("0.5") );

			//WindX
			JSlider windXSlider = new JSlider(JSlider.HORIZONTAL, 0, MAXWIND, 5);
			this.createSlider(windXSlider, 1, labelTableWind);

			windXSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider) evt.getSource();
					if (!slider.getValueIsAdjusting()) {
						double value = slider.getValue();
						world.setWindSpeedX((value-5)/10);
					}
				}
			});
			windSlidersX.add(windXSlider);

			//WindY
			JSlider windYSlider = new JSlider(JSlider.HORIZONTAL, 0, MAXWIND, 5);
			this.createSlider(windYSlider, 1, labelTableWind);

			windYSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider) evt.getSource();
					if (!slider.getValueIsAdjusting()) {
						double value = slider.getValue();
						world.setWindSpeedY((value-5)/10);
					}
				}
			});
			windSlidersY.add(windYSlider);

			//WindZ
			JSlider windZSlider = new JSlider(JSlider.HORIZONTAL, 0, MAXWIND, 5);
			this.createSlider(windZSlider, 1, labelTableWind);

			windZSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					JSlider slider = (JSlider) evt.getSource();
					if (!slider.getValueIsAdjusting()) {
						double value = slider.getValue();
						world.setWindSpeedZ((value-5)/10);
					}
				}
			});
			windSlidersZ.add(windZSlider);

			this.makeConstraints(constraintsPanelWindSlidersX, new Insets(1, 1, 1, 1), 1, 0, 1, 0, 0, 0.4);
			panelWindSlidersX.add(windSlidersX.get(0), constraintsPanelWindSlidersX);
			this.makeConstraints(constraintsPanelWindSlidersY, new Insets(1, 1, 1, 1), 1, 0, 1, 0, 0, 0.4);
			panelWindSlidersY.add(windSlidersY.get(0), constraintsPanelWindSlidersY);
			this.makeConstraints(constraintsPanelWindSlidersZ, new Insets(1, 1, 1, 1), 1, 0, 1, 0, 0, 0.4);
			panelWindSlidersZ.add(windSlidersZ.get(0), constraintsPanelWindSlidersZ);
			this.makeConstraints(constraintsPanelWindSlidersX, new Insets(1, 1, 1, 1), 0, 5);
			add(panelWindSlidersX, constraintsPanelWindSlidersX);
			this.makeConstraints(constraintsPanelWindSlidersY, new Insets(1, 1, 1, 1), 0, 6);
			add(panelWindSlidersY, constraintsPanelWindSlidersY);
			this.makeConstraints(constraintsPanelWindSlidersZ, new Insets(1, 1, 1, 1), 0, 7);
			add(panelWindSlidersZ, constraintsPanelWindSlidersZ);
		}
		
		if(world instanceof WorldParser) {
			GridBagConstraints constraintsWindX = new GridBagConstraints();
			GridBagConstraints constraintsWindY = new GridBagConstraints();
			GridBagConstraints constraintsWindZ = new GridBagConstraints();
			this.makeConstraints(constraintsWindX, new Insets(1, 1, 1, 1), 0, 5,1,5);
			this.makeConstraints(constraintsWindY, new Insets(1, 1, 1, 1), 0, 6,1,5);
			this.makeConstraints(constraintsWindZ, new Insets(1, 1, 1, 1), 0, 7,1,5);
			Timer timerWind = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
						windX.setText("Wind force X-axis: " + Math.round(world.getWindSpeedX()*100)/100.00 + " N");
						windY.setText("Wind force Y-axis: " + Math.round(world.getWindSpeedY()*100)/100.00 + " N" );
						windZ.setText("Wind force Z-axis: " + Math.round(world.getWindSpeedZ()*100)/100.00 + " N");
						add(windX, constraintsWindX);
						add(windY, constraintsWindY);
						add(windZ, constraintsWindZ);
						windX.revalidate();
						windX.repaint();
						windY.revalidate();
						windY.repaint();
						windZ.revalidate();
						windZ.repaint();
					
				}
			});
			timerWind.start(); 
		}

		// #AddButtons
				constraintsAddButton = new GridBagConstraints();
				JPanel PanelAddButton = new JPanel(new GridBagLayout());

				buttonsAdd.add(new JButton("Add new polyhedron"));
				buttonsAdd.add(new JButton("Add new obstacle"));
				buttonsAdd.get(0).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String nameButton = e.getActionCommand();
						if(nameButton.equalsIgnoreCase("Add new polyhedron")){
							JFrame addPolyhedron = new JFrame();
							createAddPolyhedron(addPolyhedron);
						}
					}
				});
				buttonsAdd.get(1).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String nameButton = e.getActionCommand();
						if(nameButton.equalsIgnoreCase("Add new obstacle")){
							JFrame AddObstaclePolyhedron = new JFrame();
							createAddObstaclePolyhedron(AddObstaclePolyhedron);
						}
					}
				});
				this.makeConstraints(constraintsAddButton, new Insets(1, 1, 1, 1), 0, 0, 1, 5, 0, 1);
				PanelAddButton.add(buttonsAdd.get(0), constraintsAddButton);
				this.makeConstraints(constraintsAddButton, new Insets(1, 1, 1, 1), 1, 0, 1, 5, 0, 1);
				PanelAddButton.add(buttonsAdd.get(1), constraintsAddButton);
				this.makeConstraints(constraintsAddButton,new Insets(1, 1, 1, 1), 0, 8);
				add(PanelAddButton, constraintsAddButton);

		//resize
		this.setPreferredSize(new Dimension(390, 768));
	}

	public void createSlider(JSlider windSlider,int majorTickSpacing, Hashtable labelTableWind){
		windSlider.setMajorTickSpacing(majorTickSpacing);
		windSlider.setPaintTicks(true);
		windSlider.setLabelTable(labelTableWind);
		windSlider.setPaintLabels(true);
	}

	public void createAddPolyhedron(JFrame addPolyhedron){
		addPolyhedron.setTitle("Add new polyhedron");
		addPolyhedron.setAlwaysOnTop(true);
		addPolyhedron.setBounds(100, 300, 600, 200);

		
		Font font = new Font("Tahoma", Font.PLAIN, 18);
		JPanel panel = new JPanel(new GridLayout(1,2));

		JLabel select = new JLabel("Select type: ");
		select.setFont(font);
		panel.add(select);
		

		String[] list = { " --- Select Type --- ", "Pyramid", "Hollow cube", "Letter L", "Star"};
		JComboBox menu = new JComboBox(list);
		panel.add(menu);
		menu.setFont(font);
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox menu = (JComboBox) e.getSource();
				Object selected = menu.getSelectedItem();
				if (selected.toString().equals("Pyramid")){
					setType(1);
				}else if(selected.toString().equals("Hollow cube")) {
					setType(2);
				}else if (selected.toString().equals("Letter L")){					
					setType(3);
				}else if (selected.toString().equals("Star")){					
					setType(4);
				}
			}
		});
		
		GridBagConstraints constraintsAddSphere = new GridBagConstraints();
		JPanel panelAddSphere = new JPanel(new GridBagLayout());


		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 1, 0, 7, 0, 0, 1);
		panelAddSphere.add(panel, constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 0, 1, 1, 0, 0, 1);
		panelAddSphere.add(new JLabel("x: "), constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 1, 1, 1, 0, 0, 1);
		JTextField userTextX = new JTextField(5);
		panelAddSphere.add(userTextX, constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 3, 1, 1, 0, 0, 1);
		panelAddSphere.add(new JLabel("y: "), constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 4, 1, 1, 0, 0, 1);
		JTextField userTextY = new JTextField(5);
		panelAddSphere.add(userTextY, constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 6, 1, 1, 0, 0, 1);
		panelAddSphere.add(new JLabel("z: "), constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 7, 1, 1, 0, 0, 1);
		JTextField userTextZ = new JTextField(5);
		panelAddSphere.add(userTextZ, constraintsAddSphere);

		//AddButton
		JButton insideAddButton = new JButton("Add");
		insideAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double positionX = Double.parseDouble(userTextX.getText());
				double positionY = Double.parseDouble(userTextY.getText());
				double positionZ = Double.parseDouble(userTextZ.getText());
				double[] position = {positionX, positionY, positionZ};
				if(getType()==1){
					Pyramid fig = new Pyramid(world, PolyhedronType.TARGET, position);
					world.getWorldObjectList().add(fig);
				}
				if(getType()==2){
					HollowCubePolyhedron fig = new HollowCubePolyhedron(world, PolyhedronType.TARGET, position);
					world.getWorldObjectList().add(fig);
				}
				if(getType()==3){
					LetterLPolyhedron fig = new LetterLPolyhedron(world, PolyhedronType.TARGET, position);
					world.getWorldObjectList().add(fig);
				}
				if(getType()==4){
					TwinkleTwinkleLittleStarPolyhedron fig = new TwinkleTwinkleLittleStarPolyhedron(world, PolyhedronType.TARGET, position);
					world.getWorldObjectList().add(fig);
				}
				addPolyhedron.setVisible(false);
			}
		});
		GridBagConstraints constraintsButton = new GridBagConstraints();
		this.makeConstraints(constraintsButton, new Insets(1, 1, 1, 1), 7, 2, 1, 0, 0, 1);
		panelAddSphere.add(insideAddButton, constraintsButton);

		//CancelButton
		JButton insideCancelButton = new JButton("Cancel");
		insideCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPolyhedron.setVisible(false);
			}
		});
		this.makeConstraints(constraintsButton, new Insets(1, 1, 1, 1), 7, 3, 1, 0, 0, 1);
		panelAddSphere.add(insideCancelButton, constraintsButton);

		addPolyhedron.add(panelAddSphere);
		addPolyhedron.setVisible(true);
	}
	
	public void createAddObstaclePolyhedron(JFrame AddObstaclePolyhedron){
		AddObstaclePolyhedron.setTitle("Add new obstacle polyhedron");
		AddObstaclePolyhedron.setAlwaysOnTop(true);
		AddObstaclePolyhedron.setBounds(100, 300, 600, 200);

		Font font = new Font("Tahoma", Font.PLAIN, 18);
		JPanel panel = new JPanel(new GridLayout(1,2));

		JLabel select = new JLabel("Select type: ");
		select.setFont(font);
		panel.add(select);
		

		String[] list = { " --- Select Type --- ", "Pyramid", "Hollow cube", "Letter L", "Star"};
		JComboBox menu = new JComboBox(list);
		panel.add(menu);
		menu.setFont(font);
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox menu = (JComboBox) e.getSource();
				Object selected = menu.getSelectedItem();
				if (selected.toString().equals("Pyramid")){
					setType(1);
				}else if(selected.toString().equals("Hollow cube")) {
					setType(2);
				}else if (selected.toString().equals("Letter L")){					
					setType(3);
				}else if (selected.toString().equals("Star")){					
					setType(4);
				}
			}
		});
		
		GridBagConstraints constraintsAddSphere = new GridBagConstraints();
		JPanel panelAddSphere = new JPanel(new GridBagLayout());


		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 1, 0, 7, 0, 0, 1);
		panelAddSphere.add(panel, constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 0, 1, 1, 0, 0, 1);
		panelAddSphere.add(new JLabel("x: "), constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 1, 1, 1, 0, 0, 1);
		JTextField userTextX = new JTextField(5);
		panelAddSphere.add(userTextX, constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 3, 1, 1, 0, 0, 1);
		panelAddSphere.add(new JLabel("y: "), constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 4, 1, 1, 0, 0, 1);
		JTextField userTextY = new JTextField(5);
		panelAddSphere.add(userTextY, constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 6, 1, 1, 0, 0, 1);
		panelAddSphere.add(new JLabel("z: "), constraintsAddSphere);
		this.makeConstraints(constraintsAddSphere, new Insets(1, 1, 1, 1), 7, 1, 1, 0, 0, 1);
		JTextField userTextZ = new JTextField(5);
		panelAddSphere.add(userTextZ, constraintsAddSphere);

		//AddButton
		JButton insideAddButton = new JButton("Add");
		insideAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double positionX = Double.parseDouble(userTextX.getText());
				double positionY = Double.parseDouble(userTextY.getText());
				double positionZ = Double.parseDouble(userTextZ.getText());
				double[] position = {positionX, positionY, positionZ};
				if(getType()==1){
					Pyramid fig = new Pyramid(world, PolyhedronType.OBSTACLE, position);
					world.getWorldObjectList().add(fig);
				}
				if(getType()==2){
					HollowCubePolyhedron fig = new HollowCubePolyhedron(world, PolyhedronType.OBSTACLE, position);
					world.getWorldObjectList().add(fig);
				}
				if(getType()==3){
					LetterLPolyhedron fig = new LetterLPolyhedron(world, PolyhedronType.OBSTACLE, position);
					world.getWorldObjectList().add(fig);
				}
				if(getType()==4){
					TwinkleTwinkleLittleStarPolyhedron fig = new TwinkleTwinkleLittleStarPolyhedron(world, PolyhedronType.OBSTACLE, position);
					world.getWorldObjectList().add(fig);
				}
				AddObstaclePolyhedron.setVisible(false);
			}
		});
		GridBagConstraints constraintsButton = new GridBagConstraints();
		this.makeConstraints(constraintsButton, new Insets(1, 1, 1, 1), 7, 2, 1, 0, 0, 1);
		panelAddSphere.add(insideAddButton, constraintsButton);

		//CancelButton
		JButton insideCancelButton = new JButton("Cancel");
		insideCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddObstaclePolyhedron.setVisible(false);
			}
		});
		this.makeConstraints(constraintsButton, new Insets(1, 1, 1, 1), 7, 3, 1, 0, 0, 1);
		panelAddSphere.add(insideCancelButton, constraintsButton);

		AddObstaclePolyhedron.add(panelAddSphere);
		AddObstaclePolyhedron.setVisible(true);
	}
	
	public void makeConstraints(GridBagConstraints name, Insets insets, int gridx, int gridy){
		name.insets=insets;
		name.gridx=gridx;
		name.gridy=gridy;
	}

	public void makeConstraints(GridBagConstraints name, Insets insets, int gridx, int gridy, int gridwidth, int ipady){
		name.insets=insets;
		name.gridx=gridx;
		name.gridy=gridy;
		name.gridwidth=gridwidth;
		name.ipady=ipady;
	}

	public void makeConstraints(GridBagConstraints name, Insets insets, int gridx, int gridy, int gridwidth,int ipadx, int ipady, double weightx){
		name.insets=insets;
		name.gridx=gridx;
		name.gridy=gridy;
		name.gridwidth=gridwidth;
		name.ipadx=ipadx;
		name.ipady=ipady;
		name.weightx=weightx;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	
}
