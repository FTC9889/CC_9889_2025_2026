//package com.team9889.ftc2025.Subsystem;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//public class Turrent {
//    DcMotor CatR, CatL;
//
//    public void init(HardwareMap hardwareMap) {
//        CatL = hardwareMap.dcMotor.get("ShooterR");
//        CatR = hardwareMap.dcMotor.get("ShooterL");
//
//        CatL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        CatR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//    }
//
//    public void setCatPower(double power) {
//        CatL.setPower(power);
//        CatR.setPower(power);
//    }
//
//}
