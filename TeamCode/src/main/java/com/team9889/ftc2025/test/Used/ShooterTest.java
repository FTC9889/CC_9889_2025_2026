package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
@TeleOp
public class ShooterTest extends OpMode {
    Robot mRobot = new Robot();
    public static double shooterVelocity = 0;
    public static double turrentPosition = 0.1;
    public static double hoodPosition = 0.12;
    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {

        mRobot.mShooter.setShooterVelocity(shooterVelocity);
        mRobot.mShooter.setHoodPosition(MathFunctions.clamp(hoodPosition, 0, 0.125));
        mRobot.mShooter.runT2Angle(turrentPosition);

        if (gamepad1.aWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
        } else if (gamepad1.bWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
        } else if (gamepad1.yWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OUTTAKE);
        }

        telemetry.addLine("press A to turn intake to shoot mode");
        telemetry.addLine("press B to turn intake off");
        telemetry.addLine("press Y to turn intake to outtake");
        telemetry.update();

        mRobot.update();


    }
}
