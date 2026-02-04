package com.team9889.ftc2025.Subsystem.Shooter;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;

@Config
public class ShooterConstants {
    public static Pose GOAL_POSE_RED = new Pose(138, 138);
    public static Pose GOAL_POSE_BLUE = GOAL_POSE_RED.mirror();

    public static double SCORE_HEIGHT = 37.0;
    public static double SCORE_ANGLE = Math.toRadians(-40);
    public static double PASS_THROUGH_POINT_RADIUS = 12.0;

    public static double HOOD_MAX_ANGLE = 0;
    public static double HOOD_MIN_ANGLE = 0;


}
