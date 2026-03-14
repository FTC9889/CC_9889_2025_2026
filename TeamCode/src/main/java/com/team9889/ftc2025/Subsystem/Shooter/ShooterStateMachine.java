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

    boolean shooterReadyEvent = false;
    boolean lastShooterReady = false;

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
        AUTOCLOSER(ShooterState.AUTOALTCLOSE, TurretState.AUTOCLOSEALTR, HoodState.AUTOALTCLOSE),
        AUTOCLOSEB(ShooterState.AUTOALTCLOSE, TurretState.AUTOCLOSEALTB, HoodState.AUTOALTCLOSE),
        AUTO2CLOSER(ShooterState.AUTO2ALTCLOSE, TurretState.AUTO2CLOSEALTR, HoodState.AUTO2ALTCLOSE),
        AUTO2CLOSEB(ShooterState.AUTO2ALTCLOSE, TurretState.AUTO2CLOSEALTB, HoodState.AUTO2ALTCLOSE),
        AUTOR1(ShooterState.AUTO1, TurretState.AUTOR1, HoodState.AUTO1),
        AUTOR2(ShooterState.AUTO2, TurretState.AUTOR2, HoodState.AUTO2),
        AUTOB1(ShooterState.AUTO1, TurretState.AUTOB1, HoodState.AUTO1),
        AUTOB2(ShooterState.AUTO2, TurretState.AUTOB2, HoodState.AUTO2),
        AUTORF(ShooterState.AUTOF, TurretState.AUTORF, HoodState.AUTOF),
        AUTOBF(ShooterState.AUTOF, TurretState.AUTOBF, HoodState.AUTOF),
        DEFAULT(ShooterState.OFF,
                TurretState.TRACKING,
                HoodState.READY),
        TEST_DEFAULT(ShooterState.OFF,
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
        AUTOALTCLOSE(1525),
        AUTO2ALTCLOSE(1400),
        AUTO1(1350),
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
                case AUTOALTCLOSE:
                    return "AUTOALTCLOSE";
                case AUTO2ALTCLOSE:
                    return "AUTO2ALTCLOSE";
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
        AUTOCLOSEALTR(255),
        AUTOCLOSEALTB(155),
        AUTO2CLOSEALTR(270),
        AUTO2CLOSEALTB(90),
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
                case AUTOCLOSEALTR:
                    return "AUTOCLOSEALTR";
                case AUTOCLOSEALTB:
                    return "AUTOCLOSEALTB";
                case AUTO2CLOSEALTR:
                    return "AUTO2CLOSEALTR";
                case AUTO2CLOSEALTB:
                    return "AUTO2CLOSEALTB";
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
        AUTOALTCLOSE(0.055),
        AUTO2ALTCLOSE(0.07),
        AUTO1(0.07),
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
                case AUTOALTCLOSE:
                    return "AUTOALTCLOSE";
                case AUTO2ALTCLOSE:
                    return "AUTO2ALTCLOSE";
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
//        position += 0.005;
//        position = MathFunctions.clamp(position, 0.02, 0.12);
        hoodR.setPosition(position);
        hoodL.setPosition(position);
    }

    public void runT2Angle(double angle) {

        // Clamp to physical limits
        angle = MathFunctions.clamp(angle, 98, 270);

        // Linear mapping
        double position = ((angle - 98.0) / 172.0);

        turretR.setPosition(position);
        turretL.setPosition(position);
    }

    public double getShooterVelocity() {
        return shooterL.getVelocity(AngleUnit.DEGREES);
    }

    public double offsetAngle = 0;
    public double TargetAnge = 0;
    Pose redTarget = new Pose(130, 131.5);
    Pose blueTarget = new Pose(14, 131.5);

    public boolean canShoot = false;

    public double getTurretAngle(Pose robotPose) {
        double Xt;
        double Yt;

        Xt = robotPose.getX() - 2*Math.cos(robotPose.getHeading());
        Yt = robotPose.getY() - 2*Math.sin(robotPose.getHeading());

        double dx;
        double targetY = 130;

        if (Drive.red) {
            dx = 130 - Xt;
        } else {
            dx =  14 - Xt;
        }

        double dy = targetY - Yt;

        double targetAngle = Math.toDegrees(Math.atan2(dy, dx));
        double robotAngle = Math.toDegrees(robotPose.getHeading());
        robotAngle = (robotAngle % 360 + 360) % 360;
        double relativeAngle;

        relativeAngle = robotAngle - targetAngle;
        relativeAngle = (relativeAngle % 360 + 360) % 360;

        RobotLog.a("robotAngle: " + String.valueOf(robotAngle) +
                " | TargetAngle: " + String.valueOf(targetAngle) +
                " | RelativeAngle: " + String.valueOf(relativeAngle));

        if (relativeAngle > 270 || relativeAngle < 98) {
            canShoot = false;
        } else {
            canShoot = true;
        }

        return MathFunctions.clamp(relativeAngle, 98, 270);
    }

    public boolean JasonWillRememberNotToAirballAndBecomeStephCurryAndHitTheNightNightDeepThreeAgainstOKCInTheLastPointSixSeconds = false;

    public static double shooterVelocity(double goalDistance) {
        return MathFunctions.clamp(
                0.0000160901 * Math.pow(goalDistance, 4)
                - 0.00594158 * Math.pow(goalDistance, 3)
                + 0.763031 * Math.pow(goalDistance, 2)
                - 33.29185 * goalDistance
                + 1753.0786,
                0,
                2200
        );
    }

    public static double hoodAngle(double goalDistance) {
        return MathFunctions.clamp(2.4204e-9 * Math.pow(goalDistance, 4)
                - 8.89975e-7 * Math.pow(goalDistance, 3)
                + 0.00011796 * Math.pow(goalDistance, 2)
                - 0.00752694 * goalDistance
                + 0.254199,
                0,
                0.125
        );
    }

    double limelightAngle;
    double limelightX;
    double limelightY;


