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
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

@Autonomous(name = "🟥🔺♻️RedAltClose", group = "Red", preselectTeleOp = "Teleop")
public class RedAltClose extends OpMode {
    Robot mRobot = new Robot();

    private Timer pathTimer;
    private int pathState = 0;
    private Paths paths;

    ElapsedTime shotTimer = new ElapsedTime();
    ElapsedTime matchTimer = new ElapsedTime();
    ElapsedTime pickupTimer = new ElapsedTime();
    ElapsedTime gateTimer = new ElapsedTime();

    Pose currentPose = new Pose();

    /* ---------------- FLIPPED POSES WITH +180° HEADINGS ---------------- */

    private final Pose startPose       = new Pose(118.304, 129.149, Math.toRadians(220));   // 37 + 180
    private final Pose shootPreload    = new Pose(86.000, 78.000);

    private final Pose pickUp1         = new Pose(131.000, 58.500);
    private final Pose Control1        = new Pose(84.000, 59.000);

    private final Pose shoot           = new Pose(86.000, 78.000);

    private final Pose pickUpGatePretend      = new Pose(131, 65.000);

    private final Pose pickUpGate      = new Pose(127.000, 65.000);
    private final Pose gateControl1     = new Pose(86.6, 76.5);
    private final Pose gateControl2     = new Pose(106.67, 64);
    private final Pose pickupGate_2     = new Pose(132.000, 56.000);

    private final Pose gateShootCon1   = new Pose(118.2, 56);
    private final Pose gateShootCon2   = new Pose(92.1, 67.2);

    private final Pose pickUp2         = new Pose(126, 84.5);

    private final Pose shoot2Con1      = new Pose(107.1, 81.7);
    private final Pose shoot2Con2      = new Pose(94, 85.6);

    private final Pose shoot2          = new Pose(90.5, 91.7);



    private final Pose epark        = new Pose(112.000, 70.000);
    private final Pose park        = new Pose(102, 80    );



