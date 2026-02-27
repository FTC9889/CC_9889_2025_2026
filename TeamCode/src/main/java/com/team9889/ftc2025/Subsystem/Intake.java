package com.team9889.ftc2025.Subsystem;

import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

public class Intake {

    public DcMotorEx intakeR, intakeL;
    public Servo gate;

    public IntakeState CurrentIntakeState = IntakeState.NULL;
    public IntakeState RequestedIntakeState = IntakeState.NULL;
    public IntakeState getCurrentIntakeState() {
        return CurrentIntakeState;
    }
    public IntakeState getRequestedIntakeState() {return RequestedIntakeState;}
    public void setRequestedIntakeState(IntakeState requestedIntakeState) {
        RequestedIntakeState = requestedIntakeState;
    }

    public GateState CurrentGateState = GateState.NULL;
    public GateState RequestedGateState = GateState.NULL;
    public GateState getCurrentGateState() {
        return CurrentGateState;
    }
    public GateState getRequestedGateState() {return RequestedGateState;}
    public void setRequestedGateState(GateState requestedGateState) {
        RequestedGateState = requestedGateState;
    }

    public enum TopLevelState {
        INTAKE(IntakeState.ON, GateState.CLOSED),
        KEEP(IntakeState.SLOW, GateState.CLOSED),
        SHOOT(IntakeState.ON, GateState.OPEN),
        OUTTAKE(IntakeState.OUTTAKE, GateState.CLOSED),
        OFF(IntakeState.OFF, GateState.CLOSED),
        ;

        final IntakeState intakeState;
        final GateState gateState;

        TopLevelState(IntakeState intakeState, GateState gateState) {
            this.intakeState = intakeState;
            this.gateState = gateState;
        }
    }

    public enum IntakeState{
        ON(1),
        OFF(0),
        SLOW(0.75),
        OUTTAKE(-1),
        NULL(0);

        private final double value;
        IntakeState(double value) {
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
    public enum GateState{
        OPEN(0.7),
        CLOSED(0.55),
        NULL(0);

        private final double value;
        GateState(double value) {
            this.value = value;
        }

        public double setTargetPower() {
            return value;
        }

        public String toString() {
            switch (this) {
                case OPEN:
                    return "OPEN";
                case CLOSED:
                    return "CLOSED";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public void init(HardwareMap hardwareMap) {
        gate = hardwareMap.servo.get("gate");

        intakeR = hardwareMap.get(DcMotorEx.class, "intakeR");
        intakeL = hardwareMap.get(DcMotorEx.class, "intakeL");

        intakeR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intakeL.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeR.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    double intakePower = 2;
    public void setIntakePower(double power) {
        if (intakePower != power) {
            intakeR.setPower(power);
            intakeL.setPower(power);
            intakePower = power;
        }
    }

    public void setFrontIntakePower(double power) {
        intakeR.setPower(power);
    }

    double transferPower = 2;
    public void setGatePosition(double position) {
        gate.setPosition(position);
    }



    public static double intakePower(double goalDistance) {
        double intakePow = 0;
        if (goalDistance > 119) {
            intakePow = 0.9;
        } else {
            intakePow = 1;
        }
        return MathFunctions.clamp(intakePow, 0, 1
        );
    }

    ElapsedTime outtakeTimer = new ElapsedTime();
    public void update(double goalDistance) {

        // Gate
        if (RequestedGateState != GateState.NULL &&
                RequestedGateState != CurrentGateState) {
            setGatePosition(RequestedGateState.value);
            CurrentGateState = RequestedGateState;
        }

        // Intake
        if (RequestedIntakeState != IntakeState.NULL &&
                RequestedIntakeState != CurrentIntakeState) {
            if (RequestedGateState != GateState.OPEN) {
                setIntakePower(RequestedIntakeState.value);
                CurrentIntakeState = RequestedIntakeState;
            } else if (RequestedIntakeState == IntakeState.ON && RequestedGateState == GateState.OPEN) {
                setIntakePower(intakePower(goalDistance));
                setGatePosition(GateState.OPEN.value);
                RobotLog.a("DFT", goalDistance);
            }
        }

        if (RequestedIntakeState == IntakeState.ON && RequestedGateState == GateState.OPEN) {
            setIntakePower(intakePower(goalDistance));
            setGatePosition(GateState.OPEN.value);
        }
    }

    public boolean isComplete() {
        return RequestedIntakeState == CurrentIntakeState && RequestedGateState == CurrentGateState;
    }
    public void requestState(TopLevelState state) {
        RequestedIntakeState = state.intakeState;
        RequestedGateState = state.gateState;
    }
}

