package com.team9889.ftc2025.Subsystem.EverybotStuff;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.logging.Handler;

public class Intake {
    DcMotor intake;
    public void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");

        intake.setDirection(DcMotor.Direction.FORWARD); // Forward should INTAKE.
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
