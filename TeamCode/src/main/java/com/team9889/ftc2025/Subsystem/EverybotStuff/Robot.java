package com.team9889.ftc2025.Subsystem.EverybotStuff;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public Drive mDrive = new Drive();
    public Intake mIntake = new Intake();
    public Catapult mCatapult = new Catapult();
    public Foot mFoot = new Foot();

    public void init(HardwareMap hardwareMap) {
        mDrive.init(hardwareMap);
        mIntake.init(hardwareMap);
        mCatapult.init(hardwareMap);
        mFoot.init(hardwareMap);
    }
}