    @Override
    public void init() {
        Drive.red = true;
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

        currentPose = mRobot.mDrive.getPose();

        mRobot.mDrive.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", mRobot.mDrive.getPose().getX());
        telemetry.addData("Y", mRobot.mDrive.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(mRobot.mDrive.getPose().getHeading()));
//        telemetry.addData("IntakeRequestedState", mRobot.mIntake.getRequestedFrontIntakeState());
//        telemetry.addData("IntakeBackCurrentState", mRobot.mIntake.getCurrentBackIntakeState());
//        telemetry.addData("IntakeFrontCurrentState", mRobot.mIntake.getCurrentFrontIntakeState());
        telemetry.addLine("---------------------------------");

        telemetry.addData("GateRequestedState", mRobot.mIntake.getRequestedGateState());
        telemetry.addData("GateCurrentState", mRobot.mIntake.getCurrentGateState());
        telemetry.addLine("-*--*--*--*--*--*--*--*--*--*--*-");

        telemetry.addData("ShooterRequestedState", mRobot.mShooter.getRequestedShooterState());
        telemetry.addData("ShooterCurrentState", mRobot.mShooter.getCurrentShooterState());
        telemetry.addLine("---------------------------------");

        telemetry.addData("HoodRequestedState", mRobot.mShooter.getRequestedHoodState());
        telemetry.addData("HoodCurrentState", mRobot.mShooter.getCurrentHoodState());
        telemetry.addLine("---------------------------------");

        telemetry.addData("TurretRequestedState", mRobot.mShooter.getRequestedTurretState());
        telemetry.addData("TurretCurrentState", mRobot.mShooter.getCurrentTurretState());

        telemetry.addLine("********************************");
        telemetry.update();
        mRobot.update();
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                matchTimer.reset();
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                mRobot.mShooter.AutoTurretPredictPose = paths.ShootPreload.endPose();
                mRobot.mDrive.followPath(paths.ShootPreload);
                setPathState(1);
                break;
            case 1:
                if (!mRobot.mDrive.isBusy() && mRobot.isComplete()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.AUTO_SHOOT);
                    setPathState(2);
                }
                break;
            case 2:
                if (shotTimer.milliseconds() > 900) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(3);
                }
                break;
            case 3:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup1);
                    setPathState(4);
                }
                break;
            case 4:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Shoot1);
                    setPathState(5);
                }
                break;
            case 5:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.AUTO_SHOOT);
                    setPathState(6);
                }
                break;
            case 6:
                if (shotTimer.milliseconds() > 900) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(7);
                }
                break;
            case 7:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.GatePickUp);
                    setPathState(10001);
                }
                break;
            case 10001:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.PickupGate_2);
                    setPathState(881);
                }
                break;
            case 881:
                if (!mRobot.mDrive.isBusy()) {
                    pickupTimer.reset();
                    setPathState(9);
                }
                break;
            case 9:
                if (!mRobot.mDrive.isBusy() && pickupTimer.milliseconds() > 750) {
                    mRobot.mShooter.AutoTurretPredictPose = paths.GateShoot.endPose();
                    mRobot.mDrive.followPath(paths.GateShoot);
                    shotTimer.reset();
                    setPathState(999);
                }
                break;
            case 999:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.AUTO_SHOOT);
                    setPathState(10);
                }
                break;
            case 10:
                if (shotTimer.milliseconds() > 900) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    if (matchTimer.seconds() > 22) {
                        setPathState(11);
                    } else {
                        setPathState(7);
                    }
                }
                break;
            case 11:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(paths.Pickup2);
                    if (matchTimer.seconds() > 29.5) {
                        setPathState(2000);
                    } else {
                        setPathState(12);
                    }
                }
                break;
            case 12:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    mRobot.mShooter.AutoTurretPredictPose = paths.Shoot2.endPose();
                    mRobot.mDrive.followPath(paths.Shoot2);
                    if (matchTimer.seconds() > 29.5) {
                        setPathState(2000);
                    } else {
                        setPathState(13);
                    }
                }
                break;
            case 13:
                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.AUTO_SHOOT);
                    if (matchTimer.seconds() > 29.5) {
                        setPathState(2000);
                    } else {
                        setPathState(14);
                    }
                }
                break;
            case 14:
                if (shotTimer.milliseconds() > 900) {
                    if (matchTimer.seconds() > 29.5) {
                        setPathState(2000);
                    } else {
                        mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                        mRobot.mDrive.followPath(paths.Park);
                        setPathState(15);
                    }
                }
                break;
            case 15:
                if (!mRobot.mDrive.isBusy()) {
                    setPathState(-10000);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                }
                break;
            case 2000:
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                currentPose = mRobot.mDrive.getPose();
                mRobot.mDrive.breakFollowing();
                setPathState(2001);
                break;
            case 2001:
                mRobot.mDrive.followPath(paths.EPARK);
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                setPathState(2002);
                break;
            case 2002:
                if (!mRobot.mDrive.isBusy()) {
                    setPathState(-2);
                }
        }
    }

    private void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public class Paths {
        public PathChain ShootPreload, GatePickUp, Pickup1, GateShoot,  Shoot1, Pickup2, Shoot2, Park, EPARK, PickupGate_2;

        public Paths(Follower follower) {

            ShootPreload = follower.pathBuilder()
                    .addPath(new BezierLine(startPose, shootPreload))
                    .setLinearHeadingInterpolation(Math.toRadians(220), Math.toRadians(300)) // +180
                    .build();

            Pickup1 = follower.pathBuilder()
                    .addPath(new BezierCurve(shootPreload, Control1, pickUp1))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(0)) // +180
                    .build();

            Shoot1 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUp1, shoot))
                    .setLinearHeadingInterpolation(Math.toRadians(356), Math.toRadians(300))
                    .build();

            GatePickUp = follower.pathBuilder()
                    .addPath(new BezierCurve(shoot, gateControl1, gateControl2, pickUpGate))
                    .setTangentHeadingInterpolation()
                    .build();

            PickupGate_2 = follower.pathBuilder()
                    .addPath(new BezierLine(pickUpGate, pickupGate_2))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(30))
                    .build();

            GateShoot = follower.pathBuilder()
                    .addPath(new BezierCurve(pickUpGatePretend, gateShootCon1, gateShootCon2, shoot))
                    .setLinearHeadingInterpolation(Math.toRadians(-24), Math.toRadians(300))
                    .build();

            Shoot2 = follower.pathBuilder()
                    .addPath(new BezierCurve(pickUp2, shoot2Con1, shoot2Con2, shoot2))
                    .setLinearHeadingInterpolation(Math.toRadians(9), Math.toRadians(300))
                    .build();

            Pickup2 = follower.pathBuilder()
                    .addPath(new BezierLine(shoot, pickUp2))
                    .setTangentHeadingInterpolation()
                    .build();

            Park = follower.pathBuilder()
                    .addPath(new BezierLine(shoot2, park))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(0))
                    .build();

            EPARK = follower.pathBuilder()
                    .addPath(new BezierLine(currentPose, epark))
                    .setLinearHeadingInterpolation(Math.toRadians(currentPose.getHeading()), Math.toRadians(0))
                    .build();
        }
    }

    @Override
    public void stop() {
        Robot.robotPose = mRobot.mDrive.getPose();
    }
}