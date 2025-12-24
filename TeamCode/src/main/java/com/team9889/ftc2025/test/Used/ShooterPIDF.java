package com.team9889.ftc2025.test.Used;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.team9889.ftc2025.Subsystem.Robot;

//https://www.youtube.com/watch?v=aPNCpZzCTKg    7:00
public class ShooterPIDF extends OpMode {
    Robot mRobot = new Robot();

    DcMotor Shooter;

    public double highVelocity = 1500;
    public double lowVelocity = 900;

    double curTargetVel = highVelocity;

    double F = 0;
    double P = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};

    int stepIndex = 1;
    @Override
    public void init() {
        mRobot.init(hardwareMap);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

    }

    @Override
    public void loop() {

    }
}
