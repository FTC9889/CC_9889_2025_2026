package com.team9889.ftc2025.test.Used;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Robot;

@TeleOp
public class TestIntakeMotor extends OpMode {
    Robot mRobot = new Robot();
    @Override
    public void init() {
        mRobot.init(hardwareMap);

    }

    @Override
    public void loop() {

    }
}