//    double shotTime = 0.000000000000000000000000000000000000000;
//    Pose FrobotPose;

    public boolean shooterReady = false;

    public double getDistanceFromGoal(Pose robotPose) {
        double goalDistance = robotPose.distanceFrom(Drive.red ? redTarget : blueTarget);
        return  goalDistance;
    }

    //UPDATE
    public void update(Pose robotpose, Vector robotVelocity, double AngularVelocity) {

        if (shooterReady && !lastShooterReady) {
            shooterReadyEvent = true;
        } else {
            shooterReadyEvent = false;
        }

        lastShooterReady = shooterReady;

//        FrobotPose = new Pose(
//                robotpose.getX() + (robotVelocity.getXComponent() * shotTime),
//                robotpose.getY() + (robotVelocity.getYComponent() * shotTime),
//                robotpose.getHeading() + (AngularVelocity * shotTime)
//        );

        double goalDistance = robotpose.distanceFrom(Drive.red ? redTarget : blueTarget);
        RobotLog.a("goalDistance: " + String.valueOf(goalDistance));

        if (limelight.getLatestResult() != null) {
            if (limelight.getLatestResult().getStaleness() < 250) {
                limelightAngle = limelight.getLatestResult().getBotpose_MT2().getOrientation().getYaw() + 90;
                limelightAngle = (limelightAngle % 360 + 360) % 360;
                Drive.yawOffset = robotpose.getHeading() - limelightAngle;

                limelightX = robotpose.getX() - (limelight.getLatestResult().getBotpose_MT2().getPosition().x * 39.37);
                limelightY = robotpose.getY() - (limelight.getLatestResult().getBotpose_MT2().getPosition().y * 39.37);
            }
        }

        //Shooter State
        if (RequestedShooterState == ShooterState.AUTO1 ||
                RequestedShooterState == ShooterState.AUTO2 ||
                RequestedShooterState == ShooterState.AUTOF ||
        RequestedShooterState == ShooterState.AUTOALTCLOSE
        || RequestedShooterState == ShooterState.AUTO2ALTCLOSE) {
            setShooterVelocity(RequestedShooterState.value);
            CurrentShooterState = RequestedShooterState;
        } else if (RequestedShooterState != CurrentShooterState) {
            if (RequestedShooterState == ShooterState.READY) {
                shooterL.setVelocity(shooterVelocity(goalDistance));
                shooterR.setVelocity(shooterVelocity(goalDistance));
                CurrentShooterState = RequestedShooterState;
                if (shooterR.getVelocity() == shooterVelocity(goalDistance)) {
                    shooterReady = true;
                } else if (shooterR.getVelocity() != RequestedShooterState.value) {
                    shooterReady = false;
                }
            } else if (RequestedShooterState == ShooterState.OFF) {
                shooterL.setVelocity(0);
                shooterR.setVelocity(0);
                shooterReady = false;
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
                        || RequestedTurretState == TurretState.AUTOCLOSEALTB
                        || RequestedTurretState == TurretState.AUTOCLOSEALTR
                        || RequestedTurretState == TurretState.AUTO2CLOSEALTB
                        || RequestedTurretState == TurretState.AUTO2CLOSEALTR
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
        if (RequestedHoodState == HoodState.AUTO1
                || RequestedHoodState == HoodState.AUTO2
                || RequestedHoodState == HoodState.AUTOF
        || RequestedHoodState == HoodState.AUTOALTCLOSE
        || RequestedHoodState == HoodState.AUTO2ALTCLOSE) {
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
        if (CurrentShooterState == ShooterState.READY) {
            shooterL.setVelocity(shooterVelocity(goalDistance));
            shooterR.setVelocity(shooterVelocity(goalDistance));
        }

        if (CurrentTurretState == TurretState.TRACKING) {
            runT2Angle(smoothAngle(getBacklashTurretAngle(getTurretAngle(robotpose))));
        }

        if (CurrentHoodState == HoodState.READY) {
            setHoodPosition(hoodAngle(goalDistance));
        }
    }

    double filteredAngle = 0;

    public double smoothAngle(double newAngle) {
        double alpha = 0.2; // smaller = smoother
        filteredAngle = filteredAngle + alpha * (newAngle - filteredAngle);
        return filteredAngle;
    }

    double lastTurretAngle = 0;
    double lastDirection = 1;

    public double getBacklashTurretAngle(double currentTargetAngle) {
        double x = currentTargetAngle - lastTurretAngle;
        if (Math.abs(x) > 0.001) {
            lastDirection = x / Math.abs(x);
        }

        currentTargetAngle = currentTargetAngle - lastDirection * 2;

        lastTurretAngle = currentTargetAngle;

        return currentTargetAngle;
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

    public boolean getShooterReadyEvent() {
        return shooterReadyEvent;
    }
}
