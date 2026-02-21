package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

@TeleOp
@Config
public class ShooterPID extends OpMode {
    Robot mRobot = new Robot();

    public static double F = 0;
    public static double P = 0;

    public static double shooterVel = 0;
    public static double turretPos = 0;
    public static double hoodPos = 0;
    @Override
    public void init() {
        mRobot.init(hardwareMap);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        mRobot.mShooter.shooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        mRobot.mShooter.shooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

    }

    @Override
    public void loop() {
        mRobot.mShooter.setShooterVelocity(shooterVel);
        mRobot.mShooter.setHoodPosition(hoodPos);
        mRobot.mShooter.runT2Angle(turretPos);

        if (gamepad1.aWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
        } else  if (gamepad1.bWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
        }

        mRobot.update();
    }
}
