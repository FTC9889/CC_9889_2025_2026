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

@Autonomous(name = "🟦⬆️BlueStandard", group = "Blue", preselectTeleOp = "Teleop")
public class BlueStandard extends OpMode {
    Robot mRobot = new Robot();

    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    ElapsedTime shotTimer = new ElapsedTime();
    ElapsedTime waitTImer = new ElapsedTime();

    /* ---------------- FLIPPED POSES WITH +180° HEADINGS ---------------- */

    private final Pose startPose   = new Pose(25.696, 129.149, Math.toRadians(320));   // flipped
    private final Pose shootPreload= new Pose(56.000, 103.000);
    private final Pose turn        = new Pose(46, 83.832);
    private final Pose pickUp1     = new Pose(21, 83.832);
    private final Pose gateControl = new Pose(24, 58.701);
    private final Pose gate2       = new Pose(14.5, 73.7);

    private final Pose shoot1      = new Pose(53.5, 91.7);
    private final Pose shoot1Con   = new Pose(53.5, 83.832);

    private final Pose pickUp2     = new Pose(14.5, 58.701);
    private final Pose control2    = new Pose(64.346, 60);
    private final Pose shoot2      = new Pose(60.08, 72.924);
    private final Pose pickup3     = new Pose(16, 36);
    private final Pose control3    = new Pose(60.2305, 32.629);
    private final Pose shoot3      = new Pose(60.08, 72.924);
    private final Pose shoot3Con1  = new Pose(25.3, 36.1);
    private final Pose shoot3Con2  = new Pose(60.4, 45.6);
    private final Pose pickup4     = new Pose(7, 40);
    private final Pose pickup4Con1 = new Pose(60.3, 67.789);
    private final Pose pickup4Con2 = new Pose(12, 68);

    private final Pose pickup4_2   = new Pose(0, 10.5);
    private final Pose shoot4      = new Pose(60.08, 72.924);
    private final Pose shoot4Con1  = new Pose(33.9, 35.1);
    private final Pose shoot4Con2  = new Pose(60.1, 38.3);

    private final Pose pickup5     = new Pose(14, 9);

    private final Pose park        = new Pose(7, 30);


