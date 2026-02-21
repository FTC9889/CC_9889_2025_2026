package com.team9889.ftc2025.Opmode.Auto.Used;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.team9889.ftc2025.Subsystem.Robot;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Close Auto", group = "Auto")
public class Close extends OpMode {

    private Robot robot;
    private Follower follower;
    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    // Poses defined at the top
    private final Pose startPose = new Pose(25.696, 129.149, Math.toRadians(-37));
    private final Pose shootPreload = new Pose(56.000, 103.000);
    private final Pose pickUp1 = new Pose(56.000, 80.500);
    private final Pose control1 = new Pose(15.966, 83.832);
    private final Pose shoot1 = new Pose(60.000, 73.000);
    private final Pose pickUp2 = new Pose(60.346, 58.680);
    private final Pose control2 = new Pose(8.627, 58.701);
    private final Pose shoot2 = new Pose(60.080, 72.924);

    @Override
    public void init() {
        robot = new Robot();
        robot.init(hardwareMap);

        follower = Constants.createFollower(hardwareMap);
        pathTimer = new Timer();

        paths = new Paths(follower); // Pass follower to Paths constructor
        follower.setStartingPose(startPose);
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
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(paths.ShootPreload);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Pickup1);
                    setPathState(2);
                }
                break;
            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Shoot1);
                    setPathState(3);
                }
                break;
            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Pickup2);
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(paths.Shoot2);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()) {
                    setPathState(-1); // Done
                }
                break;
        }
    }

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public class Paths {
        public PathChain ShootPreload, Pickup1, Shoot1, Pickup2, Shoot2;

        public Paths(Follower follower) {

            ShootPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shootPreload))
                    .setLinearHeadingInterpolation(Math.toRadians(-37), Math.toRadians(270))
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierCurve(shootPreload, control1, pickUp1))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp1, shoot1))
                    .setLinearHeadingInterpolation(Math.toRadians(175), Math.toRadians(270))
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot1, control2, pickUp2))
                    .setTangentHeadingInterpolation()
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp2, shoot2))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
                    .build();
        }
    }

    @Override
    public void stop() {
        Robot.robotPose = follower.getPose();
    }
}