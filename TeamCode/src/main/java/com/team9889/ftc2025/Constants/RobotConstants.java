package com.team9889.ftc2025.Constants;

public class RobotConstants {
    public double INTAKE_IN_POWER = 1.0;
    public double INTAKE_OUT_POWER = -0.9;
    public double INTAKE_OFF_POWER = 0.0;
    public double intakePower = INTAKE_OFF_POWER;

    public double FOOT_UP_POWER = 1.0;
    public double FOOT_DOWN_POWER = -0.85;
    public double FOOT_OFF_POWER = 0.0;
    public double footPower = FOOT_OFF_POWER;

    public double CATAPULT_UP_POWER = -1.0;
    public double CATAPULT_DOWN_POWER = 1.0;

    public enum CatapultModes {UP, DOWN, BRAKE}

    public CatapultModes pivotMode;

    public enum FootMode {UP, DOWN, BRAKE}

    public FootMode footmode;

}
