package com.team9889.ftc2025.Opmode.Auto;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "BLUE", group = "CloseAutos")
public class BlueTest extends OpMode {

    Robot mRobot = new Robot();
    private Timer pathTimer;
    private int pathState = 0;
    ElapsedTime shootTimer = new ElapsedTime();
    ElapsedTime waitTime = new ElapsedTime();
    boolean change = false;

    /* ---------------- POSES ---------------- */
    public final Pose startPose    = new Pose(25, 128, Math.toRadians(-37)); // mirrored X
    public final Pose shootPose    = new Pose(64, 78);
    public final Pose smallMove    = new Pose(54.000, 84.000);
    public final Pose stackPose    = new Pose(17.5, 84.000);
    public final Pose curvePose1   = new Pose(61, 58.094);
    public final Pose curvePose2   = new Pose(9.500, 56.000);

    /* ---------------- PATH CHAINS ---------------- */
    public PathChain ShootPreload, IntakeOne, TurnOne, DriveBackOne, TurnToShoot, IntakeTwo, DriveToShootLast, Park;

    /* ---------------- BUILD PATHS ---------------- */
    public void buildPaths() {
        ShootPreload = mRobot.mDrive.pathBuilder().addPath(
                        new BezierLine(startPose, shootPose)
                ).setLinearHeadingInterpolation(Math.toRadians(-37), Math.toRadians(-44))
                .build();

        IntakeOne = mRobot.mDrive.pathBuilder().addPath(
                        new BezierLine(shootPose, smallMove)
                ).setLinearHeadingInterpolation(Math.toRadians(-44), Math.toRadians(180))
                .build();

        TurnOne = mRobot.mDrive.pathBuilder().addPath(
                        new BezierLine(smallMove, stackPose)
                ).setTangentHeadingInterpolation()
                .build();

        DriveBackOne = mRobot.mDrive.pathBuilder().addPath(
                        new BezierLine(stackPose, shootPose)
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-44))
                .build();

        TurnToShoot = mRobot.mDrive.pathBuilder().addPath(
                        new BezierLine(shootPose, smallMove)
                ).setLinearHeadingInterpolation(Math.toRadians(-44), Math.toRadians(180))
                .build();

        IntakeTwo = mRobot.mDrive.pathBuilder().addPath(
                        new BezierCurve(smallMove, curvePose1, curvePose2)
                ).setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        DriveToShootLast = mRobot.mDrive.pathBuilder().addPath(
                        new BezierCurve(curvePose2, curvePose1, shootPose)
                ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-44))
                .build();

        Park = mRobot.mDrive.pathBuilder().addPath(
                        new BezierLine(shootPose, new Pose(49, 65))
                ).setLinearHeadingInterpolation(Math.toRadians(-44), Math.toRadians(90))
                .build();
    }

    /* ---------------- PATH UPDATES ---------------- */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.setFrontIntakePower(1);
//                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mDrive.followPath(ShootPreload, true);
                    setPathState(11);
                    shootTimer.reset();
                }
                break;
            case 11:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY_CLOSE);
                    if (shootTimer.milliseconds() > 3000 && !mRobot.mDrive.isBusy()) {
                        setPathState(12);
                        change = false;
                        mRobot.mIntake.setFrontIntakePower(0);
//                        mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                    }
                }
                break;
            case 12:
                mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                shootTimer.reset();
                waitTime.reset();
                setPathState(13);
                break;
            case 13:
                if (shootTimer.milliseconds() > 200 && !change) {
                    mRobot.mIntake.RequestedGateState = Intake.GateState.CLOSED;
                    change = true;
                    shootTimer.reset();
                }
                if (shootTimer.milliseconds() > 500 && change) {
                    mRobot.mIntake.RequestedGateState = Intake.GateState.OPEN;
                    change = false;
                    shootTimer.reset();
                }

                if (waitTime.milliseconds() > 3000) {
                    mRobot.mIntake.setRequestedGateState(Intake.GateState.CLOSED);
                    setPathState(1);
                    change = false;
                }
                break;
            case 1:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(TurnOne, true);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(2);
                }
                break;
            case 2:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(IntakeOne, true);
                    setPathState(3);
                }
                break;
            case 3:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.setFrontIntakePower(1);
