package p_en_o_cw_2016.wireprotocol;

import java.io.*;
import p_en_o_cw_2016.*;

public class AutopilotProxy {
    public static AutopilotFactory createFactory(DataInputStream is, DataOutputStream os) {
        return new AutopilotFactory() {
            void writePixels(int[] pixels) throws IOException {
                for (int i = 0; i < pixels.length; i++) {
                    int pixel = pixels[i];
                    os.writeByte(pixel);
                    os.writeByte(pixel >> 8);
                    os.writeByte(pixel >> 16);
                }
            }
            public Autopilot create(Drone drone) {
                try {
                    Camera leftCamera = drone.getLeftCamera();
                    os.writeFloat(leftCamera.getHorizontalAngleOfView());
                    os.writeFloat(leftCamera.getVerticalAngleOfView());
                    int width = leftCamera.getWidth();
                    os.writeInt(width);
                    int[] firstLeftImage = leftCamera.takeImage();
                    os.writeInt(firstLeftImage.length / width);
                    os.writeFloat(drone.getCameraSeparation());
                    os.writeFloat(drone.getWeight());
                    os.writeFloat(drone.getGravity());
                    os.writeFloat(drone.getDrag());
                    os.writeFloat(drone.getMaxThrust());
                    os.writeFloat(drone.getMaxPitchRate());
                    os.writeFloat(drone.getMaxRollRate());
                    os.writeFloat(drone.getMaxYawRate());

                    Autopilot autopilot = () -> {
                        try {
                            writePixels(drone.getLeftCamera().takeImage());
                            writePixels(drone.getRightCamera().takeImage());
                            os.writeFloat(drone.getPitch());
                            os.writeFloat(drone.getRoll());
                            os.writeFloat(drone.getCurrentTime());

                            drone.setPitchRate(is.readFloat());
                            drone.setRollRate(is.readFloat());
                            drone.setYawRate(is.readFloat());
                            drone.setThrust(is.readFloat());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    };

                    autopilot.timeHasPassed();
                    return autopilot;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}