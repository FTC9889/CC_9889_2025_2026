package com.team9889.ftc2025.Subsystem.Jesse;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class Teleop extends LinearOpMode {
    Jesse JESSE = new Jesse();
    @Override
    public void runOpMode() throws InterruptedException {
        JESSE.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            JESSE.setPower(0.4);
            JESSE.setPosition(0.5);
            

        }
    }
}

