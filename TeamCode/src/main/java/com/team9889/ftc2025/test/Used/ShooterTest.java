package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
//@TeleOp
public class ShooterTest extends OpMode {
    Robot mRobot = new Robot();
    public static double shooterVelocity = 0;
    public static double turrentPosition = 0.1;
    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mShooter.setShooterVelocity(shooterVelocity);
        mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
        mRobot.mShooter.setHoodPosition(turrentPosition);

        mRobot.update();

    }
}
