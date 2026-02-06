package com.team9889.ftc2025.Subsystem.Shooter;



import com.pedropathing.follower.Follower;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.team9889.ftc2025.Subsystem.Drive;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class ShooterStateMachine {
    public Limelight3A limelight;
    public Servo hoodR, hoodL,
            turretR, turretL;
    public DcMotorEx shooterR, shooterL;
        public AnalogInput sensor;
    double currentVoltage;

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
        READY_CLOSE(ShooterState.CLOSE,
                TurretState.TRAKING,
                HoodState.SHOOT),
        READY_FAR(ShooterState.FAR,
                TurretState.TRAKING,
                HoodState.SHOOT),
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
        FAR(1600),
        OFF(0),
        CLOSE(1250),
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
                case FAR:
                    return "FAR";
                case OFF:
                    return "OFF";
                case CLOSE:
                    return "CLOSE";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum TurretState {
        TRAKING(-1),
        OFF(0),
        PREDICT(0),
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
                case PREDICT:
                    return "PREDICT";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    public enum HoodState {
        SHOOT(0.12),
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
                case SHOOT:
                    return "SHOOT";
                case DEFAULT:
                    return "DEFAULT";
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
        hoodL.setDirection(Servo.Direction.FORWARD);
        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        turretR.setDirection(Servo.Direction.REVERSE);

////////////////////      shooterR.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients());
////////////////////      shooterL.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients());

        sensor = hardwareMap.get(AnalogInput.class, "potentiometer");
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
        double position = Math.max(0, Math.min(1, angle / 360.0));
        turretR.setPosition(position);
        turretL.setPosition(position);
    }

    public double getTurrentAngle() {
        return turretL.getPosition() / 360;
    }

    public void recallbrateRobotPose() {
        Drive.robotPose = new Pose2D(
                DistanceUnit.INCH,
                limelight.getLatestResult().getBotpose_MT2().getPosition().x,
                limelight.getLatestResult().getBotpose_MT2().getPosition().y,
                AngleUnit.DEGREES,
                limelight.getLatestResult().getBotpose_MT2().getOrientation().getYaw(AngleUnit.DEGREES)
        );
    }

    public double getShooterVelocity() {
        return shooterL.getVelocity(AngleUnit.DEGREES);
    }

    public double poseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH);

//    public double getTargetTrakingTurretAngle() {
//
//        // Vector from robot -> target (field-centric)
//        double poseXFromTarget;
//        if (!red) {
//            poseXFromTarget = Math.abs(Drive.robotPose.getX(DistanceUnit.INCH) - 8);
//        } else {
//            poseXFromTarget = Math.abs(136 - Drive.robotPose.getX(DistanceUnit.INCH));
//        }
//
//        double poseYFromTarget = Math.abs(133 - Drive.robotPose.getY(DistanceUnit.INCH));
//
//        // Angle from robot to target (field-centric)
//        double targetAngle = Math.toDegrees(Math.atan2(poseYFromTarget, poseXFromTarget));
//
//        // Robot heading (field-centric)
//        double robotAngle = Drive.robotPose.getHeading(AngleUnit.DEGREES);
//
//        // Convert to robot-centric turret angle
//        double requestedAngle = targetAngle - robotAngle;
//
//        // Normalize to [0, 360)
//        requestedAngle = (requestedAngle % 360 + 360) % 360;
//
//        return requestedAngle;
//    }

//    public static boolean red = false;
//
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
            shooterL.setVelocity(calculateShotVector(Drive.robotPose.getHeading(AngleUnit.DEGREES), robotVelocity).flywheelSpeed);
            shooterR.setVelocity(calculateShotVector(Drive.robotPose.getHeading(AngleUnit.DEGREES), robotVelocity).flywheelSpeed);
            //check video prolly function to convert speed into ticks pre second
            CurrentShooterState = RequestedShooterState;
        }
