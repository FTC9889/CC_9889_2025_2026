package com.team9889.ftc2025.Opmode.Auto.Used.Far;

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

@Autonomous(name = "🟦⬇️♻️️Blue2Alt2Far", group = "Red", preselectTeleOp = "Teleop")

public class Blue2Alt2Far extends OpMode {
    Robot mRobot = new Robot();

    private Timer pathTimer;
    private int pathState;

    private PathChain ShootPreload, Pickup1Row3, Shoot1, Pickup2Row4_1, Pickup2Row4_2, Shoot2, Pickup3Prediction1, Shoot3, Pickup4Prediction2, Shoot4, Park;

    ElapsedTime shotTimer = new ElapsedTime();
    ElapsedTime waitTimer = new ElapsedTime();

    /* ---------------- POSES ---------------- */

    private final Pose startPose = new Pose(88.000, 8.230, Math.toRadians(270));

    private final Pose preloadShoot = new Pose(88.000, 15.000);

    private final Pose curveControl = new Pose(100.000, 40.000);
    private final Pose pickup1 = new Pose(132.000, 36.000);

    private final Pose shoot1 = new Pose(88.000, 15.000);

    private final Pose pickup2 = new Pose(133.500, 30.000);
    private final Pose pickup2Down = new Pose(133.500, 8.500);

    private final Pose shoot2 = new Pose(88.000, 15.000);

    private final Pose pickup3 = new Pose(132.000, 8.500);
    private final Pose shoot3 = new Pose(88.000, 15.000);

    private final Pose pickup4 = new Pose(132.000, 8.500);
    private final Pose shoot4 = new Pose(88.000, 15.000);

    private final Pose park = new Pose(105.000, 15.000);

    @Override
    public void init() {
        Drive.red = true;

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

        ShootPreload = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(startPose, preloadShoot))
                .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(205)) // 335 -> 205
                .build();

        Pickup1Row3 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierCurve(preloadShoot, curveControl, pickup1))
                .setLinearHeadingInterpolation(Math.toRadians(205), Math.toRadians(180)) // 335 -> 205
                .build();

        Shoot1 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup1, shoot1))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(205)) // 335 -> 205
                .build();

        Pickup2Row4_1 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot1, pickup2))
                .setLinearHeadingInterpolation(Math.toRadians(205), Math.toRadians(270)) // 335 -> 205
                .build();

        Pickup2Row4_2 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup2, pickup2Down))
                .setTangentHeadingInterpolation()
                .build();

        Shoot2 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup2Down, shoot2))
                .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(205)) // 335 -> 205
                .build();

        Pickup3Prediction1 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot2, pickup3))
                .setLinearHeadingInterpolation(Math.toRadians(205), Math.toRadians(180)) // 335 -> 205
                .build();

        Shoot3 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup3, shoot3))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(205)) // 335 -> 205
                .build();

        Pickup4Prediction2 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot3, pickup4))
                .setLinearHeadingInterpolation(Math.toRadians(205), Math.toRadians(180)) // 335 -> 205
                .build();

        Shoot4 = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(pickup4, shoot4))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(205)) // 335 -> 205
                .build();

        Park = mRobot.mDrive.pathBuilder()
                .addPath(new BezierLine(shoot4, park))
                .setLinearHeadingInterpolation(Math.toRadians(205), Math.toRadians(180)) // 335 -> 205
                .build();
    }

    /* ---------------- STATE MACHINE ---------------- */


    public void autonomousPathUpdate() {

        switch (pathState) {

            case 0:
                mRobot.mDrive.followPath(ShootPreload);
//                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.);

                setPathState(1);

                break;

            case 1:

                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);

                    setPathState(111);
                }

                break;

            case 111:

                if (shotTimer.milliseconds() > 1000) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    setPathState(2);
                }
                break;

            case 2:

                if (waitTimer.milliseconds() > 1200) {
                    mRobot.mDrive.followPath(Pickup1Row3);
                    setPathState(3);
                }

                break;

            case 3:

                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mDrive.followPath(Shoot1);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    setPathState(4);
                }

                break;

            case 4:

                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    shotTimer.reset();
                    setPathState(5);
                }

                break;

            case 5:

                if (shotTimer.milliseconds() > 1000) {

                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mDrive.followPath(Pickup2Row4_1);

                    setPathState(6);
                }

                break;

            case 6:

                if (!mRobot.mDrive.isBusy()) {

                    mRobot.mDrive.followPath(Pickup2Row4_2);

                    setPathState(7);
                }

                break;

            case 7:

                if (!mRobot.mDrive.isBusy()) {
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
                    mRobot.mDrive.followPath(Shoot2);

                    setPathState(71);
                }

                break;

            case 71:

                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(72);
                }

                break;

            case 72:

                if (shotTimer.milliseconds() > 1000) {

                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mDrive.followPath(Pickup3Prediction1);

                    setPathState(9);
                }

                break;

            case 9:

                if (!mRobot.mDrive.isBusy()) {

                    mRobot.mDrive.followPath(Shoot3);

                    setPathState(91);
                }

                break;

            case 91:

                if (!mRobot.mDrive.isBusy()) {
                    shotTimer.reset();
                    mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                    setPathState(92);
                }

                break;

            case 92:

                if (shotTimer.milliseconds() > 1000) {

                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mDrive.followPath(Pickup4Prediction2);

                    setPathState(11);
                }

                break;

            case 11:

                if (!mRobot.mDrive.isBusy()) {

                    mRobot.mDrive.followPath(Shoot4);

                    setPathState(1111);
                }

                break;

            case 1111:

                if (shotTimer.milliseconds() > 1000) {

                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                    mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                    mRobot.mDrive.followPath(Park);

                    setPathState(12);
                }

                break;

            case 12:

                if (!mRobot.mDrive.isBusy()) {
                    setPathState(-1);
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