package com.team9889.ftc2025.Opmode.Auto.Used;

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

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "🟥RedCloseTest", group = "Red", preselectTeleOp = "Teleop")
public class RedCloseTest extends OpMode {
    Robot mRobot = new Robot();

    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    ElapsedTime shotTimer = new ElapsedTime();

    /* ---------------- FLIPPED POSES WITH +180° HEADINGS ---------------- */

    private final Pose startPose   = new Pose(118.304, 129.149, Math.toRadians(220));   // 37 + 180
    private final Pose shootPreload= new Pose(88.000, 103.000);
    private final Pose turn        = new Pose(98, 83.832);
    private final Pose pickUp1     = new Pose(125, 83.832);
    private final Pose gateControl = new Pose(122.8, 75);
    private final Pose gate1       = new Pose(120, 73.7);
    private final Pose gate2       = new Pose(124, 73.7);
    private final Pose shoot1      = new Pose(88.000, 103.000);
    private final Pose pickUp2     = new Pose(131.373, 58.701);
    private final Pose control2    = new Pose(83.654, 58.680);
    private final Pose shoot2      = new Pose(83.920, 72.924);
    private final Pose pickup3     = new Pose(92, 36);
    private final Pose control3    = new Pose(83.5, 36);
    private final Pose pickup3_2   = new Pose(131.5, 36);
    private final Pose shoot3      = new Pose(83.920, 72.924);
    private final Pose pickup4     = new Pose(133.5, 52.8);
    private final Pose pickup4_2   = new Pose(133.5, 9.5);
    private final Pose shoot4     = new Pose(133.5, 52.8);
    private final Pose shoot4_2   = new Pose(84.3, 72.924);
    private final Pose park        = new Pose(84.3, 58.8);


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
                mRobot.mDrive.followPath(paths.ShootPreload);
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOR1);
                setPathState(101);
                break;
            case 101:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(1);
                }
                break;
            case 1:
                RobotLog.a("RobotC", mRobot.isComplete());
                RobotLog.a("timer", shotTimer);
                if (mRobot.isComplete() && shotTimer.milliseconds() > 1000){
                    mRobot.mDrive.followPath(paths.Pickup1);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOR1);
                    setPathState(1001);
                }
                break;
            case 1001:
                if (mRobot.isComplete()) {
                    mRobot.mDrive.followPath(paths.Turn);
                    setPathState(1002);
                }
                break;
            case 1002:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup1);
                    setPathState(2);
                }
                break;
            case 2:
                if (mRobot.isComplete() && !mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Shoot1);
                    setPathState(21);
                }
                break;
            case 21:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(3);
                }
                break;
            case 3:
                if (mRobot.mIntake.isComplete() && shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOR2);
                    mRobot.mDrive.followPath(paths.Pickup2);
                    setPathState(1003);
                }
                break;
            case 1003:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Gate1);
                    setPathState(1004);
                }
                break;
            case 1004:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Gate2);
                    setPathState(4);
                }
                break;
            case 4:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    mRobot.mDrive.followPath(paths.Shoot2);
                    setPathState(51);
                }
                break;
            case 51:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(5);
                }
                break;
            case 5:
                if (!mRobot.mDrive.isBusy() && shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(paths.Pickup3);
                    setPathState(6);
                }
                break;
            case 6:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup3_2);
                    setPathState(7);
                }
                break;
            case 7:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Shoot3);
                    setPathState(8);
                }
                break;
            case 8:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(9);
                }
                break;
            case 9:
                if (!mRobot.mDrive.isBusy() && shotTimer.milliseconds() > 1000) {
                    mRobot.mDrive.followPath(paths.Pickup4);
                    setPathState(10);
                }
                break;
            case 10:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(paths.Pickup4_2);
                    setPathState(11);
                }
                break;
            case 11:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    mRobot.mDrive.followPath(paths.Shoot4);
                    setPathState(12);
                }
                break;
            case 12:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Shoot4_2);
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
                if (shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    setPathState(15);
                }
                break;
            case 15:
                mRobot.mDrive.followPath(paths.Park);
                setPathState(-1000);
                break;
        }
    }

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public class Paths {
        public PathChain ShootPreload, Turn, Pickup1, Gate1, Gate2,  Shoot1, Pickup2, Shoot2, Pickup3, Pickup3_2, Shoot3, Pickup4, Pickup4_2, Shoot4, Shoot4_2, Park;

        public Paths(Follower follower) {

            ShootPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shootPreload))
                    .setLinearHeadingInterpolation(Math.toRadians(220), Math.toRadians(270)) // +180
                    .build();

            Turn = follower.pathBuilder()
                    .addPath(new BezierLine(shootPreload, turn))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(0)) // +180
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierLine(turn, pickUp1))
                    .setTangentHeadingInterpolation()
                    .build();

            Gate1 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickUp1,gateControl, gate1))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(270))
                    .build();

            Gate2 = follower.pathBuilder()
                    .addPath(new BezierLine(gate1, gate2))
                    .setConstantHeadingInterpolation(Math.toRadians(90))
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(gate2, shoot1))
                    .setLinearHeadingInterpolation(Math.toRadians(5), Math.toRadians(270)) // -175 + 180 = 5
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot1, control2, pickUp2))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp2, shoot2))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(270)) // -180 + 180 = 0
                    .build();

            Pickup3 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot2, control3, pickup3))
                    .setTangentHeadingInterpolation()
                    .build();

            Pickup3_2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup3, pickup3_2))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot3 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup3_2, shoot3))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(270))
                    .build();

            Pickup4 = follower.pathBuilder()
                    .addPath(new BezierLine(shoot3, pickup4))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Pickup4_2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup4, pickup4_2))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Shoot4 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup4_2, shoot4))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Shoot4_2 = follower.pathBuilder()
                    .addPath(new BezierLine(shoot4, shoot4_2))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Park = follower.pathBuilder()
                    .addPath(new BezierLine(shoot4_2, park))
                    .setTangentHeadingInterpolation()
                    .build();
        }
    }

    @Override
    public void stop() {
        Robot.robotPose = mRobot.mDrive.getPose();
    }
}