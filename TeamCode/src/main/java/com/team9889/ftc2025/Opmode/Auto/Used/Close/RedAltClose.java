package com.team9889.ftc2025.Opmode.Auto.Used.Close;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

@Autonomous(name = "🟥🔺♻️RedAltClose", group = "Red", preselectTeleOp = "Teleop")
public class RedAltClose extends OpMode {
    Robot mRobot = new Robot();

    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    ElapsedTime shotTimer = new ElapsedTime();
    ElapsedTime matchTimer = new ElapsedTime();

    /* ---------------- FLIPPED POSES WITH +180° HEADINGS ---------------- */

    private final Pose startPose   = new Pose(118.304, 129.149, Math.toRadians(220));   // 37 + 180
    private final Pose shootPreload= new Pose(86.000, 78.000);
    private final Pose pickUp1     = new Pose(133.000, 58.500);
    private final Pose Control1 = new Pose(84.000, 59.000);
    private final Pose shoot      = new Pose(86.000, 78.000);
    private final Pose pickUpGate     = new Pose(132.000, 61.000);

    private final Pose pickUp2      = new Pose(126.000, 83.500);
    private final Pose Control2     = new Pose(92.000, 90.000);
    private final Pose park        = new Pose(112.000, 70.000);


    @Override
    public void init() {
        Drive.red = true;
        mRobot.init(hardwareMap);

        pathTimer = new Timer();

        paths = new Paths(mRobot.mDrive);
        mRobot.mDrive.setStartingPose(startPose);
        mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
        mRobot.update();
    }

    @Override
    public void start() {
        pathTimer.resetTimer();
        pathState = 0;
    }

    @Override
    public void loop() {

        mRobot.mDrive.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", mRobot.mDrive.getPose().getX());
        telemetry.addData("Y", mRobot.mDrive.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(mRobot.mDrive.getPose().getHeading()));
        telemetry.update();
        mRobot.update();
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                matchTimer.reset();
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOCLOSER);
                mRobot.mDrive.followPath(paths.ShootPreload);
                setPathState(1);
                break;
            case 1:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(2);
                }
                break;
            case 2:
                if (shotTimer.milliseconds() > 800) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(3);
                }
                break;
            case 3:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup1);
                    setPathState(4);
                }
                break;
            case 4:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Shoot1);
                    setPathState(5);
                }
                break;
            case 5:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(6);
                }
                break;
            case 6:
                if (shotTimer.milliseconds() > 800) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(7);
                }
                break;
            case 7:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.GatePickUp);
                    setPathState(8);
                }
                break;
            case 8:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.GateShoot);
                    setPathState(9);
                }
                break;
            case 9:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(10);
                }
                break;
            case 10:
                if (shotTimer.milliseconds() > 800) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    if (matchTimer.seconds() > 25) {
                        setPathState(11);
                    } else {
                        setPathState(7);
                    }
                }
                break;
            case 11:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup2);
                    setPathState(12);
                }
                break;
            case 12:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Shoot2);
                    setPathState(13);
                }
                break;
            case 13:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(14);
                }
                break;
            case 14:
                if (shotTimer.milliseconds() > 800) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mDrive.followPath(paths.Park);
                    setPathState(15);
                }
                break;
            case 15:
                if (!mRobot.mDrive.isBusy()) {
                    setPathState(-10000);
                }
                break;
        }
    }

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public class Paths {
        public PathChain ShootPreload, GatePickUp, Pickup1, GateShoot,  Shoot1, Pickup2, Shoot2, Park;

        public Paths(Follower follower) {

            ShootPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shootPreload))
                    .setLinearHeadingInterpolation(Math.toRadians(220), Math.toRadians(300)) // +180
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierCurve(shootPreload, Control1, pickUp1))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(0)) // +180
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp1, shoot))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(300)) // +180
                    .build();

            GatePickUp = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot, pickUpGate))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(30))
                    .build();

            GateShoot = follower.pathBuilder()
                    .addPath(new BezierLine(pickUpGate, shoot))
                    .setLinearHeadingInterpolation(Math.toRadians(30), Math.toRadians(300)) // -180 + 180 = 0
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickUp2, shoot))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(300))
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot, Control2, pickUp2))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(0)) // +180
                    .build();

            Park = follower.pathBuilder()
                    .addPath(new BezierLine(shoot, park))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(90))
                    .build();
        }
    }

    @Override
    public void stop() {
        Robot.robotPose = mRobot.mDrive.getPose();
    }
}