    @Override
    public void init() {
        mRobot.mShooter.AutoTurretPredict = true;
        Drive.red = false;
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
                mRobot.mShooter.AutoTurretPredictPose = paths.ShootPreload.endPose();
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                setPathState(1);
                break;
            case 1:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(2);
                }
                break;
            case 2:
                RobotLog.a("RobotC", mRobot.isComplete());
                RobotLog.a("timer", shotTimer);
                if (mRobot.isComplete() && shotTimer.milliseconds() > 1000){
                    mRobot.mDrive.followPath(paths.Pickup1);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
//                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTO2CLOSER);
                    setPathState(3);
                }
                break;
            case 3:
                if (mRobot.isComplete()) {
                    mRobot.mDrive.followPath(paths.Turn);
                    setPathState(4);
                }
                break;
            case 4:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup1);
                    setPathState(5);
                }
                break;
            case 5:
                if (mRobot.isComplete() && !mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.intakeL.setPower(0);
                    mRobot.mShooter.AutoTurretPredictPose = paths.Shoot1.endPose();
                    mRobot.mDrive.followPath(paths.Shoot1);
                    setPathState(6);
                }
                break;
            case 6:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    mRobot.mIntake.intakeL.setPower(1);
                    shotTimer.reset();
                    setPathState(7);
                }
                break;
            case 7:
                if (mRobot.mIntake.isComplete() && shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
//                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTOR2);
                    mRobot.mDrive.followPath(paths.Pickup2);
                    setPathState(8);
                }
                break;
            case 8:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Gate1);
                    waitTImer.reset();
                    setPathState(9);
                }
                break;
            case 9:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    mRobot.mIntake.intakeL.setPower(0);
                    mRobot.mDrive.followPath(paths.Shoot2);
                    mRobot.mShooter.AutoTurretPredictPose = paths.Shoot2.endPose();
                    setPathState(10);
                }
                break;
            case 10:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    mRobot.mIntake.intakeL.setPower(1);
                    shotTimer.reset();
                    setPathState(11);
                }
                break;
            case 11:
                if (!mRobot.mDrive.isBusy() && shotTimer.milliseconds() > 1000) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(paths.Pickup3);
                    setPathState(12);
                }
                break;
            case 12:
                if (!mRobot.mDrive.isBusy()) {
//                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.AUTORS2);
                    mRobot.mIntake.intakeL.setPower(0);
                    mRobot.mShooter.AutoTurretPredictPose = paths.Shoot3.endPose();
                    mRobot.mDrive.followPath(paths.Shoot3);
                    setPathState(13);
                }
                break;
            case 13:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    mRobot.mIntake.intakeL.setPower(1);
                    shotTimer.reset();
                    setPathState(14);
                }
                break;
            case 14:
                if (!mRobot.mDrive.isBusy() && shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(paths.Pickup5);
                    setPathState(15);
                }
                break;
            case 15:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(paths.Pickup5_2);
                    waitTImer.reset();
                    setPathState(16);
                }
                break;
            case 16:
                if ((!mRobot.mDrive.isBusy() && mRobot.isComplete()) || waitTImer.milliseconds() > 500) {
                    mRobot.mIntake.intakeL.setPower(0);
                    mRobot.mShooter.AutoTurretPredictPose = paths.Shoot4.endPose();
                    mRobot.mDrive.followPath(paths.Shoot4);
                    setPathState(17);
                }
                break;
            case 17:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    mRobot.mIntake.intakeL.setPower(1);
                    setPathState(18);
                }
                break;
            case 18:
                if (shotTimer.milliseconds() > 1000) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    setPathState(19);
                }
                break;
            case 19:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup5);
                    setPathState(20);
                }
                break;
            case 20:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(paths.Pickup5_2);
                    waitTImer.reset();
                    setPathState(21);
                }
                break;
            case 21:
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
        public PathChain ShootPreload, Turn, Pickup1, Gate1,  Shoot1, Pickup2, Shoot2, Pickup3, Pickup5, Pickup5_2, Shoot3, Pickup4, Pickup4_2, Shoot4, Shoot5, Park;

        public Paths(Follower follower) {

            ShootPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shootPreload))
                    .setLinearHeadingInterpolation(Math.toRadians(320), Math.toRadians(270)) // flipped
                    .build();

            Turn = follower.pathBuilder()
                    .addPath(new BezierLine(shootPreload, turn))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(180)) // flipped
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierLine(turn, pickUp1))
                    .setTangentHeadingInterpolation()
                    .build();

            Gate1 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickUp2, gateControl, gate2))
                    .setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp1, shoot1))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270)) // flipped
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot1, control2, pickUp2))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierLine(gate2, shoot2))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(270)) // flipped
                    .build();

            Pickup3 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot2, control3, pickup3))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot3 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickup3, shoot3Con1, shoot3Con2, shoot3))
                    .setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            Pickup4 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot3, pickup4Con1, pickup4Con2, pickup4))
                    .setTangentHeadingInterpolation()
                    .build();

            Pickup4_2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup4, pickup4_2))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(260)) // flipped
                    .build();

            Shoot4 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickup4_2, shoot4Con1, shoot4Con2, shoot4))
                    .setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            Pickup5 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot3, pickup4Con1, pickup4Con2, pickup5))
                    .setTangentHeadingInterpolation()
                    .build();

            Pickup5_2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickup5, pickup4_2))
                    .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(260)) // flipped
                    .build();

            Shoot5 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickup4_2, shoot4Con1, shoot4Con2, shoot4))
                    .setTangentHeadingInterpolation()
                    .build();

            Park = follower.pathBuilder()
                    .addPath(new BezierLine(shoot4, park))
                    .setConstantHeadingInterpolation(Math.toRadians(270)) // unchanged
                    .build();
        }
    }

    @Override
    public void stop() {
        Robot.robotPose = mRobot.mDrive.getPose();
    }
}