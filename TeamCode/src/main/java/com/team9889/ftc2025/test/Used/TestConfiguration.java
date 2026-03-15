package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;

@TeleOp
@Config
@Disabled

public class TestConfiguration extends OpMode {

    public static double shooterl = 0;
    public static double shooterr = 0;
    public static double turretr = 0;
    public static double turretl = 0;
    public static double hoodl = 0;
    public static double hoodr = 0;
    public static double intakel = 0;
    public static double intaker = 0;

    Robot mRobot = new Robot();
    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mShooter.shooterR.setVelocity(shooterr);
        mRobot.mShooter.shooterL.setVelocity(shooterl);
        mRobot.mShooter.turretL.setPosition(turretl);
        mRobot.mShooter.turretR.setPosition(turretr);
        mRobot.mShooter.hoodL.setPosition(MathFunctions.clamp(hoodl, 0, 0.12));
        mRobot.mShooter.hoodR.setPosition(MathFunctions.clamp(hoodr, 0, 0.12));

        mRobot.mIntake.intakeL.setPower(intakel);
        mRobot.mIntake.intakeR.setPower(intaker);

        mRobot.update();
    }
}
