package com.team9889.ftc2025.Subsystem;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {

    private Limelight3A limelight;

    Servo hoodR, hoodL;

    DcMotor shooter, turret;

    public void init(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        shooter = hardwareMap.dcMotor.get("shooter");

        hoodL = hardwareMap.servo.get("hoodL");
        hoodR = hardwareMap.servo.get("hoodR");

        turret = hardwareMap.dcMotor.get("turret");
        turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hoodR.setDirection(Servo.Direction.REVERSE);
    }

    public void setShooterPower(double power) {
        shooter.setPower(power);
    }

    public void setHoodPosition(double position) {
        hoodR.setPosition(position);
        hoodL.setPosition(position);
    }

    public void setTurretPower(double power) {
        turret.setPower(power);
    }




}
