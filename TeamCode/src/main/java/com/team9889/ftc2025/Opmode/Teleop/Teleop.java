package com.team9889.ftc2025.Opmode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Robot;

@TeleOp(name = "TeleOp Control", group = "Teleop")
public class Teleop extends LinearOpMode {
    Robot mRobot = new Robot();

    @Override
    public void runOpMode() throws InterruptedException {
        mRobot.init(hardwareMap);

        mRobot.mDrive.setPower(-gamepad1.left_stick_y, gamepad1.left_stick_x, -gamepad1.right_stick_x / 2);
    }
}