package com.team9889.ftc2025.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.net.PortUnreachableException;
import java.security.PublicKey;

public class Robot {

    public Drive mDrive = new Drive();
    public Intake mIntake = new Intake();
    public Serializer mSeializer = new Serializer();
    public Turrent mTurrent = new Turrent();

    public void init(HardwareMap hardwareMap) {
        mDrive.init(hardwareMap);
        mIntake.init(hardwareMap);
        mSeializer.init(hardwareMap);
        mTurrent.init(hardwareMap);

    }
}
