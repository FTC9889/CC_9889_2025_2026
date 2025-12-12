package com.team9889.ftc2025.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Zestiness {
    double as = 0.6;
    float asd = 1;
    boolean asdfa = false;
   DcMotor Zesty;
   Servo zesty;
   public  void init(HardwareMap hardwareMap){
       Zesty = hardwareMap.dcMotor.get("qwerty");
       zesty = hardwareMap.servo.get("Qwerty");
   }

   public void power(double jacob) {
       Zesty.setPower(jacob);
   }
   public void position(double jakob) {
       zesty.setPosition(jakob);
   }
}
