package com.team9889.ftc2025.Opmode.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.team9889.ftc2025.Opmode.Auto.Unused.LeagueMeet.BlueClose;
import com.team9889.ftc2025.Subsystem.Drive;
import com.team9889.ftc2025.Subsystem.Intake;
import com.team9889.ftc2025.Subsystem.Robot;
import com.team9889.ftc2025.Subsystem.Shooter.ShooterStateMachine;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;


@TeleOp
public class Teleop extends OpMode {
    Robot mRobot = new Robot();


    FtcDashboard dashboard = FtcDashboard.getInstance();

//    private boolean automatedDrive = false;
//    private Supplier<PathChain> pathChain;
//
//    ElapsedTime shoot = new ElapsedTime();
//    ElapsedTime shootWait = new ElapsedTime();
//
//    private Pose AparkPose;
//    private Pose AcurretPose;
//    private double Aheading;
//
//    private final Pose RedPark    = new Pose(41, 33.5, Math.toRadians(180));
//    private final Pose BluePark   = new Pose(103, 33.5, Math.toRadians(0));


    @Override
    public void init() {
        mRobot.init(hardwareMap);

        mRobot.mShooter.AutoTurretPredict = false;

        mRobot.mDrive.setStartingPose(Drive.robotPose);

        telemetry.setMsTransmissionInterval(11);

        Drive.red = true;

        if (Drive.red) {
            mRobot.mShooter.limelight.pipelineSwitch(0);
//            AparkPose = RedPark;
        } else {
            mRobot.mShooter.limelight.pipelineSwitch(1);
//            AparkPose = BluePark;
        }

        mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
        mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
        mRobot.update();
    }


    @Override
    public void start() {
        mRobot.mShooter.limelight.start();
        mRobot.mDrive.startTeleopDrive();
    }

    boolean toggle = true;
    boolean result = false;
    ElapsedTime poseResetTimer = new ElapsedTime();
    boolean waitingToResetPose = false;

    double x;
    double y;
    double heading;

    @Override
    public void loop() {
        LLResult Lresult = mRobot.mShooter.limelight.getLatestResult();
        if (Lresult != null && Lresult.isValid() && Lresult.getStaleness() < 100) {

            Pose3D botpose = Lresult.getBotpose();

            x = 144 - ((-botpose.getPosition().y * 39.37) + 72);
            y = (-botpose.getPosition().x * 39.37) + 72;
            heading = botpose.getOrientation().getYaw(AngleUnit.DEGREES) + 90;
            heading = MathFunctions.normalizeAngleSigned(Math.toRadians(heading));

            if (x != 72 && y != 72) {

                // Trigger rumble and start timer
                if (!waitingToResetPose) {
                    gamepad2.rumble(100);
                    poseResetTimer.reset();
                    waitingToResetPose = true;
                }

                // After 500 ms automatically reset pose
                if (waitingToResetPose && poseResetTimer.milliseconds() > 500) {
                    mRobot.mDrive.setPose(new Pose(x, y, heading));
                    waitingToResetPose = false;
                }
            }

        } else {
            waitingToResetPose = false;
        }

            if (gamepad2.back && gamepad2.b) {
                Drive.red = true;
            } else if (gamepad2.back && gamepad2.x) {
                Drive.red = false;
            }

            if (gamepad1.left_trigger > 0.1 || (gamepad2.right_trigger > 0.1 && gamepad2.left_trigger > 0.1)) {
                mRobot.mDrive.setTeleOpDrive(
                        -gamepad1.left_stick_y * 0.15,
                        -gamepad1.left_stick_x  * 0.25,
                        -gamepad1.right_stick_x  * 0.15,
                        true
                );
            } else {
                mRobot.mDrive.setTeleOpDrive(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x,
                        true
                );
            }

            if (gamepad1.left_bumper && !toggle) {
                result = !result;
                toggle = true;
            } else if (!gamepad1.left_bumper && toggle) {
                toggle = false;
            }

            if (gamepad1.right_trigger > 0.1 && mRobot.mShooter.canShoot) {
                mRobot.mIntake.requestState(Intake.TopLevelState.SHOOT);
                result = true;
            } else if (gamepad2.y) {
                mRobot.mIntake.requestState(Intake.TopLevelState.OUTTAKE);
                result = false;
            } else {
                if (result)
                    mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
                else
                    mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
            }


            if (gamepad2.right_bumper) {
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.READY);
            } else if (gamepad2.left_bumper) {
                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
                mRobot.mIntake.requestState(Intake.TopLevelState.INTAKE);
            }

            if (mRobot.mIntake.getBallEvent() && mRobot.mIntake.getCurrentIntakeState() == Intake.IntakeState.ON && mRobot.mIntake.getCurrentGateState() == Intake.GateState.CLOSED) {
                gamepad1.rumble(500);
                mRobot.mIntake.requestState(Intake.TopLevelState.KEEP);
            }

