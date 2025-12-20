//package com.team9889.ftc2025.Subsystem;
//
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//
//public class Spindexer {
//
//    Servo spin1, spin2, gate;
//    CRServo pusher;
//
//    DcMotor elevator;
//
//    ColorSensor colorS1, colorS2, colorS3;
//
//
//    public void init(HardwareMap hardwareMap) {
//        spin1 = hardwareMap.servo.get("spin1");
//        spin2 = hardwareMap.servo.get("spin2");
//
//        gate = hardwareMap.servo.get("gate");
//
//        pusher = hardwareMap.crservo.get("pusher");
//
//        elevator = hardwareMap.dcMotor.get("elevator");
//    }
//
//    public void setSpinPosition(double position) {
//        spin1.setPosition(position);
//        spin2.setPosition(position);
//    }
//
//    public void setElevatorPower(double power) {
//        elevator.setPower(power);
//    }
//
//    public void setPusherPower(double power) {
//        pusher.setPower(power);
//    }
//
//    public void setGatePosition(double position) {
//        gate.setPosition(position);
//    }
//}
