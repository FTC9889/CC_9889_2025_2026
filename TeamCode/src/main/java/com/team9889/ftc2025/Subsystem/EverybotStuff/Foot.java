package com.team9889.ftc2025.Subsystem.EverybotStuff;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.logging.Handler;

public class Foot {
    DcMotor foot;

    public void init(HardwareMap hardwareMap) {
        foot = hardwareMap.get(DcMotor.class, "foot");

        foot.setDirection(DcMotor.Direction.REVERSE); // Backwards should should stay UP, or in the stowed position
        foot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
