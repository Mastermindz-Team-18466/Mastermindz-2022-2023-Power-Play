package com.learning.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        Vector2d endPosition = new Vector2d(1.5 * 23.5 - Math.sqrt(37.5) - 0, -3 * 23.5 + 49 + Math.sqrt(37.5));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(100, 200, Math.toRadians(400), Math.toRadians(400), 12.7)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(1.5 * 23.5, -3 * 23.5, Math.PI / 2))
                                .lineToSplineHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 63, Math.PI / 2 + Math.toRadians(40)))
                                .lineToSplineHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 49, Math.PI / 2 + Math.toRadians(40)))
                                .lineToConstantHeading(endPosition)
                                .lineToLinearHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 46.5,Math.PI / 2 + Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-(1.5 * 23.5 + 5), -3 * 23.5 + 46.5, Math.PI / 2 + Math.toRadians(90)))
                                .lineToLinearHeading(new Pose2d(-(1.5 * 23.5 + 2.5) + Math.sqrt(75), -3 * 23.5 + 51 + Math.sqrt(75), Math.PI / 2 + Math.toRadians(130)))
                                .waitSeconds(5)
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 + 2.5) + Math.sqrt(45), -3 * 23.5 + 47.5 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90)))
                                .lineToSplineHeading(new Pose2d(-(1.5 * 23.5 + 26.5), -3 * 23.5 + 47.5 + Math.sqrt(45), Math.PI / 2 + Math.toRadians(90)))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}