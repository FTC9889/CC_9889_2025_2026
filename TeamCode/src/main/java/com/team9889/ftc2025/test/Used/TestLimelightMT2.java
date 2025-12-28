package com.team9889.ftc2025.test.Used;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.team9889.ftc2025.Subsystem.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
@TeleOp
public class TestLimelightMT2 extends OpMode {

    Robot mRobot = new Robot();


    @Override
    public void init() {
        mRobot.init(hardwareMap);
        mRobot.mShooter.limelight.pipelineSwitch(7);
    }

    @Override
    public void start() {
        mRobot.mShooter.limelight.start();
        //if delayed move to init

    }

    @Override
    public void loop() {
        YawPitchRollAngles orientation = mRobot.mDrive.imu.getRobotYawPitchRollAngles();
        mRobot.mShooter.limelight.updateRobotOrientation(orientation.getYaw());
        LLResult llResult = mRobot.mShooter.limelight.getLatestResult();
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose_MT2();
            telemetry.addData("Ty", llResult.getTy());
            telemetry.addData("Tx", llResult.getTx());
            telemetry.addData("Ta", llResult.getTa());
            telemetry.addData("yaw", llResult.getBotpose().getOrientation().getYaw());
            telemetry.addData("pose", llResult.getBotpose_MT2());
        }
    }
}