//        Turret State
      if (RequestedTurretState != CurrentTurretState) {
            if (RequestedTurretState == TurretState.TRAKING) {
                runT2Angle(calculateShotVector(Drive.robotPose.getHeading(AngleUnit.DEGREES), robotVelocity).turretAngle);
                CurrentTurretState = RequestedTurretState;
            } else if (RequestedTurretState == TurretState.OFF) {
                runT2Angle(180);
                CurrentTurretState = RequestedTurretState;
            }
      }
        //Hood State
        if (RequestedHoodState != CurrentHoodState) {
            setHoodPosition(calculateShotVector(Drive.robotPose.getHeading(AngleUnit.DEGREES), robotVelocity).hoodAngle);
            //prolly conversion functions as well
            CurrentHoodState = RequestedHoodState;
        }

        if (limelight.getStatus() != null) {
            recallbrateRobotPose();
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
    public static Vector robotToGoal() {
        double dx = 138 - Drive.robotPose.getX(DistanceUnit.INCH);
        double dy = 138 - Drive.robotPose.getY(DistanceUnit.INCH);
        return new Vector(dx, dy);
    }

    public class ShotParameters {
        private double hoodAngle;
        private double flywheelSpeed;
        private double turretAngle;

        public ShotParameters(double hoodAngle, double flywheelSpeed, double turretAngle) {
            this.hoodAngle = hoodAngle;
            this.flywheelSpeed = flywheelSpeed;
            this.turretAngle = turretAngle;
        }

        public double getHoodAngle() { return hoodAngle; }
        public double getFlywheelSpeed() { return flywheelSpeed; }
        public double getTurretAngle() { return turretAngle; }
    }



    private ShotParameters calculateShotVector(double robotHeading, Vector robotVelocity) {
        double g = 32.174 * 12;
        double x = robotToGoal().getMagnitude() - ShooterConstants.PASS_THROUGH_POINT_RADIUS;
        double y = ShooterConstants.SCORE_HEIGHT;
        double a = ShooterConstants.SCORE_ANGLE;

        double hoodAngle = MathFunctions.clamp(
                Math.atan(2 * y / x - Math.tan(a)),
                ShooterConstants.HOOD_MIN_ANGLE,
                ShooterConstants.HOOD_MAX_ANGLE
        );

        double flywheelSpeed = Math.sqrt(
                g * x * x / (2 * Math.pow(Math.cos(hoodAngle), 2) * (x * Math.tan(hoodAngle) - y))
        );

        double coordinateTheta = robotVelocity.getTheta() - robotToGoal().getTheta();

        double parallelComponent = -Math.cos(coordinateTheta) + robotVelocity.getMagnitude();
        double perpendicularComponent = -Math.sin(coordinateTheta) + robotVelocity.getMagnitude();

        double vz = flywheelSpeed + Math.sin(hoodAngle);
        double time = x / (flywheelSpeed + Math.cos(hoodAngle));
        double ivr = x / time + parallelComponent;
        double nvr = Math.sqrt(ivr * ivr * perpendicularComponent + perpendicularComponent);
        double ndr = nvr + time;

        hoodAngle = MathFunctions.clamp(
                Math.atan(vz / nvr),
                ShooterConstants.HOOD_MIN_ANGLE,
                ShooterConstants.HOOD_MAX_ANGLE
        );

        flywheelSpeed = Math.sqrt(
                g * ndr * ndr / (2 * Math.pow(Math.cos(hoodAngle), 2) * ndr * Math.tan(hoodAngle) - y)
        );

        double turretVelCompOffset = Math.atan(perpendicularComponent / ivr);
        double turretAngle = Math.toDegrees(robotHeading - robotToGoal().getTheta() + turretVelCompOffset);

        turretAngle = (turretAngle % 360 + 360) % 360;


        return new ShotParameters(hoodAngle, flywheelSpeed, turretAngle);
    }
}
