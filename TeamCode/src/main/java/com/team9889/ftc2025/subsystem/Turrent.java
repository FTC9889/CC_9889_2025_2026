package com.team9889.ftc2025.subsystem;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Turrent {
    DcMotor Rotation, ShooterR, ShooterL;
    CRServo ArcR, ArcL;

    public void init(HardwareMap hardwareMap) {
        Rotation = hardwareMap.dcMotor.get("Rotation");
        ShooterR = hardwareMap.dcMotor.get("ShooterR");
        ShooterL = hardwareMap.dcMotor.get("ShooterL");

        ArcL = hardwareMap.crservo.get("ArcL");
        ArcR = hardwareMap.crservo.get("ArcR");
    }

    public void setRotationPower(double power) {
        setRotationPower(power);
    }

    public void setArcPower(double power) {
        ArcL.setPower(power);
        ArcR.setPower(-power);
    }

    public void setShooterPower(double power) {
        ShooterL.setPower(power);
        ShooterR.setPower(-power);
    }

}
