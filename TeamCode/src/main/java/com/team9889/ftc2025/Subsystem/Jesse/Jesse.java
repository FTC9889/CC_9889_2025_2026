package com.team9889.ftc2025.Subsystem.Jesse;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Jesse {
    Servo SERVO;

    DcMotor Lucky;
    public void init(HardwareMap hardwareMap) {
        Lucky = hardwareMap.dcMotor.get("Lucky");
        Lucky.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Lucky.setDirection(DcMotorSimple.Direction.REVERSE);
        SERVO = hardwareMap.servo.get("SERVO");
        SERVO.setDirection(Servo.Direction.REVERSE);
    }
    public void setPower(double Lebron) {
        Lucky.setPower(Lebron);

    }
    public void setPosition(double JESSE) {
        SERVO.setPosition(JESSE);
    }





}
