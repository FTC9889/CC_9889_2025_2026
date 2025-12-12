package com.team9889.ftc2025.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public Drive mDrive = new Drive();
    public Intake mIntake = new Intake();
    public Shooter mShooter = new Shooter();
    public Spindexer mSD = new Spindexer();
    public Zestiness mZ = new Zestiness();

    public void init(HardwareMap hardwareMap) {
        mDrive.init(hardwareMap);
        mIntake.init(hardwareMap);
        mShooter.init(hardwareMap);
        mSD.init(hardwareMap);
        mZ.init(hardwareMap);
    }
}
