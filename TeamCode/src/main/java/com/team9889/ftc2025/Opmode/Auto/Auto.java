package com.team9889.ftc2025.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.team9889.ftc2025.Subsystem.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class Auto extends LinearOpMode {
    Robot mRobot = new Robot();
    @Override
    public void runOpMode() throws InterruptedException {
        mRobot.init(hardwareMap);



        waitForStart();
        while (opModeIsActive()) {

        }


    }
}
