package com.team9889.ftc2025.Subsystem.Shooter;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2025.Subsystem.Drive;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class ShooterStateMachine {
//    public Limelight3A limelight;
    public Servo hoodR, hoodL,
            turretR, turretL;
    public DcMotorEx shooterR, shooterL;
//        public AnalogInput sensor;
//    double currentVoltage;

    public ShooterState CurrentShooterState = ShooterState.NULL;
    public ShooterState RequestedShooterState = ShooterState.NULL;

    public ShooterState getCurrentShooterState() {
        return CurrentShooterState;
    }

    public ShooterState getRequestedShooterState() {
        return RequestedShooterState;
    }

    public void setRequestedShooterState(ShooterState shooterState) {
        RequestedShooterState = shooterState;
    }

    public TurretState CurrentTurretState = TurretState.NULL;
    public TurretState RequestedTurretState = TurretState.NULL;

    public TurretState getCurrentTurretState() {
        return CurrentTurretState;
    }

    public TurretState getRequestedTurretState() {
        return RequestedTurretState;
    }

    public void setRequestedTurretState(TurretState turretState) {
        RequestedTurretState = turretState;
    }

    public HoodState CurrentHoodState = HoodState.NULL;
    public HoodState RequestedHoodState = HoodState.NULL;

    public HoodState getCurrentHoodState() {
        return CurrentHoodState;
    }

    public HoodState getRequestedHoodState() {
        return RequestedHoodState;
    }

    public void setRequestedHoodState(HoodState hoodState) {
        RequestedHoodState = hoodState;
    }

    public enum TopLevelState {
        READY(ShooterState.READY,
                TurretState.TRACKING,
                HoodState.READY),
        DEFAULT(ShooterState.OFF,
                TurretState.TRACKING,
                HoodState.DEFAULT);

        final ShooterState shooterState;
        final TurretState turretState;
        final HoodState hoodState;

        TopLevelState(ShooterState shooterState,
                      TurretState turretState,
                      HoodState hoodState) {
            this.shooterState = shooterState;
            this.turretState = turretState;
            this.hoodState = hoodState;
        }
    }

    public enum ShooterState {
        READY(0),
        OFF(0),
        NULL(0);

        private final double value;

        ShooterState(double value) {
            this.value = value;
        }

        public double setTargetVelocity() {
            return value;
        }

        public String toString() {
            switch (this) {
                case READY:
                    return "READY";
                case OFF:
                    return "OFF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum TurretState {
        TRACKING(0),
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
                case TRACKING:
                    return "TRACKING";
                case OFF:
                    return "OFF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum HoodState {
        READY(0.12),
        DEFAULT(0.12),
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
                    return "READY";
                case DEFAULT:
                    return "DEFAULT";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public void init(HardwareMap hardwareMap) {
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        shooterR = hardwareMap.get(DcMotorEx.class, "shooterR");
        shooterL = hardwareMap.get(DcMotorEx.class, "shooterL");

        hoodL = hardwareMap.servo.get("hoodL");
        hoodR = hardwareMap.servo.get("hoodR");

        turretR = hardwareMap.servo.get("turretR");
        turretL = hardwareMap.servo.get("turretL");

        hoodR.setDirection(Servo.Direction.REVERSE);
        hoodL.setDirection(Servo.Direction.FORWARD);
        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterL.setDirection(DcMotorSimple.Direction.FORWARD);

        shooterL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        shooterR.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(16, 0, 0, 35));
        shooterL.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(16, 0, 0, 35));

//        sensor = hardwareMap.get(AnalogInput.class, "potentiometer");
    }

    double shooterPower = 2;

    public void setShooterVelocity(double velocity) {
        if (shooterPower != velocity) {
            shooterR.setVelocity(velocity);
            shooterL.setVelocity(velocity);
        }
        shooterPower = velocity;
        if (velocity == ShooterState.OFF.value) {
            CurrentShooterState = ShooterState.OFF;
        }
    }

    public void setHoodPosition(double position) {
        hoodR.setPosition(position);
        hoodL.setPosition(position);
    }

    public void runT2Angle(double angle) {

        // Clamp to physical limits
        if (angle < 98) angle = 98;
        if (angle > 270) angle = 270;

        // Linear mapping
        double position = (angle - 98.0) / 172.0;

        turretR.setPosition(position);
        turretL.setPosition(position);
    }

//    public void recallbrateRobotPose() {
//        Drive.robotPose = new Pose2D(
//                DistanceUnit.INCH,
//                limelight.getLatestResult().getBotpose_MT2().getPosition().x,
//                limelight.getLatestResult().getBotpose_MT2().getPosition().y,
//                AngleUnit.DEGREES,
//                limelight.getLatestResult().getBotpose_MT2().getOrientation().getYaw(AngleUnit.DEGREES)
//        );
//    }

    public double getShooterVelocity() {
        return shooterL.getVelocity(AngleUnit.DEGREES);
    }

    public double offsetAngle = 0;
    public double TargetAnge = 0;

    public double getTurretAngle(Pose robotPose) {
        double dx;
        double targetY = 140;

        if (Drive.red) {dx = 140 - robotPose.getX();
        } else {dx = robotPose.getX() - 4;
        } double dy = targetY - robotPose.getY();

        double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
        double robotAngle = robotPose.getHeading();
        robotAngle = (robotAngle % 360 + 360) % 360;

        double relativeAngle =  robotAngle - targetAngle;
        relativeAngle = (relativeAngle % 360 + 360) % 360;

        RobotLog.a("robotAngle: " + String.valueOf(robotAngle) +
                " | TargetAngle: " + String.valueOf(targetAngle) +
                " | RelativeAngle: " + String.valueOf(relativeAngle));

        return MathFunctions.clamp(relativeAngle, 90, 270);
    }

    public static double hoodAngle(double goalDistance) {
        return MathFunctions.clamp(
                -(0.000000172398) * Math.pow(goalDistance, 3)
                + (0.0000422278) * Math.pow(goalDistance, 2)
                - (0.00381218) * goalDistance
                + 0.173328, 0, 1900
        );
    }

    public static double shooterVelocity(double goalDistance) {
        return MathFunctions.clamp(
                (0.0173883) * Math.pow(goalDistance, 2)
                + (4.04479) * goalDistance
                + 1099.94273, 0.02, 0.75
        );
    }

    //UPDATE
    public void update(Pose robotpose, double goalDistance) {
//        Shooter State
        if (RequestedShooterState != CurrentShooterState) {
            if (RequestedShooterState == ShooterState.READY) {
                shooterL.setVelocity(shooterVelocity(goalDistance));
                shooterR.setVelocity(shooterVelocity(goalDistance));
                CurrentShooterState = RequestedShooterState;
            } else if (RequestedShooterState == ShooterState.OFF) {
                shooterL.setVelocity(0);
                shooterR.setVelocity(0);
                CurrentShooterState = RequestedShooterState;
            }
        }
//        Turret State
        if (RequestedTurretState != CurrentTurretState) {
            if (RequestedTurretState == TurretState.TRACKING) {
                runT2Angle(getTurretAngle(robotpose));
                CurrentTurretState = RequestedTurretState;
            } else if (RequestedTurretState == TurretState.OFF) {
                runT2Angle(180);
                CurrentTurretState = RequestedTurretState;
            }
        }
        //Hood State
        if (RequestedHoodState != CurrentHoodState) {
            if (RequestedHoodState ==  HoodState.READY) {
                setHoodPosition(hoodAngle(goalDistance));
                CurrentHoodState = RequestedHoodState;
            } else if (RequestedHoodState == HoodState.DEFAULT) {
                setHoodPosition(0.12);
                CurrentHoodState = RequestedHoodState;
            }
        }

        if (RequestedShooterState == ShooterState.READY) {

            // Update shooter velocity
            shooterL.setVelocity(shooterVelocity(goalDistance));
            shooterR.setVelocity(shooterVelocity(goalDistance));

            // Update hood position
            setHoodPosition(hoodAngle(goalDistance));

            // Update turret angle

            CurrentShooterState = ShooterState.READY;
        }
    }

    public boolean isComplete() {
        return RequestedShooterState == CurrentShooterState &&
                RequestedTurretState == CurrentTurretState &&
                RequestedHoodState == CurrentHoodState;
    }
    public void requestState(TopLevelState state) {
        RequestedShooterState = state.shooterState;
        RequestedTurretState = state.turretState;
        RequestedHoodState = state.hoodState;
    }
}
