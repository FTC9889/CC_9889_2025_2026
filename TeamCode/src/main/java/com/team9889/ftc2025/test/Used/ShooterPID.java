package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

@TeleOp
@Config
public class ShooterPID extends OpMode {
    Robot mRobot = new Robot();
    public DcMotorEx shooterR, shooterL;


    public static double F = 0;
    public static double P = 0;

    public static double shooterVel = 0;
    public static double turretPos = 180;
    public static double hoodPos = 0.055;

    public static double intakeSpeed = 0;

    double[] stepSize = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 1;
    @Override
    public void init() {
        shooterR = hardwareMap.get(DcMotorEx.class, "shooterR");
        shooterL = hardwareMap.get(DcMotorEx.class, "shooterL");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterL.setDirection(DcMotorSimple.Direction.FORWARD);

        shooterL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        P = shooterR.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).p;
        F = shooterR.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER).f;

        mRobot.init(hardwareMap);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        shooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        shooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

    }
    boolean press = false;


    @Override
    public void loop() {

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        shooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        shooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        shooterR.setVelocity(shooterVel);
        shooterL.setVelocity(shooterVel);

        double curretVel = shooterR.getVelocity();
        double error = shooterVel - curretVel;

        telemetry.addLine("Target Velocity: 1500");
        telemetry.addData( "curretn vel", curretVel);
        telemetry.addData(" error", "%.2f", error);
        telemetry.addData("Tuning P", "%.4f (dpad U/D", P);
        telemetry.addData("Tuning F", "%.4f (dpad L/R", F);
        telemetry.addData("step size", "%.4f (B Button", stepSize[stepIndex]);
        telemetry.update();

        hoodPos = MathFunctions.clamp(hoodPos, 0, 0.12);


//        shooterR.setVelocity(1500);
//        shooterL.setVelocity(1500);
        mRobot.mShooter.setHoodPosition(hoodPos);
        mRobot.mShooter.runT2Angle(turretPos);
//
        if (gamepad1.aWasPressed()) {
            press = true;

        } else if (gamepad1.bWasPressed())
        {
            press = false;
            mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
            mRobot.mIntake.setRequestedGateState(Intake.GateState.OPEN);
        } else if (gamepad1.yWasPressed()) {
            mRobot.mIntake.requestState(Intake.TopLevelState.OUTTAKE);
        }

        if (press) {
            if (error > 200) {
                mRobot.mIntake.setIntakePower(0);
            } else {
                mRobot.mIntake.setRequestedGateState(Intake.GateState.CLOSED);
                mRobot.mIntake.setIntakePower(intakeSpeed);
            }
        }


//
        mRobot.update();
    }
}
