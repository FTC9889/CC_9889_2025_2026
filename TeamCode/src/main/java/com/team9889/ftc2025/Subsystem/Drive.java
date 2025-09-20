//package com.team9889.ftc2025.Subsystem;
//
//import com.qualcomm.hardware.rev.Rev9AxisImu;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//public class Drive {
//    DcMotor leftFront,leftBack, rightFront, rightBack;
//
//    public Rev9AxisImu imu;
//
//    public void init(HardwareMap hardwareMap) {
//        leftFront = hardwareMap.dcMotor.get("leftfront");
//        leftBack = hardwareMap.dcMotor.get("leftback");
//        rightFront = hardwareMap.dcMotor.get("rightfront");
//        rightBack = hardwareMap.dcMotor.get("rightback");
//
//        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
//        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        imu = new Rev9AxisImu();
//
//        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    }
//
//    public void setPower(double LFPower,double RFPower,double LBPower, double RBPower){
//        rightFront.setPower(RFPower);
//        leftFront.setPower(LFPower);
//        leftBack.setPower(LBPower);
//        rightBack.setPower(RBPower);
//    }
//
//    public void setPower(double leftStickX, double leftStickY, double rightStickX){
//        double r = Math.hypot(leftStickX, leftStickY);
//        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
//        double rightX = rightStickX;
//        final double v1 = r * Math.cos(robotAngle) + rightX;
//        final double v2 = r * Math.sin(robotAngle) - rightX;
//        final double v3 = r * Math.sin(robotAngle) + rightX;
//        final double v4 = r * Math.cos(robotAngle) - rightX;
//        setPower(v1,v2,v3,v4);
//
//    }
//
//
//}
