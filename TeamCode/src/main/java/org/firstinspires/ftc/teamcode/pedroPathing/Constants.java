package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(9.072)
            .forwardZeroPowerAcceleration(-32.840565296002275)
            .lateralZeroPowerAcceleration(-57.58201083341013)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)

            .translationalPIDFCoefficients(new PIDFCoefficients(0.1, 0, 0, 0))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.3, 0, 0.015, 0.01))

            .headingPIDFCoefficients(new PIDFCoefficients(0.8, 0, 0, 0.01))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.01, 0.03))

            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.04, 0.000001, 0.0001, 0.6,  0.0004))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.03, 0, 0.0000000000000002,  0.4, 0.0004))
            ;

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }



    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("rightfront")
            .rightRearMotorName("rightback")
            .leftRearMotorName("leftback")
            .leftFrontMotorName("leftfront")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .useBrakeModeInTeleOp(true)
            .xVelocity(72.54024523637426)
            .yVelocity(56.546714482344974)
            ;

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(3.636115)
            .strafePodX(-5.98725)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            ;



}
