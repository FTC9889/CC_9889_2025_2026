package com.team9889.ftc2025.Opmode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

@TeleOp
public class Test extends OpMode {
    Robot mRobot = new Robot();

    @Override
    public void init() {
        mRobot.init(hardwareMap);
    }

    @Override
    public void loop() {
        mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
        mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY_CLOSE);
        mRobot.update();
    }
}
