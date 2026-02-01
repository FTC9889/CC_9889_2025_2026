//package com.team9889.ftc2025.Opmode.Auto;
//
//import android.view.WindowInsets;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.BezierCurve;
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.geometry.Pose;
//import com.pedropathing.paths.Path;
//import com.pedropathing.paths.PathChain;
//import com.pedropathing.util.Timer;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.team9889.ftc2025.Subsystem.Intake;
//import com.team9889.ftc2025.Subsystem.Robot;
//import com.team9889.ftc2025.Subsystem.Shooter.Shooter;
//
//
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//@Autonomous(name = "CloseEarly", group = "CloseAutos", preselectTeleOp = "TeleOperate")
//public class CloseGateEarly extends OpMode {
//
//    Robot mRobot = new Robot();
//    Follower mFollower = Constants.createFollower(hardwareMap);
//    Paths paths;
//
//    private Timer pathTimer, opmodeTimer;
//
//
//
//    ElapsedTime shooterTimer = new ElapsedTime();
//
//    boolean done = false;
//    boolean gate = true;
//    int repeat = 0;
//    int scorelist = 0;
//
//    ArrayList scoreList;
//
//
//    private int pathState = 0;
//
//    public final Pose startPose        = new Pose(56.000, 135.5);
//    public final Pose shootPose        = new Pose(56.000, 96.000);
//
//    public final Pose midControlPose   = new Pose(56.501, 83.333);
//    public final Pose stackPose        = new Pose(14.800, 84.000);
//
//    public final Pose path7Pose1       = new Pose(36.244, 84.005);
//    public final Pose path7Pose2       = new Pose(15.147, 70.484);
//
//    public final Pose returnPose       = new Pose(49.200, 84.000);
//
//    public final Pose path4Pose1       = new Pose(47.533, 52.799);
//    public final Pose intakePose       = new Pose(8.492, 59.826);
//
//    public final Pose path6Pose1       = new Pose(48.200, 53.562);
//
//    public final Pose path7Pose3       = new Pose(47.706, 70.776);
//    public final Pose path7Pose4       = new Pose(27.000, 70.000);
//
//
//    /* ---------------- PATH CHAINS ---------------- */
//
//    public PathChain Drive2ShootPos, Pickup1, ShootPos1, Pickup2, OpenGate, ShootPos2, Park;
//
//    /* ---------------- BUILD PATHS ---------------- */
//
//    public void buildPaths() {
//        Drive2ShootPos = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierLine(
//                                startPose,
//                                shootPose
//                        )
//                ).setConstantHeadingInterpolation(Math.toRadians(270))
//                .build();
//
//        Pickup1 = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierCurve(
//                                shootPose,
//                                midControlPose,
//                                stackPose
//                        )
//                ).setTangentHeadingInterpolation()
//                .build();
//
//        OpenGate = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierCurve(
//                                stackPose,
//                                path7Pose1,
//                                path7Pose2
//                        )
//                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
//                .setReversed()
//                .build();
//
//        ShootPos1 = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierLine(
//                                path7Pose2,
//                                returnPose
//                        )
//                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(180))
//                .setReversed()
//                .build();
//
//        Pickup2 = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierCurve(
//                                returnPose,
//                                path4Pose1,
//                                intakePose
//                        )
//                ).setConstantHeadingInterpolation(Math.toRadians(180))
//                .build();
//
//        ShootPos2 = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierCurve(
//                                intakePose,
//                                path6Pose1,
//                                returnPose
//                        )
//                ).setConstantHeadingInterpolation(Math.toRadians(180))
//                .build();
//
//        Park = mRobot.mDrive.pathBuilder().addPath(
//                        new BezierCurve(
//                                returnPose,
//                                path7Pose3,
//                                path7Pose4
//                        )
//                ).setConstantHeadingInterpolation(Math.toRadians(180))
//                .build();
//    }
//
//    public void autonomousPathUpdate() {
//        switch (pathState) {
//
//            case 0:
//                if (!mRobot.mDrive.isBusy() && mRobot.mShooter.isComplete()) {
//                    mRobot.mDrive.followPath((Path) scoreList.get(scorelist), true);
//                    mRobot.mShooter.requestState(Shooter.TopLevelState.READY_CLOSE);
//                    scorelist += 1;
//                    setPathState(1);
//                }
//                break;
//            case 1:
//                if (mRobot.mIntake.isComplete()) {
//                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
//                    shooterTimer.reset();
//                    done = false;
//                    setPathState(11);
//                }
//                break;
//            case 11:
//                if (mRobot.mShooter.isComplete() && mRobot.mIntake.isComplete()){
//                    if (shooterTimer.milliseconds() > 700 && !done) {
//                        mRobot.mIntake.RequestedGateState = Intake.GateState.CLOSED;
//                        done = true;
//                        shooterTimer.reset();
//                    }
//                    if (shooterTimer.milliseconds() > 200 && done) {
//                        mRobot.mIntake.RequestedGateState = Intake.GateState.CLOSED;
//                        done = false;
//                        shooterTimer.reset();
//                        repeat += 1;
//                        if (repeat < 3) {
//                            setPathState(11);
//                        } else {
//                            setPathState(12);
//                        }
//                    }
//                }
//                break;
//            case 12:
//                if (mRobot.mShooter.isComplete() && mRobot.mIntake.isComplete()) {
//                    if (scorelist < 3) {
//                        setPathState(21);
//                    } else {
//                        setPathState(5);
//                    }
//                }
//                break;
//            case 21:
//                if (!mRobot.mDrive.isBusy()) {
//                    mRobot.mShooter.requestState(Shooter.TopLevelState.DEFAULT);
//                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
//                    mRobot.mDrive.followPath(Pickup1, true);
//                    setPathState(3);
//                }
//                break;
//            case 3:
//                if (gate) {
//                    gate = false;
//                    setPathState(4);
//                } else {
//                    setPathState(0);
//                }
//                break;
//            case 4:
//                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()){
//                    mRobot.mDrive.followPath(OpenGate, true);
//                    setPathState(0);
//                }
//                break;
//            case 5:
//                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()){
//                    mRobot.mDrive.followPath(Park, true);
//                }
//                break;
//        }
//    }
//
//    public void setPathState(int pState) {
//        pathState = pState;
//        pathTimer.resetTimer();
//    }
//
//    @Override
//    public void loop() {
//        mRobot.update();
//        autonomousPathUpdate();
//
//        telemetry.addData("Path State", pathState);
//        telemetry.addData("X", mRobot.mDrive.getPose().getX());
//        telemetry.addData("Y", mRobot.mDrive.getPose().getY());
//        telemetry.addData("Heading", Math.toDegrees(mRobot.mDrive.getPose().getHeading()));
//        telemetry.update();
//    }
//
//    @Override
//    public void init() {
//        mRobot.init(hardwareMap);
//
//        mRobot.mDrive = Constants.createFollower(hardwareMap);
//        buildPaths();
//        mRobot.mDrive.setStartingPose(startPose);
//
//        pathTimer = new Timer();
//        opmodeTimer = new Timer();
//
//        telemetry.addLine("Initialized");
//        telemetry.update();
//
//        scoreList = new ArrayList<>(
//                Arrays.asList(Drive2ShootPos, ShootPos1, ShootPos2)
//        );
//    }
//
//    @Override
//    public void init_loop() {
//        telemetry.addData("Status", "Waiting for start");
//        telemetry.update();
//    }
//
//    @Override
//    public void start() {
//        opmodeTimer.resetTimer();
//        setPathState(0);
//    }
//
//    @Override
//    public void stop() {
//        Robot.robotPose = mRobot.mDrive.getPose();
//    }
//}
//
