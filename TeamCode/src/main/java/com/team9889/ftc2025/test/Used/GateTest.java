package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
@TeleOp
public class GateTest extends OpMode {
    Robot mRobot = new Robot();
    public static double gatePosition = 0.7;
    public static double turrentPosition = 0.1;
    public static double hoodPosition = 0.12;
    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mIntake.setGatePosition(gatePosition);
    }
}
