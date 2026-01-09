package com.team9889.ftc2025.Opmode.Auto;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.team9889.ftc2025.Subsystem.Robot;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.nio.file.Paths;
@Autonomous(name = "CloseLate", group = "CloseAutos", preselectTeleOp = "TeleOperate")
public class CloseLateGate extends OpMode {

    Robot mRobot = new Robot();
    Paths paths;

    private Timer pathTimer, opmodeTimer;


    private int pathState = 0;

    public final Pose startPose        = new Pose(56.000, 135.5);
    public final Pose shootPose        = new Pose(56.000, 96.000);

    public final Pose midControlPose   = new Pose(56.501, 83.333);
    public final Pose stackPose        = new Pose(14.800, 84.000);

    public final Pose returnPose       = new Pose(49.200, 84.000);

    public final Pose curveMidPose1    = new Pose(48.200, 53.800);
    public final Pose intakePose       = new Pose(8.319, 58.999);

    public final Pose adjustPose       = new Pose(18.600, 55.500);
    public final Pose finalIntakePose  = new Pose(15.000, 65.900);

    public final Pose returnMidPose    = new Pose(48.200, 64.908);
    public final Pose finalShootPose   = new Pose(56.500, 83.300);

    public final Pose parkMidPose      = new Pose(47.706, 70.776);
    public final Pose parkPose         = new Pose(27, 70);

    public PathChain Drive2ShootPos, Pickup1, ShootPos1, Pickup2, OpenGate, ShootPos2, Park;


    public void buildPaths() {

        Drive2ShootPos = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setConstantHeadingInterpolation(Math.toRadians(270))
                .build();

        Pickup1 = mRobot.mFollower.pathBuilder()
                .addPath(new BezierCurve(
                        shootPose,
                        midControlPose,
                        stackPose
                ))
                .setTangentHeadingInterpolation()
                .build();

        ShootPos1 = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(
                        stackPose,
                        returnPose
                ))
                .setTangentHeadingInterpolation().setReversed()
                .build();

        Pickup2 = mRobot.mFollower.pathBuilder()
                .addPath(new BezierCurve(
                        returnPose,
                        curveMidPose1,
                        intakePose
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        OpenGate = mRobot.mFollower.pathBuilder()
                .addPath(new BezierCurve(
                        intakePose,
                        adjustPose,
                        finalIntakePose
                ))
                .setLinearHeadingInterpolation(
                        Math.toRadians(-180),
                        Math.toRadians(-1)
                )
                .build();

        ShootPos2 = mRobot.mFollower.pathBuilder()
                .addPath(new BezierCurve(
                        finalIntakePose,
                        returnMidPose,
                        finalShootPose
                ))
                .setLinearHeadingInterpolation(
                        Math.toRadians(-1),
                        Math.toRadians(270)
                )
                .build();

        Park = mRobot.mFollower.pathBuilder()
                .addPath(new BezierCurve(
                        finalShootPose,
                        parkMidPose,
                        parkPose
                ))
                .setConstantHeadingInterpolation(Math.toRadians(270))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                mRobot.mFollower.followPath(Drive2ShootPos, true);
                setPathState(1);
                break;

            case 1:
                if (!mRobot.mFollower.isBusy()) {
                    mRobot.mFollower.followPath(Pickup1, true);
                    setPathState(2);
                }
                break;

            case 2:
                if (!mRobot.mFollower.isBusy()) {
                    mRobot.mFollower.followPath(ShootPos1, true);
                    setPathState(3);
                }
                break;

            case 3:
                if (!mRobot.mFollower.isBusy()) {
                    mRobot.mFollower.followPath(Pickup2, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!mRobot.mFollower.isBusy()) {
                    mRobot.mFollower.followPath(OpenGate, true);
                    setPathState(5);
                }
                break;

            case 5:
                if (!mRobot.mFollower.isBusy()) {
                    mRobot.mFollower.followPath(ShootPos2, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (!mRobot.mFollower.isBusy()) {
                    mRobot.mFollower.followPath(Park, true);
                    setPathState(7);
                }
                break;

            case 7:
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        mRobot.mFollower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", mRobot.mFollower.getPose().getX());
        telemetry.addData("Y", mRobot.mFollower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(mRobot.mFollower.getPose().getHeading()));
        telemetry.update();
    }

    @Override
    public void init() {
        mRobot.init(hardwareMap);

        mRobot.mFollower = Constants.createFollower(hardwareMap);
        buildPaths();
        mRobot.mFollower.setStartingPose(startPose);

        pathTimer = new Timer();
        opmodeTimer = new Timer();

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
    }


    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
        Robot.robotPose = mRobot.mFollower.getPose();
    }
}