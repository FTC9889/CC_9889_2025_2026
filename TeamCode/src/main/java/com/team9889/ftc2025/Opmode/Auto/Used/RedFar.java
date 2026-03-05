package com.team9889.ftc2025.Opmode.Auto.Used;

import android.view.contentcapture.DataRemovalRequest;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
@Disabled
@Autonomous(name = "🟥RedFar", group = "Red", preselectTeleOp = "Teleop")
public class RedFar extends OpMode {
    Robot mRobot = new Robot();

    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    ElapsedTime shotTimer = new ElapsedTime();
    ElapsedTime waitTimer = new ElapsedTime();

    /* ---------------- FLIPPED POSES WITH +180° HEADINGS ---------------- */

    private final Pose startPose   = new Pose(88, 8.000, Math.toRadians(-90));
    private final Pose shoot       = new Pose(88, 16.500);
    private final Pose turn        = new Pose(96.5, 35.500);
    private final Pose pickUp1     = new Pose(131.5, 35.500);
    private final Pose shoot1      = new Pose(shoot.getX(), shoot.getY());
    private final Pose pickup2     = new Pose(131.5, 35.500);
    private final Pose pickup2_2   = new Pose(131.5, 9.500);
    private final Pose shoot2      = new Pose(shoot.getX(), shoot.getY());
    private final Pose park         = new Pose(104, 16.500);


    @Override
    public void init() {
        Drive.red = true;
        mRobot.init(hardwareMap);

        follower = Constants.createFollower(hardwareMap);
        pathTimer = new Timer();

        paths = new Paths(follower);
        follower.setStartingPose(startPose);
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

        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
        mRobot.update();
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(paths.ShootPreload);
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTORF);
                setPathState(101);
                break;
            case 101:
                if (!follower.isBusy() && mRobot.isComplete()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    waitTimer.reset();
                    setPathState(10001);
                }
                break;
            case 10001:
                if (waitTimer.milliseconds() > 1500) {
                    shotTimer.reset();
                    setPathState(1);
                }

                break;
            case 1:
                if (mRobot.isComplete() && shotTimer.milliseconds() > 1000){
                    follower.followPath(paths.Pickup1);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    setPathState(1001);
                }
                break;
            case 1001:
                if (mRobot.isComplete()) {
                    follower.followPath(paths.Turn);
                    setPathState(1002);
                }
                break;
            case 1002:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Pickup1);
                    setPathState(2);
                }
                break;
            case 2:
                if (mRobot.isComplete() && !follower.isBusy()) {
                    follower.followPath(paths.Shoot1);
                    setPathState(21);
                }
                break;
            case 21:
                if (!follower.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(3);
                }
                break;
            case 3:
                if (mRobot.mIntake.isComplete() && shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTORF);
                    follower.followPath(paths.Pickup2);
                    setPathState(4);
                }
                break;
            case 31:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Pickup2_2);
                }
            case 4:
                if (!follower.isBusy() && mRobot.isComplete()) {
                    follower.followPath(paths.Shoot2);
                    setPathState(51);
                }
                break;
            case 51:
                if (!follower.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy() && shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                    follower.followPath(paths.Park);
                    setPathState(-2);
                }
                break;
        }
    }

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public class Paths {
        public PathChain ShootPreload, Turn, Pickup1, Pickup2_2,  Shoot1, Pickup2, Shoot2, Park;

        public Paths(Follower follower) {

            ShootPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shoot))
                    .setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            Turn = follower.pathBuilder()
                    .addPath(new BezierLine(shoot, turn))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(0)) // +180
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierLine(turn, pickUp1))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp1, shoot1))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(270))
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierLine(shoot1, pickup2))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Pickup2_2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup2, pickup2_2))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup2_2, shoot2))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Park = follower.pathBuilder()
                    .addPath(new BezierLine(shoot2, park))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(0)) // -180 + 180 = 0
                    .build();
        }
    }

    @Override
    public void stop() {
        Robot.robotPose = follower.getPose();
    }
}