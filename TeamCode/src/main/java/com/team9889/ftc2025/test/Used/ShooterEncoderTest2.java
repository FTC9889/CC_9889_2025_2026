package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
public class ShooterEncoderTest2 extends OpMode {
    Robot mRobot = new Robot();
    public static double shooterr = 0;
    public static double shooterl = 0;
    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mShooter.shooterR.setPower(shooterr);
        mRobot.mShooter.shooterL.setPower(shooterl);


        mRobot.update();


    }
}
