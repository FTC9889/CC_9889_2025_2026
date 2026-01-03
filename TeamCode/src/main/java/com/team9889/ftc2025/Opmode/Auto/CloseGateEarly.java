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
@Autonomous
public class CloseGateEarly extends OpMode {

    Robot mRobot = new Robot();
    Paths paths;

    private Timer pathTimer, opmodeTimer;


    private int pathState = 0;

    /* ---------------- POSES ---------------- */

    /* ---------------- POSES ---------------- */

    public final Pose startPose        = new Pose(56.000, 135.5);
    public final Pose shootPose        = new Pose(56.000, 96.000);

    public final Pose midControlPose   = new Pose(56.501, 83.333);
    public final Pose stackPose        = new Pose(14.800, 84.000);

    public final Pose path7Pose1       = new Pose(36.244, 84.005);
    public final Pose path7Pose2       = new Pose(15.147, 70.484);

    public final Pose returnPose       = new Pose(49.200, 84.000);

    public final Pose path4Pose1       = new Pose(47.533, 52.799);
    public final Pose intakePose       = new Pose(8.492, 59.826);

    public final Pose path6Pose1       = new Pose(48.200, 53.562);

    public final Pose path7Pose3       = new Pose(47.706, 70.776);
    public final Pose path7Pose4       = new Pose(27.000, 70.000);


    /* ---------------- PATH CHAINS ---------------- */

    public PathChain Drive2ShootPos, Pickup1, ShootPos1, Pickup2, OpenGate, ShootPos2, Park;

    /* ---------------- BUILD PATHS ---------------- */

    public void buildPaths() {
        Drive2ShootPos = mRobot.mFollower.pathBuilder().addPath(
                        new BezierLine(
                                startPose,
                                shootPose
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(270))
                .build();

        Pickup1 = mRobot.mFollower.pathBuilder().addPath(
                        new BezierCurve(
                                shootPose,
                                midControlPose,
                                stackPose
                        )
                ).setTangentHeadingInterpolation()
                .build();

        OpenGate = mRobot.mFollower.pathBuilder().addPath(
                        new BezierCurve(
                                stackPose,
                                path7Pose1,
                                path7Pose2
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
                .setReversed()
                .build();

        ShootPos1 = mRobot.mFollower.pathBuilder().addPath(
                        new BezierLine(
                                path7Pose2,
                                returnPose
                        )
                ).setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(180))
                .setReversed()
                .build();

        Pickup2 = mRobot.mFollower.pathBuilder().addPath(
                        new BezierCurve(
                                returnPose,
                                path4Pose1,
                                intakePose
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        ShootPos2 = mRobot.mFollower.pathBuilder().addPath(
                        new BezierCurve(
                                intakePose,
                                path6Pose1,
                                returnPose
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Park = mRobot.mFollower.pathBuilder().addPath(
                        new BezierCurve(
                                returnPose,
                                path7Pose3,
                                path7Pose4
                        )
                ).setConstantHeadingInterpolation(Math.toRadians(180))
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
                // Auto complete
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
        // Everything auto-disables
    }
}

