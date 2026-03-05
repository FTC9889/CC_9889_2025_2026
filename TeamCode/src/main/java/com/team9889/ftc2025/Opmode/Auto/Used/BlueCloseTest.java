package com.team9889.ftc2025.Opmode.Auto.Used;

import android.view.contentcapture.DataRemovalRequest;

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

@Autonomous(name = "🟦BlueCloseTest", group = "Blue", preselectTeleOp = "Teleop")
public class BlueCloseTest extends OpMode {
    Robot mRobot = new Robot();

    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    ElapsedTime shotTimer = new ElapsedTime();

    /* ---------------- FLIPPED POSES WITH +180° HEADINGS ---------------- */

    private final Pose startPose   = new Pose(25.696, 129.149, Math.toRadians(-40));
    private final Pose shootPreload= new Pose(56.000, 103.000);
    private final Pose turn        = new Pose(46.000, 83.832);
    private final Pose pickUp1     = new Pose(19.000, 83.832);
    private final Pose gateControl = new Pose(21.2, 75);
    private final Pose gate1       = new Pose(24.000, 75.700);
    private final Pose gate2       = new Pose(20.000, 75.700);
    private final Pose shoot1      = new Pose(56.000, 103.000);
    private final Pose pickUp2     = new Pose(12.627, 58.701);
    private final Pose control2    = new Pose(60.346, 58.680);
    private final Pose shoot2      = new Pose(60.080, 72.924);
    private final Pose pickup3     = new Pose(52.000, 36.000);
    private final Pose control3    = new Pose(60.500, 36.000);
    private final Pose pickup3_2   = new Pose(12.500, 36.000);
    private final Pose shoot3      = new Pose(shoot2.getX(), shoot2.getY());
    private final Pose pickup4     = new Pose(9.500, 52.800);
    private final Pose pickup4_2   = new Pose(9.500, 8.500);
    private final Pose shoot4      = new Pose(10.500, 52.800);
    private final Pose shoot4_2    = new Pose(shoot2.getX(), shoot2.getY());
    private final Pose park        = new Pose(59.700, 58.800);


    @Override
    public void init() {
        Drive.red = false;
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
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOB1);
                setPathState(101);
                break;
            case 101:
                if (!follower.isBusy() && mRobot.isComplete()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(1);
                }
                break;
            case 1:
                RobotLog.a("RobotC", mRobot.isComplete());
                RobotLog.a("timer", shotTimer);
                if (mRobot.isComplete() && shotTimer.milliseconds() > 1000){
                    follower.followPath(paths.Pickup1);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOB1);
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
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOB2);
                    follower.followPath(paths.Pickup2);
                    setPathState(1003);
                }
                break;
            case 1003:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Gate1);
                    setPathState(1004);
                }
                break;
            case 1004:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Gate2);
                    setPathState(4);
                }
                break;
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
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    follower.followPath(paths.Pickup3);
                    setPathState(6);
                }
                break;
            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Pickup3_2);
                    setPathState(7);
                }
                break;
            case 7:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Shoot3);
                    setPathState(8);
                }
                break;
            case 8:
                if (!follower.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(9);
                }
                break;
            case 9:
                if (!follower.isBusy() && shotTimer.milliseconds() > 1000) {
                    follower.followPath(paths.Pickup4);
                    setPathState(10);
                }
                break;
            case 10:
                if (!follower.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    follower.followPath(paths.Pickup4_2);
                    setPathState(11);
                }
                break;
            case 11:
                if (!follower.isBusy() && mRobot.isComplete()) {
                    follower.followPath(paths.Shoot4);
                    setPathState(12);
                }
                break;
            case 12:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Shoot4_2);
                    setPathState(13);
                }
                break;
            case 13:
                if (!follower.isBusy()) {
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
                follower.followPath(paths.Park);
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
                    .setLinearHeadingInterpolation(Math.toRadians(-40), Math.toRadians(270)) // +180
                    .build();

            Turn = follower.pathBuilder()
                    .addPath(new BezierLine(shootPreload, turn))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(180)) // +180
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierLine(turn, pickUp1))
                    .setTangentHeadingInterpolation()
                    .build();

            Gate1 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickUp1, gateControl, gate1))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
                    .build();

            Gate2 = follower.pathBuilder()
                    .addPath(new BezierLine(gate1, gate2))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(gate2, shoot1))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270))
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot1, control2, pickUp2))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp2, shoot2))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270)) // -180 + 180 = 0
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
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
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
        Robot.robotPose = follower.getPose();
    }
}