//                    mRobot.mIntake.requestState(Intake.TopLevelState.KEEP);
                    mRobot.mDrive.followPath(DriveBackOne, true);
                    setPathState(1001);
                }
                break;
            case 1001:
                if (!mRobot.mDrive.isBusy()) {
                    shootTimer.reset();
                    setPathState(111);
                }
                break;
            case 111:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY_CLOSE);
                    if (shootTimer.milliseconds() > 3000) {
                        setPathState(101);
                        change = false;
                    }
                }
                break;
            case 101:
                mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                shootTimer.reset();
                waitTime.reset();
                setPathState(102);
                break;
            case 102:
                if (shootTimer.milliseconds() > 200 && !change) {
                    mRobot.mIntake.RequestedGateState = Intake.GateState.CLOSED;
                    change = true;
                    shootTimer.reset();
                }
                if (shootTimer.milliseconds() > 500 && change) {
                    mRobot.mIntake.RequestedGateState = Intake.GateState.OPEN;
                    change = false;
                    shootTimer.reset();
                }

                if (waitTime.milliseconds() > 3000) {
                    mRobot.mIntake.setRequestedGateState(Intake.GateState.CLOSED);
                    setPathState(31);
                    change = false;
                }
                break;
            case 31:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(TurnToShoot, true);
                    setPathState(32);
                }
                break;

            case 32:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    setPathState(333);
                }
                break;
            case 333:
                if (mRobot.isComplete() && !mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(4);
                }
            case 4:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(IntakeTwo, true);
                    setPathState(5);
                }
                break;
            case 5:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.setFrontIntakePower(1);
                    mRobot.mDrive.followPath(DriveToShootLast, true);
                    shootTimer.reset();
                    setPathState(51);
                }
                break;
            case 51:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY_CLOSE);
                    if (shootTimer.milliseconds() > 3000) {
                        setPathState(52);
                        change = false;
                    }
                }
                break;
            case 52:
                mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                setPathState(503);
                break;
            case 503:
                if (!mRobot.mDrive.isBusy()) {
                    waitTime.reset();
                    shootTimer.reset();
                    setPathState(53);
                }
                break;
            case 53:
                if (shootTimer.milliseconds() > 200 && !change) {
                    mRobot.mIntake.RequestedGateState = Intake.GateState.CLOSED;
                    change = true;
                    shootTimer.reset();
                }
                if (shootTimer.milliseconds() > 500 && change) {
                    mRobot.mIntake.RequestedGateState = Intake.GateState.OPEN;
                    change = false;
                    shootTimer.reset();
                }

                if (waitTime.milliseconds() > 4000) {
                    mRobot.mIntake.setRequestedGateState(Intake.GateState.CLOSED);
                    setPathState(6);
                    change = false;
                }
                break;
            case 6:
                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    setPathState(100);
                }
                break;
            case 100:
                mRobot.mDrive.followPath(Park, true);
                break;
        }
    }

    /* ---------------- HELPERS ---------------- */
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /* ---------------- OP MODE ---------------- */
    @Override
    public void loop() {
        mRobot.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", mRobot.mDrive.getPose().getX());
        telemetry.addData("Y", mRobot.mDrive.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(mRobot.mDrive.getPose().getHeading()));
        telemetry.update();

        // In your OpMode or main loop
        Vector robotVelocity = mRobot.mDrive.getVelocity();
        mRobot.mShooter.update(robotVelocity);

    }

    @Override
    public void init() {
        mRobot.init(hardwareMap);
        mRobot.mDrive = Constants.createFollower(hardwareMap);
        buildPaths();
        mRobot.mDrive.setStartingPose(startPose);
        mRobot.mIntake.setRequestedGateState(Intake.GateState.CLOSED);

        pathTimer = new Timer();

        telemetry.addLine("Initialized BlueTest (flipped)");
        telemetry.update();

        mRobot.update();

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
