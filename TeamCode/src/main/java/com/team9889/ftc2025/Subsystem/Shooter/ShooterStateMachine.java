package com.team9889.ftc2025.Subsystem.Shooter;



import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.team9889.ftc2025.Subsystem.Drive;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

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
                TurretState.TRAKING,
                HoodState.READY),
        DEFAULT(ShooterState.OFF,
                TurretState.TRAKING,
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
        TRAKING(0),
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

        shooterR.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

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
        // 90  = 0;
        // 180 = 0.5;
        // 270 = 1;
        double position;

        if(angle >= 270) {
            position = 1;
        } else if (angle <= 82) {
            position = 0;
        } else {
            position = (angle - 90) / 172;
        }

        turretR.setPosition(position);
        turretL.setPosition(position);
    }

    public double getTurrentAngle() {
        return turretL.getPosition() / 360;
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

    public double poseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH);

    public class ShotParameters {
        private double hoodPosition;
        private double flywheelSpeed;
        private double turretAngle;

        public ShotParameters(double hoodAngle, double flywheelSpeed, double turretAngle) {
            this.hoodPosition = hoodAngle;
            this.flywheelSpeed = flywheelSpeed;
            this.turretAngle = turretAngle;
        }

        public double getHoodAngle() { return hoodPosition; }
        public double getFlywheelSpeed() { return flywheelSpeed; }
        public double getTurretAngle() { return turretAngle; }
    }

    public ShotParameters shootingParameters() {
        double poseXFromTarget;
        if (!red) {
            poseXFromTarget = Math.abs(Drive.robotPose.getX(DistanceUnit.INCH) - 7.5);
        } else {
            poseXFromTarget = Math.abs(136.5 - Drive.robotPose.getX(DistanceUnit.INCH));
        }

        double poseYFromTarget = Math.abs(136.5 - Drive.robotPose.getY(DistanceUnit.INCH));

        double distanceFromGoal = Math.hypot(poseXFromTarget, poseYFromTarget);

        double hoodAngle = 0;
        double shooterVelocity = 0;
        /// Turret
        // Vector from robot -> target (field-centric)


        // Angle from robot to target (field-centric)
        double targetAngle = Math.toDegrees(Math.atan2(poseYFromTarget, poseXFromTarget));

        // Robot heading (field-centric)
        double robotAngle = Drive.robotPose.getHeading(AngleUnit.DEGREES);

        // Convert to robot-centric turret angle
        double requestedAngle = targetAngle - robotAngle ;

        // Normalize to [0, 360)
        requestedAngle = (requestedAngle % 360 + 360) % 360;
        requestedAngle = MathFunctions.clamp(requestedAngle, 90, 270);

        /// Hood

        hoodAngle = -((0.000000172398) * distanceFromGoal * distanceFromGoal * distanceFromGoal) + (0.0000422278 * distanceFromGoal * distanceFromGoal) - (0.00381218 * distanceFromGoal) + 0.173328;

        /// Shooter

        shooterVelocity = (0.0173883 * distanceFromGoal * distanceFromGoal) + (4.04479 * distanceFromGoal) + 1099.94273;

        return new ShotParameters(hoodAngle, shooterVelocity, requestedAngle);
    }



    public static boolean red = false;

//    public double farRedTurretAngle = 67;
//    public double farBlueTurretAngle = 103;
//    public double closeRedTurretAngle = 45;
//    public double closeBlueTurretAngle = 135;
//
//    public boolean robotYFar = Drive.robotPose.getY(DistanceUnit.INCH) < 48;
//
//    public double getPredctedTurretAngle(boolean red) {
//        double requestedAngle;
//
//        if (red) {
//            requestedAngle = robotYFar ? farRedTurretAngle : closeRedTurretAngle;
//        } else {
//            requestedAngle = robotYFar ? farBlueTurretAngle : closeBlueTurretAngle;
//        }
//
//        // Normalize to [0, 360)
//        requestedAngle = (requestedAngle % 360 + 360) % 360;
//
//        return requestedAngle;
//    }



    //UPDATE
    public void update(Vector robotVelocity) {

//        Shooter State
        if (RequestedShooterState != CurrentShooterState) {
//            shooterL.setVelocity();
//            shooterR.setVelocity();
            //check video prolly function to convert speed into ticks pre second
            CurrentShooterState = RequestedShooterState;
        }
//        Turret State
      if (RequestedTurretState != CurrentTurretState) {
            if (RequestedTurretState == TurretState.TRAKING) {
                runT2Angle(shootingParameters().turretAngle);
                //turret stuff
                CurrentTurretState = RequestedTurretState;
            } else if (RequestedTurretState == TurretState.OFF) {
                runT2Angle(180);
                CurrentTurretState = RequestedTurretState;
            }
      }
        //Hood State
        if (RequestedHoodState != CurrentHoodState) {
            //hood stuff
            CurrentHoodState = RequestedHoodState;
        }

//        if (limelight.getStatus() != null) {
//            recallbrateRobotPose();
//        }
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
