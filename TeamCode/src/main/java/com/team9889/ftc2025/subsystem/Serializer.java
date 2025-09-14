package com.team9889.ftc2025.subsystem;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Serializer {

    DcMotor Serializer;

    ColorSensor colorS1, colorS2, colorS3;


    public void init(HardwareMap hardwareMap) {
        Serializer = hardwareMap.dcMotor.get("serializer");
    }
}
