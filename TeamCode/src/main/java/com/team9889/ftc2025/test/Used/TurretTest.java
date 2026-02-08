package com.team9889.ftc2025.test.Used;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Robot;
@Config
@TeleOp
public class TurretTest extends OpMode {
    Robot mRobot = new Robot();
    public static double turretPosition = 0;
    // open FTC Dashboard by using this link: 192.168.43.1:8080/dash
    // let josh or jason plug and wire the servo
    //// please make sure you wire correctly cause the turret is gonna turn
    /// also make sure the corresponding servo names are: turretR & turretL
    /// if this doesn't work cause you didn't plug in the shooter motors ask josh to fix it
    /*
    Step1: run the opmode
    Step2: us the configuration bar and change the values
    Step3: the servo runs from 0 to 1
    Step4: Hope the turret turns fast and works at a decent speed or else were cooked FRITAS
     */

    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mShooter.turretR.setPosition(turretPosition);
        mRobot.mShooter.turretL.setPosition(turretPosition);
    }
}
