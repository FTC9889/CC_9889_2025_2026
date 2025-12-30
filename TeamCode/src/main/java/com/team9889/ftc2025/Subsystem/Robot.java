package com.team9889.ftc2025.Subsystem;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Robot {
    public static Pose robotPose = new Pose(0, 0, Math.toRadians(0));

    //    public Drive mDrive = new Drive();
    public Drive mDrive = new Drive();
    public Follower mFollower;
    public Intake mIntake = new Intake();
    public Shooter mShooter = new Shooter();
//    public Spindexer mSD = new Spindexer();

    public void init(HardwareMap hardwareMap) {
        mDrive.init(hardwareMap);
        mIntake.init(hardwareMap);
        mShooter.init(hardwareMap);
        mFollower = Constants.createFollower(hardwareMap);
//        mSD.init(hardwareMap);
    }

    public void TurretSetTraking() {
        if (mShooter.RequestedTurretState == Shooter.TurretState.TRAKING && mShooter.traking) {

            double PoseXFromTarget = mDrive.getPose().getX(DistanceUnit.INCH) - 134;
            double PoseYFromTarget = mDrive.getPose().getY(DistanceUnit.INCH) - 133;

            double TargetAngle = 90 - Math.toDegrees(Math.atan2(PoseXFromTarget, PoseYFromTarget));
            double RobotAngle = mDrive.getPose().getHeading(AngleUnit.DEGREES);
            double TurretAngle = mShooter.getTurrentAngle();

            double TargetMovementAngle = -(RobotAngle + TargetAngle);

            if (TargetMovementAngle > 180) {
                TargetMovementAngle -= 360;
            } else if (TargetMovementAngle < -180) {
                TargetMovementAngle += 360;
            }

            double Turret2StartPos = 0 - TurretAngle;
            double TurretMovementAngle = Turret2StartPos + TargetMovementAngle;
            double movement = mShooter.getTurrentAngle() + TurretMovementAngle;

            if (movement > 360) {
                movement -= 360;
            } else if (movement < 360) {
                movement += 360;
            }
            mShooter.runT2Angle(movement);
            mShooter.CurrentTurretState = mShooter.RequestedTurretState;
        }
    }



    public double DistaceFromTarget() {
        double PoseXFromTarget = mDrive.getPose().getX(DistanceUnit.INCH) - 134;
        double PoseYFromTarget = mDrive.getPose().getY(DistanceUnit.INCH) - 133;

        return Math.hypot(PoseXFromTarget, PoseYFromTarget);
    }
}
