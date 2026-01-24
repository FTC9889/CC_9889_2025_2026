package com.team9889.ftc2025.Opmode.Auto;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;

//@Autonomous(name = "BlueFar", group = "Far")
//@Configurable // Panels
public class FarAutoBlue extends OpMode {

    Robot mRobot = new Robot();
    private Timer pathTimer;
    private int pathState = 0;
    ElapsedTime shootTimer = new ElapsedTime();
    ElapsedTime waitTime = new ElapsedTime();
    boolean change = false;
    private Paths paths; // Paths defined in the Paths class

    @Override
    public void init() {
        mRobot.init(hardwareMap);

        mRobot.mIntake.setRequestedGateState(Intake.GateState.CLOSED);
        mRobot.update();
    }

    @Override
    public void loop() {
        mRobot.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", mRobot.mDrive.getPose().getX());
        telemetry.addData("Y", mRobot.mDrive.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(mRobot.mDrive.getPose().getHeading()));
        telemetry.update();

        mRobot.update();
    }


    public static class Paths {
        public PathChain DrivetoShoot;
        public PathChain PickupOne;
        public PathChain PickeupTwo;
        public PathChain DriveBack;
        public PathChain ShootOne;
        public PathChain Park;

        public Paths(Follower follower) {
            DrivetoShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(88.000, 9.000),

                                    new Pose(88.000, 15.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(-110))

                    .build();

            PickupOne = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(88.000, 15.000),

                                    new Pose(88.000, 34.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-110), Math.toRadians(0))

                    .build();

            PickeupTwo = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(88.000, 34.500),

                                    new Pose(135.500, 34.500)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            DriveBack = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(135.500, 34.500),

                                    new Pose(88.000, 34.500)
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            ShootOne = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(88.000, 34.500),

                                    new Pose(88.000, 15.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-110))

                    .build();

            Park = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(88.000, 15.000),

                                    new Pose(88.000, 35.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-110), Math.toRadians(-90))

                    .build();
        }
    }


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to Shoot
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.DrivetoShoot, true);
                } else if (mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    setPathState(1);
                }
                break;

            case 1: // Pickup One
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.PickupOne, true);
                } else if (mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    setPathState(2);
                }
                break;

            case 2: // Pickup Two
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.PickeupTwo, true);
                } else if (mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    setPathState(3);
                }
                break;

            case 3: // Drive Back
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.DriveBack, true);
                } else if (mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    setPathState(4);
                }
                break;

            case 4: // Shoot One
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.ShootOne, true);
                } else if (mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    setPathState(5);
                }
                break;

            case 5: // Park
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Park, true);
                } else if (mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    // End of autonomous
                    setPathState(6); // optional: or keep at 5
                }
                break;

            default:
                // Do nothing; autonomous finished
                break;
        }
    }

    private void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
    }

    @Override
    public void start() {
        setPathState(0);
    }

    @Override
    public void stop() {
        Robot.robotPose = mRobot.mDrive.getPose();
    }
}