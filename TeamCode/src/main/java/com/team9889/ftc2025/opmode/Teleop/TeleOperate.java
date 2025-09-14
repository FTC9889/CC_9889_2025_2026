package com.team9889.ftc2025.opmode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.subsystem.Robot;

@TeleOp
public class TeleOperate extends LinearOpMode {
    Robot mRobot = new Robot();

    @Override
    public void runOpMode() throws InterruptedException {
        mRobot.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            mRobot.mDrive.setPower(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x/2);
        }

        if (gamepad1.right_trigger > 0.01) {
            mRobot.mTurrent.setRotationPower(gamepad1.right_trigger);
        } else if (gamepad1.left_trigger > 0.01) {
            mRobot.mTurrent.setRotationPower(-gamepad1.left_trigger);
        } else {
            mRobot.mTurrent.setRotationPower(0);
        }
    }
}
