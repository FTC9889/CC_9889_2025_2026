package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;

@Config
@TeleOp
@Disabled

public class ShooterTest extends OpMode {

    Robot mRobot = new Robot();

    // Dashboard variables
    public static double shooterVelocity = 0;
    public static double turretAngle = 180;
    public static double hoodPosition = 0.12;
    public static double intakePower = 0.6;

    @Override
    public void init() {
        Drive.red = true;
        mRobot.init(hardwareMap);
    }

    @Override
    public void start() {
        mRobot.mDrive.setPose(new Pose(118.304, 129.149, Math.toRadians(220)));
    }

    @Override
    public void loop() {

        // Apply dashboard values
        mRobot.mShooter.setShooterVelocity(shooterVelocity);
        mRobot.mShooter.runT2Angle(turretAngle);
        mRobot.mShooter.setHoodPosition(hoodPosition);

        // Intake controls
        if (gamepad1.a) {
            mRobot.mIntake.setIntakePower(intakePower);
            mRobot.mIntake.setRequestedGateState(Intake.GateState.OPEN);
        } else if (gamepad1.b) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
        } else if (gamepad1.y) {
            mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
        }

        // Telemetry
        double distance = mRobot.mShooter.getDistanceFromGoal(mRobot.mDrive.getPose());

        telemetry.addData("Distance From Goal", distance);
        telemetry.addData("Shooter Velocity", shooterVelocity);
        telemetry.addData("Turret Angle", turretAngle);
        telemetry.addData("Hood Position", hoodPosition);
        telemetry.addLine("Press A: Shoot Mode | B: Intake Off | Y: Intake Mode");
        telemetry.addData("current velocity", mRobot.mShooter.shooterR.getVelocity());

        telemetry.update();

        // Update robot
        mRobot.update();
    }
}