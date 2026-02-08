package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
@TeleOp
public class AngleToHoodPositionTest extends OpMode {
    Robot mRobot = new Robot();
    public static double turretPosition = 0;
    
    /*
    Step1: run the opmode
    Step2: us the configuration bar and change the values
    Step3: start at 0 the move at increments of 0.02-0.04
    Step4: Find the amount of degrees the hood changes by in a change of 0.01 or any value that sounds good to you
    Step5: Find the max and minimum positions the hood servos can be at to not come off the robot and where it doesn't hit the bottom
     */

    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mShooter.hoodL.setPosition(turretPosition);
        mRobot.mShooter.hoodR.setPosition(turretPosition);
    }
}
