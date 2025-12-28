package com.team9889.ftc2025.Subsystem;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.R;

public class Shooter {

    public Limelight3A limelight;

    public Servo hoodR, hoodL, turretR, turretL;

    public DcMotorEx shooterR, shooterL;

    public AnalogInput sensor;

    double currentVoltage;

    public ShooterState CurrentShooterState = ShooterState.NULL;
    public ShooterState RequestedShooterState = ShooterState.NULL;

    public TurretState CurrentTurretState = TurretState.NULL;
    public TurretState RequestedTurretState = TurretState.NULL;

    public HoodState CurrentHoodState = HoodState.NULL;
    public HoodState RequestedHoodState = HoodState.NULL;

    public ShooterState getCurrentShooterState() {
        return CurrentShooterState;
    }

    public ShooterState getRequestedShooterState() {
        return RequestedShooterState;
    }

    public TurretState getCurrentTurretState() {
        return CurrentTurretState;
    }

    public TurretState getRequestedTurretState() {
        return RequestedTurretState;
    }

    public HoodState getCurrentHoodState() {
        return CurrentHoodState;
    }

    public HoodState getRequestedHoodState() {
        return RequestedHoodState;
    }

    public enum TopLevelState {
        READY(ShooterState.ON, TurretState.TRAKING, HoodState.READY),
        DEFAULT(ShooterState.OFF, TurretState.OFF, HoodState.NULL);

        final ShooterState shooterState;
        final TurretState turretState;
        final HoodState hoodState;

        TopLevelState(ShooterState shooterState, TurretState turretState, HoodState hoodState) {
            this.shooterState = shooterState;
            this.turretState = turretState;
            this.hoodState = hoodState;
        }
    }

    public enum ShooterState{
        ON(-1),
        OFF(0),
        SLOW(-0.15),
        OUTTAKE(1),
        NULL(0);

        private final double value;
        ShooterState(double value) {
            this.value = value;
        }

        public double setTargetPower() {
            return value;
        }

        public String toString() {
            switch (this) {
                case ON:
                    return "ON";
                case OFF:
                    return "OFF";
                case SLOW:
                    return "SLOW";
                case OUTTAKE:
                    return "OUTTAKE";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum TurretState{
        TRAKING(-1),
        OFF(0),
        NULL(0);

        private final double value;
        TurretState(double value) {
            this.value = value;
        }

        public double setTargetPower() {
            return value;
        }

        public String toString() {
            switch (this) {
                case TRAKING:
                    return "ON";
                case OFF:
                    return "OFF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum HoodState{
        READY(-1),
        NULL(0);

        private final double value;
        HoodState(double value) {
            this.value = value;
        }

        public double setTargetPower() {
            return value;
        }

        public String toString() {
            switch (this) {
                case READY:
                    return "ON";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public void init(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        shooterR = hardwareMap.get(DcMotorEx.class, "shooterR");
        shooterL = hardwareMap.get(DcMotorEx.class, "shooterL");

        hoodL = hardwareMap.servo.get("hoodL");
        hoodR = hardwareMap.servo.get("hoodR");

        turretR = hardwareMap.servo.get("turretR");
        turretL = hardwareMap.servo.get("turretL");

        hoodR.setDirection(Servo.Direction.REVERSE);
        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);
        turretR.setDirection(Servo.Direction.REVERSE);

        sensor = hardwareMap.get(AnalogInput.class, "potentiometer");
    }

    double shooterPower = 2;
    public void setShooterPower(double power){
        if (shooterPower != power) {
            shooterR.setPower(power);
            shooterL.setPower(power);
        }
        shooterPower = power;
        if(power == ShooterState.OFF.value) {
            CurrentShooterState = ShooterState.OFF;
        }
    }

    public void setHoodPosition(double position) {
        hoodR.setPosition(position);
        hoodL.setPosition(position);
    }

    public void setTurretPosition(double position) {
        turretR.setPosition(position);
        turretL.setPosition(position);
    }

    public double getTurrentAngle() {
        return turretL.getPosition() / 360;
    }



    //UPDATE
    public void update() {

    }

    public boolean isComplete() {
        return RequestedShooterState == CurrentShooterState && RequestedTurretState == CurrentTurretState && RequestedHoodState == CurrentHoodState;
    }

    public void requestState(TopLevelState state) {
        RequestedShooterState = state.shooterState;
        RequestedTurretState = state.turretState;
        RequestedHoodState = state.hoodState;
    }
}
