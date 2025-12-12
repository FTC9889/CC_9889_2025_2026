package com.team9889.ftc2025.Opmode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.team9889.ftc2025.Subsystem.Robot;

@Autonomous
@Config
public class Auto extends LinearOpMode {
    Robot mRobot = new Robot();
    @Override
    public void runOpMode() throws InterruptedException {
        mRobot.init(hardwareMap);



        waitForStart();
        while (opModeIsActive()) {
            mRobot.mDrive.setPower(0, 1, 0);
            sleep(500);
            mRobot.mDrive.setPower(0, 0, 0);
        }


    }
}
