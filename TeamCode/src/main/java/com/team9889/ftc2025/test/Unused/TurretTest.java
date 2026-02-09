package com.team9889.ftc2025.test.Unused;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
public class TurretTest extends OpMode {
    //Open FTC Dashboard/Input this link in brave
    // 192.168.43.1:8080/dash
    // also make sure that the servos are plugged and configured correctly
    // jason and josh know how to do this
    //// if the code dosn't work ask josh
    //make sure in the configuration that the servo corespondent names are: turretR & turretL
    //// when you run the opmode the servos will go to position 0
    /// then in the config section of ftc dashboard you will be able to change the position
    Robot mRobot = new Robot();

    public static double turretPosition = 0;

    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mShooter.turretL.setPosition(turretPosition);
        mRobot.mShooter.turretR.setPosition(turretPosition);
    }
}
