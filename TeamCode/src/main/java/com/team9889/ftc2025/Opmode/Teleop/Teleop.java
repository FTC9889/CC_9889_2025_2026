package com.team9889.ftc2025.Opmode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;


@TeleOp
public class Teleop extends OpMode {
    Robot mRobot = new Robot();

    boolean altGate = false;
    boolean change = false;
    boolean shootPress = false;
    boolean robotC = false;
    boolean centricPress = false;


    ElapsedTime shoot = new ElapsedTime();
    ElapsedTime shootEait = new ElapsedTime();

    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void start() {
        mRobot.mDrive.startTeleopDrive();
    }

    @Override
    public void loop() {
        mRobot.mDrive.setTeleOpDrive(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                true
        );

//        if (gamepad1.back && !centricPress) {
//            centricPress = true;
//        } else if (gamepad1.back && centricPress) {
//            centricPress = false;
//        }


        if (gamepad1.aWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
        } else if (gamepad1.bWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
        } else if (gamepad1.yWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OUTTAKE);
        }

        if (gamepad1.dpadUpWasPressed()) {
            shootPress = true;
            mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY_CLOSE);
        } else if (gamepad1.dpadRightWasPressed()) {
            mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY_FAR);
            shootPress = false;
        } else if (gamepad1.dpadDownWasPressed()) {
            mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
            mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
            shootPress = false;
            altGate = false;
        }

        if (gamepad1.right_bumper) {
            mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
            altGate = true;
            change = true;
        }

        if (altGate) {
            if (shoot.milliseconds() > 300 && !change) {
                mRobot.mIntake.RequestedGateState = Intake.GateState.CLOSED;
                change = true;
                shoot.reset();
            }
            if (mRobot.mShooter.getShooterVelocity() >= (mRobot.mShooter.getRequestedShooterState().setTargetVelocity() -50)) {
                mRobot.mIntake.RequestedGateState = Intake.GateState.OPEN;
                change = false;
                shoot.reset();
            }
        }


//        if (gamepad1.aWasPressed()) {
//            mRobot.mIntake.setRequestedIntakeState(Intake.IntakeState.ON);
//        } else if (gamepad1.bWasPressed()) {
//            mRobot.mIntake.setRequestedIntakeState(Intake.IntakeState.OFF);
//        } else if (gamepad1.yWasPressed()) {
//            mRobot.mIntake.requestState(Intake.TopLevelState.OUTTAKE);
//        }






        telemetry.addData("IntakeRequestedState", mRobot.mIntake.getRequestedIntakeState());
        telemetry.addData("IntakeCurrentState", mRobot.mIntake.getCurrentIntakeState());
        telemetry.addLine("---------------------------------");

        telemetry.addData("GateRequestedState", mRobot.mIntake.getRequestedGateState());
        telemetry.addData("GateCurrentState", mRobot.mIntake.getCurrentGateState());
        telemetry.addLine("-*--*--*--*--*--*--*--*--*--*--*-");

        telemetry.addData("ShooterRequestedState", mRobot.mShooter.getRequestedShooterState());
        telemetry.addData("ShooterCurrentState", mRobot.mShooter.getCurrentShooterState());
        telemetry.addLine("---------------------------------");

        telemetry.addData("HoodRequestedState", mRobot.mShooter.getRequestedHoodState());
        telemetry.addData("HoodCurrentState", mRobot.mShooter.getCurrentHoodState());

        telemetry.addLine("********************************");
        telemetry.addData("Shooter Velocity", mRobot.mShooter.getShooterVelocity()    );
        telemetry.update();

        //1600

//        mRobot.mIntake.update();
//        mRobot.mShooter.update();
//        mRobot.mDrive.update();
        mRobot.update();
    }
}
