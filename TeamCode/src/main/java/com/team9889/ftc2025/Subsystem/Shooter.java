package com.team9889.ftc2025.Subsystem;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Shooter {

    private Limelight3A limelight;

    Servo hoodR, hoodL, turretR, turretL;

    DcMotor shooterR,shooterL;

    AnalogInput sensor;

    double currentVoltage;

    public void init(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        shooterR = hardwareMap.dcMotor.get("shooterR");
        shooterR = hardwareMap.dcMotor.get("shooterL");

        hoodL = hardwareMap.servo.get("hoodL");
        hoodR = hardwareMap.servo.get("hoodR");

        turretR = hardwareMap.servo.get("turretR");
        turretL = hardwareMap.servo.get("turretL");

        hoodR.setDirection(Servo.Direction.REVERSE);

        sensor = hardwareMap.get(AnalogInput.class, "potentiometer");
    }



    public void setShooterPower(double power) {
        shooterR.setPower(power);
        shooterL.setPower(power);
    }

    public void setHoodPosition(double position) {
        hoodR.setPosition(position);
        hoodL.setPosition(position);
    }

    public double getTurrentPos() {
        return turretL.getPosition() / 360;
    }




}
