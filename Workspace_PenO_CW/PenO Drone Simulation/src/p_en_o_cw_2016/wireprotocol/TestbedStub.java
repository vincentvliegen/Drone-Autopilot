package p_en_o_cw_2016.wireprotocol;

import java.io.*;
import p_en_o_cw_2016.*;

public class TestbedStub {
    public static void run(DataInputStream is, DataOutputStream os, AutopilotFactory autopilotFactory) throws IOException {
        class DroneStub implements Drone {
            DroneStub() throws IOException {}
            float horizontalAngleOfView = is.readFloat();
            float verticalAngleOfView = is.readFloat();
            int width = is.readInt();
            int height = is.readInt();
            float cameraSeparation = is.readFloat();
            float weight = is.readFloat();
            float gravity = is.readFloat();
            float drag = is.readFloat();
            float maxThrust = is.readFloat();
            float maxPitchRate = is.readFloat();
            float maxRollRate = is.readFloat();
            float maxYawRate = is.readFloat();

            void readPixels(int[] pixels) throws IOException {
                int byteCount = pixels.length * 3;
                byte[] buffer = new byte[byteCount];
                is.readFully(buffer);
                for (int i = 0, j = 0; i < byteCount;) {
                    int r = buffer[i++] & 0xff;
                    int g = buffer[i++] & 0xff;
                    int b = buffer[i++] & 0xff;
                    pixels[j++] = r | g << 8 | b << 16;
                }
            }

            int[] leftImage = new int[width * height];
            int[] rightImage = new int[width * height];
            float pitch;
            float roll;
            float currentTime;

            float pitchRate;
            float rollRate;
            float yawRate;
            float thrust;

            Camera createCamera(int[] image) {
                return new Camera() {
                    public float getHorizontalAngleOfView() { return horizontalAngleOfView; }
                    public float getVerticalAngleOfView() { return verticalAngleOfView; }
                    public int getWidth() { return width; }
                    public int[] takeImage() {
                        int[] img = new int[image.length];
                        System.arraycopy(image, 0, img, 0, image.length);
                        return img;
                    }
                };
            }

            Camera leftCamera = createCamera(leftImage);
            Camera rightCamera = createCamera(rightImage);

            public float getCameraSeparation() { return cameraSeparation; }
            public Camera getLeftCamera() { return leftCamera; }
            public Camera getRightCamera() { return rightCamera; }
            public float getWeight() { return weight; }
            public float getGravity() { return gravity; }
            public float getDrag() { return drag; }
            public float getMaxThrust() { return maxThrust; }
            public float getMaxPitchRate() { return maxPitchRate; }
            public float getMaxRollRate() { return maxRollRate; }
            public float getMaxYawRate() { return maxYawRate; }

            public float getPitch() { return pitch; }
            public float getRoll() { return roll; }
            public float getCurrentTime() { return currentTime; }

            public void setPitchRate(float value) { pitchRate = value; }
            public void setRollRate(float value) { rollRate = value; }
            public void setYawRate(float value) { yawRate = value; }
            public void setThrust(float value) { thrust = value; }

            void timeHasPassed(Runnable runnable) throws IOException {
                readPixels(leftImage);
                readPixels(rightImage);
                pitch = is.readFloat();
                roll = is.readFloat();
                currentTime = is.readFloat();

                runnable.run();

                os.writeFloat(pitchRate);
                os.writeFloat(rollRate);
                os.writeFloat(yawRate);
                os.writeFloat(thrust);
            }

            Autopilot autopilot;

            { timeHasPassed(() -> { autopilot = autopilotFactory.create(this); }); }

            void run() throws IOException {
                try {
                    for(;;)
                        timeHasPassed(() -> autopilot.timeHasPassed());
                } catch (EOFException e) {
                }
            }
        }
        new DroneStub().run();
    }
}