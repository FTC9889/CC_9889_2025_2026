package com.team9889.ftc2025.test.Used;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Robot;
@Disabled

public class ShooterEncoderTest extends OpMode {
    Robot mRobot = new Robot();
    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("shooterL", mRobot.mShooter.shooterL.getCurrentPosition());
        telemetry.addData("shooterR", mRobot.mShooter.shooterR.getCurrentPosition());
        telemetry.update();
    }
}
