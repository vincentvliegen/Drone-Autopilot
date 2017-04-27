package DroneAutopilot.GUI;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI {

	private final JProgressBar progressBar;
	public int maxValue;
	private JFrame frame;
	private boolean reached;
	private MissionType missionType;
	
	
	/**
	 * Create the application.
	 */
	public GUI() {
		this.progressBar = new JProgressBar();
		this.maxValue = 0;
		setMissionType(MissionType.HOVER);
		initialize();
	}

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.getFrame().setTitle("AUTOPILOT GUI");
		this.getFrame().setAlwaysOnTop(true);
		this.getFrame().setBounds(100, 100, 530, 80);
		this.getFrame().setMinimumSize(new Dimension(530,80));
		this.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Font font = new Font("Tahoma", Font.PLAIN, 18);
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panel = new JPanel(new GridLayout(1,2));

		JLabel select = new JLabel("Select mission: ");
		select.setFont(font);
		panel.add(select);

		String[] list = { " ------ Select Mission ------ ", "Fly to position","Fly to multiple positions", "Fly to single target", "Scan object", "TEST"};
		JComboBox menu = new JComboBox(list);
		menu.setPreferredSize(new Dimension(250,30));
		panel.add(menu);
		menu.setFont(font);
		menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox menu = (JComboBox) e.getSource();

				Object selected = menu.getSelectedItem();
				if (selected.toString().equals("Fly to single target")){
					setMissionType(MissionType.SINGLEOBJECT);
				}else if(selected.toString().equals("Scan object")) {
					setMissionType(MissionType.SCANOBJECT);
				}else if (selected.toString().equals("TEST")){					
					setMissionType(MissionType.TEST);
				}else if (selected.toString().equals("Fly to position")){					
					setMissionType(MissionType.FLYTOPOSITION);
				}else if (selected.toString().equals("Fly to multiple positions")){
					setMissionType(MissionType.FLYMULTIPLEPOS);
				}else {
					setMissionType(MissionType.HOVER);
				}
			}
		});

//		JLabel progress = new JLabel("Progress to next sphere: ");
//		progress.setFont(font);
		//		progress.setPreferredSize(new Dimension(80, 30));
//		panel.add(progress);
//
//		this.getProgressBar().setPreferredSize(new Dimension(250, 30));
//		this.getProgressBar().setStringPainted(true);
//		this.getProgressBar().setString("0%");
//		this.getProgressBar().setFont(font);

//		panel.add(this.getProgressBar());

		panel2.add(panel);
		this.getFrame().getContentPane().add(panel2);
		this.getFrame().setVisible(true);

	}

//	public void update(float dist,int colorint) {
//		int distance = (int) (dist*100);
//		Color color = new Color(colorint);
//		if (color.getRGB() != this.getProgressBar().getForeground().getRGB()){
//			this.setMaxValue(distance);
//			this.getProgressBar().setMaximum(distance);
//			this.setReached(false);
//		};
//		this.getProgressBar().setForeground(color);
//		if(this.isReached()){
//			this.getProgressBar().setString("100%");
//		}else{
//			if (distance > this.getMaxValue()) {
//				this.setMaxValue(distance);
//				this.getProgressBar().setMaximum(distance);
//			} else {
//				this.getProgressBar().setValue(this.getMaxValue() - distance);
//			}
//			if (this.getMaxValue() > 0){
//				this.getProgressBar().setString((Math.round(((this.getMaxValue() - distance)*100) / (float)this.getMaxValue())) + "%");
//				if((Math.round(((this.getMaxValue() - distance)*100) / (float)this.getMaxValue()))==100){
//					this.setReached(true);
//				}
//			} else {
//				this.getProgressBar().setString("100%");
//				this.setReached(true);
//			}
//		}
//	}

	
	//////////GETTERS & SETTERS//////////
	
	public boolean isReached() {
		return reached;
	}

	public void setReached(boolean reached) {
		this.reached = reached;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public MissionType getMissionType() {
		return missionType;
	}

	public void setMissionType(MissionType missionType) {
		this.missionType = missionType;
	}

	
	
}
