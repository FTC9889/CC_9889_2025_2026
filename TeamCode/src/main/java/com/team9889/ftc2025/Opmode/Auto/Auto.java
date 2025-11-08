package com.team9889.ftc2025.Opmode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.team9889.ftc2025.Constants.RobotConstants;
import com.team9889.ftc2025.Subsystem.EverybotStuff.Robot;

public class Auto extends LinearOpMode {
    Robot mRobot = new Robot();
    RobotConstants mRC = new RobotConstants();

    @Override
    public void runOpMode() throws InterruptedException {
        mRobot.init(hardwareMap);

        waitForStart();

        int tile = 830;

        while (opModeIsActive()) {
            mRobot.mDrive.setPower(0, 0.5, 0);
            sleep(500);
            mRobot.mDrive.brakeAndReset();


            mRobot.mCatapult.setPower(mRC.CATAPULT_UP_POWER);

            if(mRobot.mDrive.getAngle() > -90) {
                mRobot.mDrive.setPower(0, 0, -0.2);
                while (mRobot.mDrive.getAngle() > -94 && opModeIsActive()) {
                    telemetry.addData("gyro", mRobot.mDrive.getAngle());
                    telemetry.update();
                    sleep(10);
                }
                mRobot.mDrive.brakeAndReset();
            }



        }
    }
}
