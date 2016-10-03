package p_en_o_cw_2016;

public interface Drone {
    float getCameraSeparation();
    Camera getLeftCamera();
    Camera getRightCamera();
    float getWeight();
    float getGravity();
    float getDrag();
    float getMaxThrust();
    float getMaxPitchRate();
    float getMaxRollRate();
    float getMaxYawRate();

    float getPitch();
    float getRoll();
    float getCurrentTime();

    void setPitchRate(float value);
    void setRollRate(float value);
    void setYawRate(float value);
    void setThrust(float value);
}