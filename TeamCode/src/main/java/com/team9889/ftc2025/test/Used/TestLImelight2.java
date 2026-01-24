//package com.team9889.ftc2025.test.Used;
//
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.team9889.ftc2025.Subsystem.Robot;
//
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//@TeleOp
//public class TestLImelight2 extends LinearOpMode {
//    Robot mRobot = new Robot();
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        mRobot.init(hardwareMap);
//
//        telemetry.setMsTransmissionInterval(11);
//
//        mRobot.mShooter.limelight.pipelineSwitch(7);
//
//        mRobot.mShooter.limelight.start();
//
//        while (opModeIsActive()) {
//            LLResult result = mRobot.mShooter.limelight.getLatestResult();
//            if (result != null) {
//                if (result.isValid()) {
//                    Pose3D botpose = result.getBotpose();
//                    telemetry.addData("tx", result.getTx());
//                    telemetry.addData("ty", result.getTy());
//                    telemetry.addData("Botpose", botpose.toString());
//                }
//            }
//        }
//    }
//}
