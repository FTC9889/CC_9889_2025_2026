package com.team9889.ftc2025.Opmode.Teleop;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;


@TeleOp
public class Teleop extends OpMode {
    Robot mRobot = new Robot();

    boolean altGate = false;
    boolean shootPress = false;
    boolean press = false;
    boolean robotC = false;
    boolean centricPress = false;

    boolean red = false;




    ElapsedTime shoot = new ElapsedTime();
    ElapsedTime shootWait = new ElapsedTime();

    @Override
    public void init() {
        mRobot.init(hardwareMap);
        mRobot.mDrive.setStartingPose(new Pose(136.5, 8.25,  Math.toRadians(270)));
    }


    @Override
    public void start() {
        mRobot.mDrive.startTeleopDrive();
    }

    @Override
    public void loop() {

        if (gamepad2.back && gamepad2.b) {
            Drive.red = true;
        } else if (gamepad2.back && gamepad2.x) {
            Drive.red = false;
        }

        if (gamepad2.right_stick_button && gamepad2.left_stick_button) {
            Drive.xOffset = mRobot.mDrive.getPose().getX() - 7.5;
            Drive.yOffset = mRobot.mDrive.getPose().getY() - 8.25;
        }


        mRobot.mDrive.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );

        if (gamepad1.leftBumperWasPressed()) {
            if (!press) {
                mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                press = true;
            } else if (press) {
                mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                press = false;
            }
        } else if (gamepad2.yWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OUTTAKE);
}
        if (gamepad2.rightBumperWasPressed()) {
            if (!shootPress) {
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                shootPress = true;
            } else if (shootPress) {
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                shootPress = false;
            }
        }

        if (gamepad1.right_bumper) {
            mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
            press = false;
        }

        /////////////////////////////////////////////////////////////////////////////////////////////
        telemetry.addData("red", Drive.red);
        telemetry.addLine("----------------*----------------");
//
//        telemetry.addData("IntakeRequestedState", mRobot.mIntake.getRequestedIntakeState());
//        telemetry.addData("IntakeCurrentState", mRobot.mIntake.getCurrentIntakeState());
//        telemetry.addLine("---------------------------------");
//
//        telemetry.addData("GateRequestedState", mRobot.mIntake.getRequestedGateState());
//        telemetry.addData("GateCurrentState", mRobot.mIntake.getCurrentGateState());
//        telemetry.addLine("-*--*--*--*--*--*--*--*--*--*--*-");
//
//        telemetry.addData("ShooterRequestedState", mRobot.mShooter.getRequestedShooterState());
//        telemetry.addData("ShooterCurrentState", mRobot.mShooter.getCurrentShooterState());
//        telemetry.addLine("---------------------------------");
//
//        telemetry.addData("HoodRequestedState", mRobot.mShooter.getRequestedHoodState());
//        telemetry.addData("HoodCurrentState", mRobot.mShooter.getCurrentHoodState());
//        telemetry.addLine("---------------------------------");
//
//        telemetry.addData("TurretRequestedState", mRobot.mShooter.getRequestedTurretState());
//        telemetry.addData("TurretCurrentState", mRobot.mShooter.getCurrentTurretState());

        telemetry.addLine("********************************");
        telemetry.addData("Shooter Velocity", mRobot.mShooter.getShooterVelocity());

        telemetry.addData("robotPose", mRobot.mDrive.getPose());

        telemetry.update();
        mRobot.update();
    }
}