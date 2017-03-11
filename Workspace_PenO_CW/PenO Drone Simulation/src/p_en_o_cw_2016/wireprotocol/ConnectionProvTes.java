package p_en_o_cw_2016.wireprotocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import DroneAutopilot.DroneAutopilotFactory;

public class ConnectionProvTes {
	public static void main(String[] args) throws UnknownHostException, IOException {
				
		Socket clientSocket = new Socket("localhost", 8000);
		DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
		DataInputStream is = new DataInputStream(clientSocket.getInputStream());

		
		DroneAutopilotFactory droneap = new DroneAutopilotFactory();	
		
		TestbedStub.run(is, os, droneap);
		
		clientSocket.close();
		
		
		
	}
}
