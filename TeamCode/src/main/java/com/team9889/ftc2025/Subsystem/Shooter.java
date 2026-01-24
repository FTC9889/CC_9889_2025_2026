package com.team9889.ftc2025.Subsystem;



import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Shooter {

//    public Limelight3A limelight;
    public Servo hoodR, hoodL;
//            turretR, turretL;
    public DcMotorEx shooterR, shooterL;
    //    public AnalogInput sensor;
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

//    public TurretState CurrentTurretState = TurretState.NULL;
//    public TurretState RequestedTurretState = TurretState.NULL;
//
//    public TurretState getCurrentTurretState() {
//        return CurrentTurretState;
//    }
//
//    public TurretState getRequestedTurretState() {
//        return RequestedTurretState;
//    }
//
//    public void setRequestedTurretState(TurretState turretState) {
//        RequestedTurretState = turretState;
//    }

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
//                TurretState.TRAKING,
                HoodState.SHOOT),
        READY_FAR(ShooterState.FAR,
//                TurretState.TRAKING,
                HoodState.SHOOT),
        DEFAULT(ShooterState.OFF,
//                TurretState.TRAKING,
                HoodState.DEFAULT);

        final ShooterState shooterState;
//        final TurretState turretState;
        final HoodState hoodState;

        TopLevelState(ShooterState shooterState,
//                      TurretState turretState,
                      HoodState hoodState) {
            this.shooterState = shooterState;
//            this.turretState = turretState;
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
        //        FAR(0.15),
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
//                case FAR:
//                    return "FAR";
                case DEFAULT:
                    return "DEFAULT";
                case NULL:
                default:
                    return "NULL";
            }
        }
    }

    //
    public void init(HardwareMap hardwareMap) {
//        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        shooterR = hardwareMap.get(DcMotorEx.class, "shooterR");
        shooterL = hardwareMap.get(DcMotorEx.class, "shooterL");

        hoodL = hardwareMap.servo.get("hoodL");
        hoodR = hardwareMap.servo.get("hoodR");

//        turretR = hardwareMap.servo.get("turretR");
//        turretL = hardwareMap.servo.get("turretL");

        hoodR.setDirection(Servo.Direction.REVERSE);
        hoodL.setDirection(Servo.Direction.FORWARD);
        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterL.setDirection(DcMotorSimple.Direction.FORWARD);
//        turretR.setDirection(Servo.Direction.REVERSE);

//        shooterR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//        shooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

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

//    public void runT2Angle(double angle) {
//        double position = Math.max(0, Math.min(1, angle / 360.0));
//        turretR.setPosition(position);
//        turretL.setPosition(position);
//    }

//    public double getTurrentAngle() {
//        return turretL.getPosition() / 360;
//    }

//    public void recallbrateRobotPose() {
//        Drive.robotPose = new Pose2D(DistanceUnit.INCH, limelight.getLatestResult().getBotpose_MT2().getPosition().x, limelight.getLatestResult().getBotpose_MT2().getPosition().y, AngleUnit.DEGREES, limelight.getLatestResult().getBotpose_MT2().getOrientation().getYaw(AngleUnit.DEGREES));
//    }

    public double getShooterVelocity() {
        return shooterL.getVelocity();
    }

    public double PoseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH);
//    public double getTargetTrakingTurretAngle() {
//
//        if (!red) {
//            PoseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH) + 8;
//        } else if (red) {
//            PoseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH) - 136;
//        }
//        double PoseYFromTarget = Drive.robotPose.getY(DistanceUnit.INCH) - 133;
//
//        double TargetAngle = 90 - Math.toDegrees(Math.atan2(PoseXFromTarget, PoseYFromTarget));
//        double RobotAngle = Drive.robotPose.getHeading(AngleUnit.DEGREES);
//        double TurretAngle = getTurrentAngle();
//
//        double TargetMovementAngle = -(RobotAngle + TargetAngle);
//
//        if (TargetMovementAngle > 180) {
//            TargetMovementAngle -= 360;
//        } else if (TargetMovementAngle < -180) {
//            TargetMovementAngle += 360;
//        }
//
//        double Turret2StartPos = 0 - TurretAngle;
//        double TurretMovementAngle = Turret2StartPos + TargetMovementAngle;
//        double RequestedAngle = getTurrentAngle() + TurretMovementAngle;
//
//        if (RequestedAngle > 360) {
//            RequestedAngle -= 360;
//        } else if (RequestedAngle < 360) {
//            RequestedAngle += 360;
//        }
//        return RequestedAngle;
//    }

    public static boolean red = false;

    public double farRedTurretAngle = 67;
    public double farBlueTurretAngle = 103;
    public double closeRedTurretAngle = 45;
    public double closeBlueTurretAngle = 135;

    public boolean robotYFar = Drive.robotPose.getY(DistanceUnit.INCH) < 48;

    public double getPredctedTurretAngle(boolean red) {
        if (red) {
            if (robotYFar) {
                return farRedTurretAngle;
            } else {
                return closeRedTurretAngle;
            }
        } else if (!red) {
            if (robotYFar) {
                return farBlueTurretAngle;
            } else {
                return closeBlueTurretAngle;
            }
        } else {
            return 180;
        }
    }

    //UPDATE
    public void update() {

//        Shooter State
        if (RequestedShooterState != CurrentShooterState) {
            shooterL.setVelocity(RequestedShooterState.setTargetVelocity());
            shooterR.setVelocity(RequestedShooterState.setTargetVelocity());
            CurrentShooterState = RequestedShooterState;
        }
//        Turret State
//      if (RequestedTurretState != CurrentTurretState) {
//            if (RequestedTurretState == TurretState.TRAKING) {
//                runT2Angle(getTargetTrakingTurretAngle());
//                CurrentTurretState = RequestedTurretState;
//                recallbrateRobotPose();
//            } else if (RequestedTurretState == TurretState.PREDICT) {
//                if (Drive.robotPose.getY(DistanceUnit.INCH) < 49) {
//                    runT2Angle(getPredctedTurretAngle(red));
//                }
//                CurrentTurretState = RequestedTurretState;
//            } else if (RequestedTurretState == TurretState.OFF) {
//                runT2Angle(180);
//                CurrentTurretState = RequestedTurretState;
//            }
//      }
        //Hood State
        if (RequestedHoodState != CurrentHoodState) {
            if (!red) {
                PoseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH) + 8;
            } else if (red) {
                PoseXFromTarget = Drive.robotPose.getX(DistanceUnit.INCH) - 136;
            }
            double PoseYFromTarget = Drive.robotPose.getY(DistanceUnit.INCH) - 133;

            double TargetAngle = 90 - Math.toDegrees(Math.atan2(PoseXFromTarget, PoseYFromTarget));
            double RobotAngle = Drive.robotPose.getHeading(AngleUnit.DEGREES);
            double DistanceAway = Math.hypot(PoseXFromTarget, PoseYFromTarget);


            if (RequestedHoodState != CurrentHoodState) {
                setHoodPosition(RequestedHoodState.value);
                CurrentHoodState = RequestedHoodState;
            }
        }
    }

    public boolean isComplete() {
        return RequestedShooterState == CurrentShooterState &&
//                RequestedTurretState == CurrentTurretState &&
                RequestedHoodState == CurrentHoodState;
    }
    public void requestState(TopLevelState state) {
        RequestedShooterState = state.shooterState;
//        RequestedTurretState = state.turretState;
        RequestedHoodState = state.hoodState;
    }
}
