package com.team9889.ftc2025.Subsystem;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {

    public DcMotor intake;
    public CRServo TR, TL;
    public Servo gate;

    public IntakeState CurrentIntakeState = IntakeState.NULL;
    public IntakeState RequestedIntakeState = IntakeState.NULL;

    public TransferState CurrentTransferState = TransferState.NULL;
    public TransferState RequestedTransferState = TransferState.NULL;

    public GateState CurrentGateState = GateState.NULL;
    public GateState RequestedGateState = GateState.NULL;

    public IntakeState getCurrentIntakeState() {
        return CurrentIntakeState;
    }

    public TransferState getCurrentTransferState() {
        return CurrentTransferState;
    }

    public GateState getCurrentGateState() {
        return CurrentGateState;
    }

    public void setRequestedIntakeState(IntakeState requestedIntakeState) {
        RequestedIntakeState = requestedIntakeState;
    }

    public void setRequestedTransferState(TransferState requestedTransferState) {
        RequestedTransferState = requestedTransferState;
    }

    public void setRequestedGateState(GateState requestedGateState) {
        RequestedGateState = requestedGateState;
    }

    public enum TopLevelState {
        INTAKE(IntakeState.ON, TransferState.ON, GateState.CLOSED),
        KEEP(IntakeState.SLOW, TransferState.OFF, GateState.CLOSED),
        SHOOT(IntakeState.ON, TransferState.ON, GateState.OPEN),
        FULL_OUTTAKE(IntakeState.OUTTAKE, TransferState.OUTTAKE, GateState.CLOSED),
        OUTTAKE(IntakeState.OUTTAKE, TransferState.ON, GateState.CLOSED)
        ;

        final IntakeState intakeState;
        final TransferState transferState;
        final GateState gateState;

        TopLevelState(IntakeState intakeState, TransferState transferState, GateState gateState) {
            this.intakeState = intakeState;
            this.transferState = transferState;
            this.gateState = gateState;
        }
    }

    public enum IntakeState{
        ON(-1),
        OFF(0),
        SLOW(-0.15),
        OUTTAKE(1),
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
        OPEN(1),
        CLOSED(0),
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
                    return "ON";
                case CLOSED:
                    return "OFF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum TransferState{
        ON(-1),
        OFF(0),
        SLOW(-0.15),
        OUTTAKE(1),
        NULL(0);

        private final double value;
        TransferState(double value) {
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




    public void init(HardwareMap hardwareMap) {
        gate = hardwareMap.servo.get("gate");

        TL = hardwareMap.crservo.get("TL");
        TR = hardwareMap.crservo.get("TR");

        TR.setDirection(CRServo.Direction.REVERSE);

        intake = hardwareMap.dcMotor.get("intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    double intakePower = 2;
    public void setIntakePower(double power){
        if (intakePower != power) {
            intake.setPower(power);
        }
        intakePower = power;
        if(power == IntakeState.OFF.value) {
            CurrentIntakeState = IntakeState.OFF;
        }
    }

    double transferPower = 2;
    public void setTransferPower(double power){
        if (transferPower != power) {
            TR.setPower(power);
            TL.setPower(power);
        }
        transferPower = power;
        if(power == TransferState.OFF.value) {
            CurrentTransferState = TransferState.OFF;
        }
    }

    public void setGatePosition(double position) {
        gate.setPosition(position);
    }

    ElapsedTime outtakeTimer = new ElapsedTime();
    public void update() {

        //GatePosition
        setGatePosition(RequestedGateState.value);
        CurrentGateState = RequestedGateState;

        //TransferPower
        setTransferPower(RequestedTransferState.setTargetPower());
        CurrentTransferState = RequestedTransferState;

        //Intake Power
        if (RequestedIntakeState != IntakeState.OUTTAKE && RequestedTransferState != TransferState.ON) {
            outtakeTimer.reset();
            if (outtakeTimer.milliseconds() != 300) {
                setIntakePower(RequestedIntakeState.setTargetPower());
                CurrentIntakeState = RequestedIntakeState;
            } else {
                setRequestedIntakeState(IntakeState.SLOW);
                setRequestedTransferState(TransferState.OFF);
            }
        } else {
            setIntakePower(RequestedIntakeState.setTargetPower());
            CurrentIntakeState = RequestedIntakeState;
        }
    }

    public boolean isComplete() {
        return RequestedIntakeState == CurrentIntakeState && RequestedTransferState == CurrentTransferState;
    }

    public void requestState(TopLevelState state) {
        RequestedIntakeState = state.intakeState;
        RequestedTransferState = state.transferState;
        RequestedGateState = state.gateState;
    }
}

