package com.team9889.ftc2025.Subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Robot {

    public Drive mDrive = new Drive();
//    public Intake mIntake = new Intake();
    public Shooter mShooter = new Shooter();
//    public Spindexer mSD = new Spindexer();

    public void init(HardwareMap hardwareMap) {
        mDrive.init(hardwareMap);
//        mIntake.init(hardwareMap);
        mShooter.init(hardwareMap);
//        mSD.init(hardwareMap);
    }
//
    public double TurretRunToPosition() {
        double PoseXFromTarget = mDrive.getPose().getX(DistanceUnit.INCH) - 134;
        double PoseYFromTarget = mDrive.getPose().getY(DistanceUnit.INCH) - 133;

        double TargetAngle = 90 - Math.toDegrees(Math.atan2(PoseXFromTarget, PoseYFromTarget));
        double RobotAngle = mDrive.getPose().getHeading(AngleUnit.DEGREES);
        double TurretAngle = mShooter.getTurrentPos();

        double TargetMovementAngle = -(RobotAngle + TargetAngle);

        if (TargetMovementAngle > 180) {
            TargetMovementAngle -= 360;
        } else if (TargetMovementAngle < -180) {
            TargetMovementAngle += 360;
        }

        double Turret2StartPos = 0 - TurretAngle;

        double TurretMovementAngle = Turret2StartPos + TargetMovementAngle;

        return TurretMovementAngle;
    }


    public double DistaceFromTarget() {
        double PoseXFromTarget = mDrive.getPose().getX(DistanceUnit.INCH) - 134;
        double PoseYFromTarget = mDrive.getPose().getY(DistanceUnit.INCH) - 133;

        return Math.hypot(PoseXFromTarget, PoseYFromTarget);
    }

////
////    public double Convert2Turrent2Pos(double robot2TurrentAngle) {
////        double RobotAngle = mDrive.getPose().getHeading(AngleUnit.DEGREES);
////
////        double Turret2StartPos = 0 - TurretAngle;
////
////        double TurretMovementAngle = Turret2StartPos + robot2TurrentAngle;
////
////        return TurretMovementAngle;
////    }


}
