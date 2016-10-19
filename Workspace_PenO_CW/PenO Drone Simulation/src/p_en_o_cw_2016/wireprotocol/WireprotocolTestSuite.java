package p_en_o_cw_2016.wireprotocol;

import java.io.*;
import java.util.Random;
import p_en_o_cw_2016.*;

class WireProtocolTestSuite {
    public static void assert_(boolean b) { if (!b) throw new AssertionError(); }
    public static void main(String[] args) throws IOException, InterruptedException {
        PipedOutputStream testbedOutputStream = new PipedOutputStream();
        PipedInputStream testbedInputStream = new PipedInputStream();
        PipedInputStream autopilotInputStream = new PipedInputStream(testbedOutputStream);
        PipedOutputStream autopilotOutputStream = new PipedOutputStream(testbedInputStream);

        long seed = System.currentTimeMillis();
        class AutopilotThread extends Thread {
            boolean success;
            public void run() {
                try {
                    DataInputStream is = new DataInputStream(autopilotInputStream);
                    DataOutputStream os = new DataOutputStream(autopilotOutputStream);

                    Random random = new Random(seed);

                    TestbedStub.run(is, os, new AutopilotFactory() {
                        void checkInt(int x) { assert_(x == random.nextInt()); }
                        void checkFloat(float x) { assert_(x == random.nextFloat()); }
                        void checkImage(int[] pixels, int expectedCount) {
                            assert_(pixels.length == expectedCount);
                            for (int i = 0; i < pixels.length; i++) {
                                assert_(pixels[i] == random.nextInt(0x1000000));
                            }
                        }
                        public Autopilot create(Drone drone) {
                            checkFloat(drone.getLeftCamera().getHorizontalAngleOfView());
                            checkFloat(drone.getLeftCamera().getVerticalAngleOfView());
                            int width, height;
                            assert_((width = drone.getLeftCamera().getWidth()) == random.nextInt(300));
                            assert_((height = drone.getLeftCamera().takeImage().length/drone.getLeftCamera().getWidth()) == random.nextInt(10));

                            checkFloat(drone.getCameraSeparation());
                            checkFloat(drone.getWeight());
                            checkFloat(drone.getGravity());
                            checkFloat(drone.getDrag());
                            checkFloat(drone.getMaxThrust());
                            checkFloat(drone.getMaxPitchRate());
                            checkFloat(drone.getMaxRollRate());
                            checkFloat(drone.getMaxYawRate());

                            Autopilot autopilot = () -> {
                                checkImage(drone.getLeftCamera().takeImage(), width * height);
                                checkImage(drone.getRightCamera().takeImage(), width * height);
                                checkFloat(drone.getPitch());
                                checkFloat(drone.getRoll());
                                checkFloat(drone.getCurrentTime());

                                drone.setPitchRate(random.nextFloat());
                                drone.setRollRate(random.nextFloat());
                                drone.setYawRate(random.nextFloat());
                                drone.setThrust(random.nextFloat());
                            };

                            autopilot.timeHasPassed();
                            return autopilot;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                success = true;
            }
        }
        AutopilotThread t = new AutopilotThread();
        t.start();

        {
            DataOutputStream os = new DataOutputStream(testbedOutputStream);
            DataInputStream is = new DataInputStream(testbedInputStream);
            Random random = new Random(seed);
            class TestDrone implements Drone {
                float horizontalAngleOfView = random.nextFloat();
                float verticalAngleOfView = random.nextFloat();
                int width = random.nextInt(300);
                int height = random.nextInt(10);

                float cameraSeparation = random.nextFloat();
                float weight = random.nextFloat();
                float gravity = random.nextFloat();
                float drag = random.nextFloat();
                float maxThrust = random.nextFloat();
                float maxPitchRate = random.nextFloat();
                float maxRollRate = random.nextFloat();
                float maxYawRate = random.nextFloat();

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
                        public int[] takeImage() { return image.clone(); }
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

                void nextPixels(int[] pixels) {
                    for (int i = 0; i < pixels.length; i++) {
                        pixels[i] = random.nextInt(0x1000000);
                    }
                }

                void checkFloat(float x) { assert_(x == random.nextFloat()); }
                void timeHasPassed(Runnable runnable) {
                    nextPixels(leftImage);
                    nextPixels(rightImage);
                    pitch = random.nextFloat();
                    roll = random.nextFloat();
                    currentTime = random.nextFloat();

                    runnable.run();

                    checkFloat(pitchRate);
                    checkFloat(rollRate);
                    checkFloat(yawRate);
                    checkFloat(thrust);
                }

                Autopilot autopilot;

                void run() {
                    timeHasPassed(() -> { autopilot = AutopilotProxy.createFactory(is, os).create(this); });
                    for (int i = 0; i < 5; i++) {
                        timeHasPassed(() -> autopilot.timeHasPassed());
                    }
                }
            }
            new TestDrone().run();
            os.close();
            is.close();
        }
        t.join();
        if (!t.success) throw new RuntimeException();
        System.out.println("PASS");
    }
}