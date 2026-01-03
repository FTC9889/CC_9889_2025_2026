//package com.team9889.ftc2025.test.Used;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.PIDFCoefficients;
//import com.team9889.ftc2025.Subsystem.Robot;
//@TeleOp
////https://www.youtube.com/watch?v=aPNCpZzCTKg    7:00
//public class ShooterPIDF extends OpMode {
//    Robot mRobot = new Robot();
//
//    DcMotor Shooter;
//
//    public double highVelocity = 1500;
//    public double lowVelocity = 900;
//
//    double curTargetVel = highVelocity;
//
//    double F = 0;
//    double P = 0;
//
//    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
//
//    int stepIndex = 1;
//
//    @Override
//    public void init() {
//        mRobot.init(hardwareMap);
//        mRobot.mShooter.shooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        mRobot.mShooter.shooterR.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//
//        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
//        mRobot.mShooter.shooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//        mRobot.mShooter.shooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//        telemetry.addLine("init complete");
//
//    }
//
//    public void loop() {
//        if (gamepad1.yWasPressed()) {
//            if (curTargetVel == highVelocity) {
//                curTargetVel = lowVelocity;
//            } else {
//                curTargetVel = highVelocity;
//            }
//        }
//        if (gamepad1.bWasPressed()) {
//            stepIndex = (stepIndex + 1) % stepSizes.length;
//        }
//
//        if (gamepad1.dpadLeftWasPressed()) {
//            F += stepSizes[stepIndex];
//        }
//        if (gamepad1.dpadRightWasPressed()) {
//            F -= stepSizes[stepIndex];
//        }
//
//        if (gamepad1.dpadDownWasPressed()) {
//            P -= stepSizes[stepIndex];
//        }
//
//        if (gamepad1.dpadUpWasPressed()) {
//            P += stepSizes[stepIndex];
//        }
//
//        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
//
//        mRobot.mShooter.shooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//        mRobot.mShooter.shooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//
//
//        mRobot.mShooter.shooterR.setVelocity(curTargetVel);
//        mRobot.mShooter.shooterL.setVelocity(curTargetVel);
//
//        double curVel = (mRobot.mShooter.shooterR.getVelocity() + mRobot.mShooter.shooterL.getVelocity())/2;
//        double error = curTargetVel - curVel;
//
//        telemetry.addData("Target Vel", curTargetVel);
//        telemetry.addData("Current Vel", curVel);
//        telemetry.addData("Error", "%.2f", error);
//        telemetry.addLine("-----------------------------------");
//        telemetry.addData("P","%.4f (0-Pad U/D", P);
//        telemetry.addData("F", "%.4f (0-Pad U/D", F);
//        telemetry.addData("Step Size", "%.4f (B Button", stepSizes[stepIndex]);
//    }
//}
