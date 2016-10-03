package p_en_o_cw_2016;

public interface Camera {
    float getHorizontalAngleOfView();
    float getVerticalAngleOfView();
    int getWidth();
    int[] takeImage();
}