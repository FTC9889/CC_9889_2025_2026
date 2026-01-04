package com.team9889.ftc2025.Opmode.Auto; // make sure this aligns with class location

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.team9889.ftc2025.Subsystem.Robot;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "FarThreeRows", group = "FarAutos", preselectTeleOp = "TeleOperate")
public class Far3Rows extends OpMode {
    Robot mRobot = new Robot();

    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(50.000, 7.500, Math.toRadians(180)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(50.000, 15.500, Math.toRadians(180)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
    private final Pose LowIntake1Pose = new Pose(50.000, 7.500, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose LowIntake2Pose = new Pose(8.500, 7.500, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose MidIntakePose = new Pose(8.500, 36.600, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    private final Pose MidControlPoint = new Pose(51.000, 36.000, Math.toRadians(180));
    private final Pose HighIntakePose = new Pose(8.500, 60.500);
    private final Pose HighControlPoint = new Pose(47.000, 60.500);
    private final Pose ParkPose = new Pose(50.000, 30.000, Math.toRadians(180));

    private Path scorePreload;
    private PathChain Drive2ScorePos, LowIntake1, LowIntake2, LowReturn, MidIntake, MidReturn, HighIntake, HighReturn,  Park;

    public void buildPaths() {
        Drive2ScorePos = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        LowIntake1 = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(scorePose, LowIntake1Pose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        LowIntake2 = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(LowIntake1Pose, LowIntake2Pose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        LowReturn = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(LowIntake2Pose, scorePose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        MidIntake = mRobot.mFollower.pathBuilder()
                .addPath(new BezierCurve(scorePose, MidControlPoint, MidIntakePose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        MidReturn = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(MidIntakePose, scorePose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        HighIntake = mRobot.mFollower.pathBuilder().addPath(
                        new BezierCurve(scorePose, HighControlPoint, HighIntakePose)
                ).setConstantHeadingInterpolation(Math.toRadians(180))

                .build();

        HighReturn = mRobot.mFollower.pathBuilder().addPath(
                        new BezierLine(HighIntakePose, scorePose)
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();


        Park = mRobot.mFollower.pathBuilder()
                .addPath(new BezierLine(scorePose, ParkPose))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
        }
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        mRobot.mFollower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", mRobot.mFollower.getPose().getX());
        telemetry.addData("y", mRobot.mFollower.getPose().getY());
        telemetry.addData("heading", mRobot.mFollower.getPose().getHeading());
        telemetry.update();
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        mRobot.init(hardwareMap);

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        mRobot.mFollower = Constants.createFollower(hardwareMap);
        buildPaths();
        mRobot.mFollower.setStartingPose(startPose);

    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /**
     * We do not use this because everything should automatically disable
     **/
    @Override
    public void stop() {
        Robot.robotPose = mRobot.mFollower.getPose();
    }
}