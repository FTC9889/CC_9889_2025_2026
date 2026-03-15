package com.team9889.ftc2025.Opmode.Auto.Used.Far;

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
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

@Autonomous(name = "🟦⬇️♻️BlueCycleFar", group = "Blue", preselectTeleOp = "Teleop")

public class Blue2Alt2Far extends OpMode {

    Robot mRobot = new Robot();

    private Timer pathTimer;
    private int pathState;

    private PathChain Shoot1, Shoot2, Turn, Pickup1, Pickup2, Wiggle1, Pickup2_2, Pickup3, Shoot3, Park;

    ElapsedTime shotTimer = new ElapsedTime();
    ElapsedTime waitTimer = new ElapsedTime();
    ElapsedTime matchTImer = new ElapsedTime();

    /* ---------------- POSES ---------------- */

    private final Pose startPose = new Pose(57, 8.230, Math.toRadians(270));

    private final Pose turn = new Pose(57, 35.000);

    private final Pose pickup1 = new Pose(12, 35);

    private final Pose shoot1 = new Pose(57, 15.000);

    private final Pose pickup2 = new Pose(14, 15);
    private final Pose pickup2_2 = new Pose(14, 9.3);
    private final Pose wiggle1 = new Pose(12, 9.7);

    private final Pose shoot2 = new Pose(57, 15.500);

    private final Pose pickup3 = new Pose(14, 9.7);
    private final Pose shoot3 = new Pose(57, 15.500);

    private final Pose pickup4 = new Pose(0.000, 8.500);
    private final Pose shoot4 = new Pose(57, 15.500);

    private final Pose park = new Pose(27.000, 15.500);
    @Override
    public void init() {
        mRobot.mShooter.AutoTurretPredict = true;
        Drive.red = false;

        mRobot.init(hardwareMap);

        pathTimer = new Timer();

        buildPaths();

        mRobot.mDrive.setStartingPose(startPose);

        mRobot.mIntake.requestState(Intake.TopLevelState.OFF);

        mRobot.update();
    }

    @Override
    public void start() {

        pathTimer.resetTimer();

        setPathState(0);
    }

    /* ---------------- BUILD PATHS ---------------- */

    public void buildPaths() {

        Turn = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(startPose, turn))
                .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(180))
                .build();

        Pickup1 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(turn, pickup1))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build();

        Shoot1 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup1, shoot1))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(240))
                .build();

        Pickup2 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot1, pickup2))
                .setLinearHeadingInterpolation(Math.toRadians(240), Math.toRadians(195))
                .build();

        Pickup2_2 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup2, pickup2_2))
                .setConstantHeadingInterpolation(Math.toRadians(195))
                .build();

        Wiggle1 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup2_2, wiggle1))
                .setLinearHeadingInterpolation(Math.toRadians(195), Math.toRadians(189))
                .build();

        Shoot2 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(wiggle1, shoot2))
                .setLinearHeadingInterpolation(Math.toRadians(188), Math.toRadians(240))
                .build();

        Pickup3 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot2, pickup3))
                .setLinearHeadingInterpolation(Math.toRadians(240), Math.toRadians(180))
                .build();

        Shoot3 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup3, shoot3))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(240))
                .build();

        Park = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot3, park))
                .setLinearHeadingInterpolation(Math.toRadians(210), Math.toRadians(180))
                .build();
    }

    /* ---------------- STATE MACHINE ---------------- */

    public void autonomousPathUpdate() {

        switch (pathState) {

            case 0:
                matchTImer.reset();
                mRobot.mShooter.AutoTurretPredictPose = startPose;
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                waitTimer.reset();
                setPathState(1);
                break;
            case 1:
                if (!mRobot.mDrive.isBusy() && waitTimer.milliseconds() > 1500) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(2);
                }
                break;
            case 2:
                if (shotTimer.milliseconds() > 1200) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mDrive.followPath(Pickup2);
                    setPathState(3);
                }
                break;
            case 3:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(Pickup2_2);
                    setPathState(5);
                }
                break;
            case 5:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(Wiggle1);
                    setPathState(6);
                }
                break;
            case 6:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    mRobot.mShooter.AutoTurretPredictPose = Shoot2.endPose();
                    mRobot.mDrive.followPath(Shoot2);
                    setPathState(7);
                }
                break;
            case 7:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(8);
                }
                break;
            case 8:
                if (shotTimer.milliseconds() > 1200) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(9);
                }
                break;
            case 9:
                if (mRobot.isComplete()) {
                    mRobot.mDrive.followPath(Turn);
                    setPathState(91);
                }
                break;
            case 91:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(Pickup1);
                    setPathState(10);
                }
                break;
            case 10:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(Shoot1);
                    mRobot.mShooter.AutoTurretPredictPose = Shoot1.endPose();
                    setPathState(11);
                }
                break;
            case 11:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    waitTimer.reset();
                    setPathState(12);
                }
                break;
            case 12:
                if (!mRobot.mDrive.isBusy() && waitTimer.milliseconds() > 1500) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    mRobot.mIntake.setGatePosition(Intake.GateState.OPEN.setTargetPower());
                    shotTimer.reset();
                    setPathState(13);
                }
                break;

            case 13:
                if (shotTimer.milliseconds() > 1200) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
//                    mRobot.mDrive.followPath(Pickup3Prediction1);
                    setPathState(14);
                }
                break;
            case 14:
                if (mRobot.isComplete()) {
                    mRobot.mDrive.followPath(Pickup3);
                    setPathState(15);
                }
                break;
            case 15:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.AutoTurretPredictPose = Shoot3.endPose();
                    mRobot.mDrive.followPath(Shoot3);
                    setPathState(16);
                }
                break;
            case 16:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    waitTimer.reset();
                    setPathState(17);
                }
                break;
            case 17:
                if (!mRobot.mDrive.isBusy() && waitTimer.milliseconds() > 1500) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    mRobot.mIntake.setGatePosition(Intake.GateState.OPEN.setTargetPower());
                    shotTimer.reset();
                    setPathState(18);
                }
                break;
            case 18:
                if (shotTimer.milliseconds() > 1200) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    if (matchTImer.seconds() > 24) {
                        setPathState(1000);
                    } else {
                        setPathState(14);
                    }
                }
                break;
            case 1000:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    mRobot.mDrive.followPath(Park);
                    setPathState(1001);
                }
                break;
            case 1001:
                if (!mRobot.mDrive.isBusy()) {
                    setPathState(-4);
                }
                break;
        }
    }

    public void setPathState(int state) {

        pathState = state;

        pathTimer.resetTimer();
    }

    /* ---------------- INIT / LOOP ---------------- */



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

    @Override
    public void stop() {

        Robot.robotPose = mRobot.mDrive.getPose();
    }
}