package com.team9889.ftc2025.Subsystem.Shooter;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2025.Subsystem.Drive;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ShooterStateMachine {
    public Limelight3A limelight;
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
        AUTOCLOSER(ShooterState.AUTOCLOSE, TurretState.AUTOCLOSER, HoodState.AUTOCLOSE),
        AUTOCLOSEB(ShooterState.AUTOCLOSE, TurretState.AUTOCLOSEB, HoodState.AUTOCLOSE),
        AUTOR1(ShooterState.AUTO1, TurretState.AUTOR1, HoodState.AUTO1),
        AUTOR2(ShooterState.AUTO2, TurretState.AUTOR2, HoodState.AUTO2),
        AUTOB1(ShooterState.AUTO1, TurretState.AUTOB1, HoodState.AUTO1),
        AUTOB2(ShooterState.AUTO2, TurretState.AUTOB2, HoodState.AUTO2),
        AUTORF(ShooterState.AUTOF, TurretState.AUTORF, HoodState.AUTOF),
        AUTOBF(ShooterState.AUTOF, TurretState.AUTOBF, HoodState.AUTOF),
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
        AUTOCLOSE(0),
        AUTO1(1300),
        AUTO2(1550),
        AUTOF(1900),
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
                case AUTOCLOSE:
                    return "AUTOCLOSE";
                case AUTO1:
                    return "AUTO1";
                case AUTO2:
                    return "AUTO2";
                case AUTOF:
                    return "AUTOF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum TurretState {
        TRACKING(0),
        OFF(0),
        AUTOCLOSER(240),
        AUTOCLOSEB(221.5),
        AUTOR1(240),
        AUTOR2(221.5),
        AUTOB1(120),
        AUTOB2(138.5),
        AUTORF(203),
        AUTOBF(157),
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
                case AUTOR1:
                    return "AUTOR1";
                case AUTOR2:
                    return "AUTOR2";
                case AUTOCLOSER:
                    return "AUTOCLOSER";
                case AUTOCLOSEB:
                    return "AUTOCLOSEB";
                case AUTOB1:
                    return "AUTOB1";
                case AUTOB2:
                    return "AUTOB2";
                case AUTORF:
                    return "AUTORF";
                case AUTOBF:
                    return "AUTOBF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum HoodState {
        READY(0.12),
        DEFAULT(0.12),
        AUTOCLOSE(0),
        AUTO1(0.075),
        AUTO2(0.05),
        AUTOF(0.023),
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
                case AUTOCLOSE:
                    return "AUTOCLOSE";
                case AUTO1:
                    return "AUTO1";
                case AUTO2:
                    return "AUTO2";
                case AUTOF:
                    return "AUTOF";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public void init(HardwareMap hardwareMap, IMU imu) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");


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

        shooterR.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(50, 0, 0, 15));
        shooterL.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(50, 0, 0, 15));

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
        MathFunctions.clamp(position, 0.02, 0.12);
        hoodR.setPosition(position);
        hoodL.setPosition(position);
    }

    public void runT2Angle(double angle) {

        // Clamp to physical limits
        angle = MathFunctions.clamp(angle, 98, 270);

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
    Pose redTarget = new Pose(140, 140);
    Pose blueTarget = new Pose(4, 140);

    public double getTurretAngle(Pose robotPose) {
        double dx;
        double targetY = 140;

        if (Drive.red) {
            dx = 140 - robotPose.getX();
        } else {
            dx =  4 - robotPose.getX();
        }

        double dy = targetY - robotPose.getY();

        double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
        double robotAngle = Math.toDegrees(robotPose.getHeading());
        robotAngle = (robotAngle % 360 + 360) % 360;
        double relativeAngle;

        relativeAngle = robotAngle - targetAngle;


        relativeAngle = (relativeAngle % 360 + 360) % 360;

        RobotLog.a("robotAngle: " + String.valueOf(robotAngle) +
                " | TargetAngle: " + String.valueOf(targetAngle) +
                " | RelativeAngle: " + String.valueOf(relativeAngle));

        return MathFunctions.clamp(relativeAngle, 98, 270);
    }

    public static double shooterVelocity(double goalDistance) {
        if (goalDistance > 120)
            goalDistance -= 5;
        return MathFunctions.clamp(
                (-0.000115264 * Math.pow(goalDistance, 3))
                        + (0.0415589 * Math.pow(goalDistance, 2))
                        + 2.83322 * goalDistance
                        + 1137.17211,
                0,
                2000
        );
    }

    public static double hoodAngle(double goalDistance) {
        return MathFunctions.clamp(
                (0.000000000870861 * Math.pow(goalDistance, 4))
                        - (0.000000436878 * Math.pow(goalDistance, 3))
                        + (0.000070371 * Math.pow(goalDistance, 2))
                        - (0.00498223 * goalDistance)
                        + 0.186651,
                0.02,
                0.12
        );
    }

    double limelightAngle;
    double limelightX;
    double limelightY;

    double shotTime = ;
    Pose FrobotPose;

    //UPDATE
    public void update(Pose robotpose, Vector robotVelocity, double AngularVelocity) {
        FrobotPose = new Pose(
                robotpose.getX() + (robotVelocity.getXComponent() * shotTime),
                robotpose.getY() + (robotVelocity.getYComponent() * shotTime),
                robotpose.getHeading() + (AngularVelocity * shotTime)
        );

        double goalDistance = FrobotPose.distanceFrom(Drive.red ? redTarget : blueTarget);
        goalDistance -= 8.5;

        if (limelight.getLatestResult() != null) {
            limelightAngle = limelight.getLatestResult().getBotpose_MT2().getOrientation().getYaw() + 90;
            limelightAngle = (limelightAngle % 360 + 360) % 360;
            Drive.yawOffset = robotpose.getHeading() - limelightAngle;

            limelightX = robotpose.getX() - (limelight.getLatestResult().getBotpose_MT2().getPosition().x * 39.37);
            limelightY = robotpose.getY() - (limelight.getLatestResult().getBotpose_MT2().getPosition().y * 39.37);

        }

        //Shooter State
        if (RequestedShooterState == ShooterState.AUTO1 || RequestedShooterState == ShooterState.AUTO2 || RequestedShooterState == ShooterState.AUTOF) {
            setShooterVelocity(RequestedShooterState.value);
            CurrentShooterState = RequestedShooterState;
        } else if (RequestedShooterState != CurrentShooterState) {
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
        //Turret State
        if (
                RequestedTurretState == TurretState.AUTOR1
                || RequestedTurretState == TurretState.AUTOR2
                || RequestedTurretState == TurretState.AUTOB1
                || RequestedTurretState == TurretState.AUTOB2
                || RequestedTurretState == TurretState.AUTORF
                || RequestedTurretState == TurretState.AUTOBF
        ) {
            runT2Angle(RequestedTurretState.value);
            CurrentTurretState = RequestedTurretState;
        } else if (RequestedTurretState != CurrentTurretState) {
            if (RequestedTurretState == TurretState.TRACKING) {
                runT2Angle(getTurretAngle(robotpose));
                CurrentTurretState = RequestedTurretState;
            } else if (RequestedTurretState == TurretState.OFF) {
                runT2Angle(180);
                CurrentTurretState = RequestedTurretState;
            }
        }
        //Hood State
        if (RequestedHoodState == HoodState.AUTO1 || RequestedHoodState == HoodState.AUTO2 || RequestedHoodState == HoodState.AUTOF) {
            setHoodPosition(RequestedHoodState.value);
            CurrentHoodState = RequestedHoodState;
        } else if (RequestedHoodState != CurrentHoodState) {
            if (RequestedHoodState == HoodState.READY) {
                setHoodPosition(hoodAngle(goalDistance));
                CurrentHoodState = RequestedHoodState;
            } else if (RequestedHoodState == HoodState.DEFAULT) {
                setHoodPosition(0.12);
                CurrentHoodState = RequestedHoodState;
            }
        }
        //LOOP
        if (RequestedShooterState == ShooterState.READY) {
            // Update shooter velocity
            shooterL.setVelocity(shooterVelocity(goalDistance));
            shooterR.setVelocity(shooterVelocity(goalDistance));
            // Update hood position
            setHoodPosition(hoodAngle(goalDistance));
            // Update turret angle
            runT2Angle(getTurretAngle(robotpose));
            CurrentShooterState = RequestedShooterState;
            CurrentHoodState = RequestedHoodState;
            CurrentTurretState = RequestedTurretState;
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
