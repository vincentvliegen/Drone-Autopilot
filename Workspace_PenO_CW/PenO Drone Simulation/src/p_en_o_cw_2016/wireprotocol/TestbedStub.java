package p_en_o_cw_2016.wireprotocol;

import java.io.*;
import java.util.function.Consumer;
import p_en_o_cw_2016.*;

class DroneStub0 {
    DataInputStream is;
    DataOutputStream os;
    DroneStub0(DataInputStream is, DataOutputStream os) {
        this.is = is;
        this.os = os;
    }
}
class DroneStub extends DroneStub0 implements Drone {
    DroneStub(DataInputStream is, DataOutputStream os) throws IOException {
        super(is, os);
    }
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
        os.flush();
    }
}

public class TestbedStub {
    /** Starts a stub. Call this method from the AWT/Swing GUI thread. Calls the autopilot factory and the autopilot in the AWT/Swing GUI thread.
        Calls simulationEnded.run() in the AWT/Swing GUI thread when the simulation has ended.
        Calls exceptionHandler.accept() in the AWT/Swing GUI thread in case an exception happens after this method returns. */
    public static void start(InputStream is, OutputStream os, AutopilotFactory autopilotFactory, Runnable simulationEnded, Consumer<Throwable> exceptionHandler) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);

        class Runner extends Thread {
            DroneStub stub = new DroneStub(dis, dos);
            Autopilot autopilot;
            Runner() throws IOException {
                stub.timeHasPassed(() -> { autopilot = autopilotFactory.create(stub); });
            }
            public void run() {
                try {
                    try {
                        for (;;) {
                            // Block until one byte is available.
                            dis.mark(1);
                            dis.read();
                            dis.reset();

                            java.awt.EventQueue.invokeAndWait(() -> {
                                try {
                                    stub.timeHasPassed(() -> { autopilot.timeHasPassed(); });
                                } catch (IOException e) {
                                    exceptionHandler.accept(e);
                                }
                            });
                        }
                    } catch (EOFException e) {
                    }
                    java.awt.EventQueue.invokeLater(simulationEnded);
                } catch (Throwable t) {
                    java.awt.EventQueue.invokeLater(() -> { exceptionHandler.accept(t); });
                }
            }
        }
        new Runner().start();
    }
    /** Runs the stub in the current thread. Blocks the current thread. Do not use if the autopilotFactory shows an AWT/Swing GUI in the current process. */
    public static void run(DataInputStream is, DataOutputStream os, AutopilotFactory autopilotFactory) throws IOException {
        class Runner {
            Runner() throws IOException {}
            DroneStub stub = new DroneStub(is, os);
            Autopilot autopilot;
            void run() throws IOException {
                stub.timeHasPassed(() -> { autopilot = autopilotFactory.create(stub); });
                try {
                    for (;;) {
                        stub.timeHasPassed(() -> { autopilot.timeHasPassed(); });
                    }
                } catch (EOFException e) {
                }
            }
        }
        new Runner().run();
    }
}