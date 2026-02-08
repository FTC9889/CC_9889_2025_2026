package com.team9889.ftc2025.test.WillUse;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter;
@Config
//@TeleOp
//https://www.youtube.com/watch?v=aPNCpZzCTKg    7:00
public class ShooterPID extends OpMode {
    Robot mRobot = new Robot();

    public static double F = 0;
    public static double P = 0;

    @Override
    public void init() {
        mRobot.init(hardwareMap);
        telemetry.addLine("init complete");

    }

    public void loop() {
        if (gamepad1.yWasPressed()) {
            mRobot.mShooter.requestState(Shooter.TopLevelState.READY_FAR);
        }

//        if (gamepad1.yWasPressed()) {
//            mRobot.mShooter.requestState(com.team9889.ftc2025.Subsystem.Shooter.TopLevelState.READY_FAR);
//            mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
//        } else if (gamepad1.xWasPressed()) {
//            mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
//            mRobot.mShooter.requestState(com.team9889.ftc2025.Subsystem.Shooter.TopLevelState.DEFAULT);
//        }


        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);



        mRobot.update();
    }
}