            if (mRobot.mShooter.getShooterReadyEvent()) {
                gamepad1.rumble(500);
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
            telemetry.addData("x", String.valueOf(x));
            telemetry.addData("y", String.valueOf(y));
            telemetry.addData("yaw", String.valueOf(Math.toDegrees(heading)));

            telemetry.addData("red", Drive.red);
            telemetry.addLine("----------------*----------------");
//
//        telemetry.addData("IntakeRequestedState", mRobot.mIntake.getRequestedIntakeState());
//        telemetry.addData("IntakeCurrentState", mRobot.mIntake.getCurrentIntakeState());
//        telemetry.addLine("---------------------------------");
//
//        telemetry.addData("GateRequestedState", mRobot.mIntake.getRequestedGateState());
//        telemetry.addData("GateCurrentState", mRobot.mIntake.getCurrentGateState());
//        telemetry.addLine("-*--*--*--*--*--*--*--*--*--*--*-");
//
//        telemetry.addData("ShooterRequestedState", mRobot.mShooter.getRequestedShooterState());
//        telemetry.addData("ShooterCurrentState", mRobot.mShooter.getCurrentShooterState());
//        telemetry.addLine("---------------------------------");
//
//        telemetry.addData("HoodRequestedState", mRobot.mShooter.getRequestedHoodState());
//        telemetry.addData("HoodCurrentState", mRobot.mShooter.getCurrentHoodState());
//        telemetry.addLine("---------------------------------");
//
//        telemetry.addData("TurretRequestedState", mRobot.mShooter.getRequestedTurretState());
//        telemetry.addData("TurretCurrentState", mRobot.mShooter.getCurrentTurretState());
//
//        telemetry.addLine("********************************");
//        telemetry.addData("Shooter Velocity", mRobot.mShooter.getShooterVelocity());

//        telemetry.addData("robotPose", mRobot.mDrive.getPose());
        telemetry.addData("intakeEnergy", mRobot.mIntake.intakeL.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("intakeFrontEnergy", mRobot.mIntake.intakeR.getCurrent(CurrentUnit.MILLIAMPS));

        telemetry.addData("DistanceFromGoal", mRobot.mShooter.getDistanceFromGoal(mRobot.mDrive.getPose()));

            drawLimelightPose(x, y, heading, mRobot.mDrive.getPose());

//            if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
//                mRobot.mDrive.followPath(pathChain.get());
//                AcurretPose = mRobot.mDrive.getPose();
//                automatedDrive = true;
//            }
//            //Stop automated following if the follower is done
//            if (automatedDrive && (gamepad1.bWasPressed() || !mRobot.mDrive.isBusy())) {
//                mRobot.mDrive.startTeleopDrive();
//                automatedDrive = false;
//            }
//        } else {
//            autonomousPathUpdate();
//            setPathState(0);


        telemetry.update();
        mRobot.update();
    }

    public void drawLimelightPose(double limelightXm, double limelightYm, double headingRad, Pose pose) {

        double limelightX = limelightXm * 39.37;
        double limelightY = limelightYm * 39.37;

        double limelightHeading = Math.toRadians(headingRad);

        TelemetryPacket packet = new TelemetryPacket();
        Canvas c = packet.fieldOverlay();

        // Draw robot body (18in diameter assumed)
        c.setStroke("lime")
                .setStrokeWidth(2)
                .strokeCircle(limelightX, limelightY, 9);

        // Draw heading line
        double lineLength = 9;
        double x2 = limelightX + lineLength * Math.cos(limelightHeading);
        double y2 = limelightY + lineLength * Math.sin(limelightHeading);

        c.strokeLine(limelightX, limelightY, x2, y2);

        c.setStroke("black")
                .setStrokeWidth(2)
                .strokeCircle(pose.getX(), pose.getY(), 9);
        x2 = pose.getX() + lineLength * Math.cos(pose.getHeading());
        y2 = pose.getY() + lineLength * Math.sin(pose.getHeading());

        c.strokeLine(pose.getX(), pose.getY(), x2, y2);

//        // Optional label
//        c.fillText("Limelight",
//                limelightX + 3,
//                limelightY + 3,
//                "10px Arial",
//                0,
//                false);

        dashboard.sendTelemetryPacket(packet);
    }

//    private void autonomousPathUpdate() {
//        switch (pathState) {
//            case 0:
//                mRobot.mDrive.followPath(paths.Park);
//                mRobot.mShooter.requestState(ShooterStateMachine.TopLevelState.DEFAULT);
//                mRobot.mIntake.requestState(Intake.TopLevelState.OFF);
//                setPathState(1);
//                break;
//            case 1:
//                if (!mRobot.mDrive.isBusy()) {
//                    automatedDrive = true;
//                    setPathState(-1);
//                }
//                break;
//        }
//    }
//
//    private void setPathState(int state) {
//        pathState = state;
//        pathTimer.resetTimer();
//    }
//
//    public class Paths {
//        public PathChain Park;
//
//        public Paths(Follower follower) {
//
//            Park = follower.pathBuilder()
//                    .addPath(new BezierLine(AcurretPose, AparkPose))
//                    .setLinearHeadingInterpolation(Aheading, Math.toRadians(AparkPose.getHeading()))
//                    .build();
//        }
//    }
}