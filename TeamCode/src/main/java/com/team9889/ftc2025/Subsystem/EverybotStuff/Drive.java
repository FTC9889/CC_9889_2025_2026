package com.team9889.ftc2025.Subsystem.EverybotStuff;

import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Drive {
    DcMotor leftfront, leftback, rightfront, rightback;
    IMU imu;

    public void init(HardwareMap hardwareMap) {
        leftfront = hardwareMap.get(DcMotor.class, "leftfront");
        leftback = hardwareMap.get(DcMotor.class, "leftback");
        rightfront = hardwareMap.get(DcMotor.class, "rightfront");
        rightback = hardwareMap.get(DcMotor.class, "rightback");

        imu = new Rev9AxisImu((I2cDeviceSynchSimple) hardwareMap, true);

        leftfront.setDirection(DcMotor.Direction.FORWARD);
        leftback.setDirection(DcMotor.Direction.FORWARD);
        rightfront.setDirection(DcMotor.Direction.REVERSE);
        rightback.setDirection(DcMotor.Direction.REVERSE);
    }

    public void setPower(double LFPower,double RFPower,double LBPower, double RBPower){
        rightfront.setPower(RFPower);
        leftfront.setPower(LFPower);
        leftback.setPower(LBPower);
        rightback.setPower(RBPower);
    }

    public void setPower(double leftStickX, double leftStickY, double rightStickX){
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        double rightX = rightStickX;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;
        setPower(v1,v2,v3,v4);

    }

    public void brakeAndReset() {
        rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );
        leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );
        leftback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );
        rightback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );

        setPower(0, 0, 0);

        rightfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftback.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightback.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void brake() {
        rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );
        leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );
        leftback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );
        rightback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE );

        setPower(0, 0, 0);
    }
    public void reset() {
        rightfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftfront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftback.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightback.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftfront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightback.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void floatTheMotors(){
        rightfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftfront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightback.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public double getAngle() {
        Orientation orientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.XYZ,
                AngleUnit.DEGREES
        );

        return (double) orientation.firstAngle; // cast float → double
    }



    public int front_encoder(){
        return -rightfront.getCurrentPosition();

    }
}
