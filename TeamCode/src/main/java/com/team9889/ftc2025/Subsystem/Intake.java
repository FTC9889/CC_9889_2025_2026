package com.team9889.ftc2025.Subsystem;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    Pose redTarget = new Pose(140, 140);
    Pose blueTarget = new Pose(4, 140);

    boolean ballEvent = false;
    boolean lastBallState = false;

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
        AUTO_SHOOT(IntakeState.ON, GateState.OPEN),
        OUTTAKE(IntakeState.OUTTAKE, GateState.OPEN),
        OFF(IntakeState.OFF, GateState.CLOSED),
        SLOW(IntakeState.SLOW, GateState.CLOSED)
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
        AUTO_SHOOT(1),
        OFF(0),
        SLOW(0.2),
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
                case AUTO_SHOOT:
                    return "AUTO_SHOOT";
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
        CLOSED(0.48),
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
            if (rumble) {
                power = 0.2;
            }
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
        position = MathFunctions.clamp(position, 0.48, 0.7);
        gate.setPosition(position);
    }



    public static double intakePower(double goalDistance) {
        return MathFunctions.clamp(
                1.4991e-8 * Math.pow(goalDistance, 4)
                        - 4.35809e-6 * Math.pow(goalDistance, 3)
                        + 0.000375634 * Math.pow(goalDistance, 2)
                        - 0.011593 * goalDistance
                        + 1.1087,
                0,
                1
        );
    }


    public boolean rumble = false;


    ElapsedTime outtakeTimer = new ElapsedTime();
    ElapsedTime intakeBallCounter = new ElapsedTime();
    public void update(Pose robotpose) {
        double goalDistance = robotpose.distanceFrom(Drive.red ? redTarget : blueTarget);

        boolean ballDetected = ballInIntake() == 3;

        if (ballDetected && !lastBallState) {
            ballEvent = true;
        } else {
            ballEvent = false;
        }

        lastBallState = ballDetected;

        // Gate
        setGatePosition(RequestedGateState.value);
        CurrentGateState = RequestedGateState;


        // Intake
        if (RequestedIntakeState != IntakeState.NULL &&
                RequestedIntakeState != CurrentIntakeState) {
            if (RequestedIntakeState == IntakeState.AUTO_SHOOT) {
                setIntakePower(1);
                setGatePosition(GateState.OPEN.value);
                CurrentIntakeState = RequestedIntakeState;
            } else if (RequestedIntakeState == IntakeState.ON && RequestedGateState == GateState.CLOSED && ballInIntake() == 3) {
                intakeL.setPower(0);
                intakeR.setPower(1);
            } else if (RequestedGateState != GateState.OPEN) {
                setIntakePower(RequestedIntakeState.value);
                CurrentIntakeState = RequestedIntakeState;
                CurrentGateState = RequestedGateState;
            } else if (RequestedIntakeState == IntakeState.ON && RequestedGateState == GateState.OPEN) {
                setIntakePower(intakePower(goalDistance));
                setGatePosition(GateState.OPEN.value);
                CurrentIntakeState = RequestedIntakeState;
                CurrentGateState = RequestedGateState;
            } else if (RequestedIntakeState == IntakeState.SLOW) {
                intakeL.setPower(0);
                intakeR.setPower(1);
            } else {
                setIntakePower(RequestedIntakeState.value);
            }
        }

        rumble = rumble(ballInIntake());
    }

    public boolean rumble(double ball) {
        return ball == 3;
    }

    public boolean isComplete() {
        return RequestedIntakeState == CurrentIntakeState && RequestedGateState == CurrentGateState;
    }

    //0 < 1300
    //1 =    >1300   < 2000
    //2 > 2000   < 2500

    double current1 = 1500;
    double current3 = 3000;

    public double ball;
    public boolean start = false;

    public double ballInIntake() {
        double currentMilliampB = intakeL.getCurrent(CurrentUnit.MILLIAMPS);
        double currentMilliampF = intakeR.getCurrent(CurrentUnit.MILLIAMPS);

        if (CurrentIntakeState == IntakeState.ON && CurrentGateState == GateState.CLOSED) {
            boolean spike = currentMilliampB > current1 && currentMilliampF > current3;
            if (spike) {
                if (!start) {
                    start = true;
                    intakeBallCounter.reset();
                }
                if (intakeBallCounter.milliseconds() > 250) {
                    ball = 3;
                } else {
                    ball = 2;
                }
            } else {
                start = false;
                ball = 0;
            }
        } else {
            start = false;
            ball = 0;
        }
        return ball;
    }

    public void requestState(TopLevelState state) {
        RequestedIntakeState = state.intakeState;
        RequestedGateState = state.gateState;
    }

    public boolean getBallEvent() {
        return intakeL.getCurrent(CurrentUnit.MILLIAMPS) > 5500;
    }
}

