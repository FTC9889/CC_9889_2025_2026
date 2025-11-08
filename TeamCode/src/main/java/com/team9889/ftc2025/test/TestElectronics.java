package com.team9889.ftc2025.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Constants.RobotConstants;
import com.team9889.ftc2025.Subsystem.EverybotStuff.Robot;

@TeleOp
public class TestElectronics extends LinearOpMode {
    Robot mRobot = new Robot();
    RobotConstants mRC = new RobotConstants();
    @Override
    public void runOpMode() throws InterruptedException {
        mRobot.init(hardwareMap);

        waitForStart();
        while (opModeIsActive()) {

        }

    }
}
