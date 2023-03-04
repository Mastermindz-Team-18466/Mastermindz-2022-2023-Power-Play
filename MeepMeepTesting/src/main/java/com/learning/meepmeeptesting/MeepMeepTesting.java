package com.learning.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(59.980, 59, Math.toRadians(345.772), Math.toRadians(280), 12.7)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(1.5 * 23.5 - Math.sqrt(37.5) - 0, -3 * 23.5 + 49 + Math.sqrt(37.5), Math.PI / 2 + Math.toRadians(40)))
                                //.lineToSplineHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 60, Math.PI / 2 + Math.toRadians(40)))
                                //.lineToSplineHeading(new Pose2d(1.5 * 23.5 - 3, -3 * 23.5 + 49, Math.PI / 2 + Math.toRadians(40)))
                                //.lineToConstantHeading(new Vector2d(1.5 * 23.5 - Math.sqrt(37.5) - 0, -3 * 23.5 + 49 + Math.sqrt(37.5)))
                                //.waitSeconds(2)

                                //.lineToConstantHeading(new Vector2d(1.5 * 23.5 - 2, -3 * 23.5 + 50))
                                //.lineToSplineHeading(new Pose2d(1.5 * 23.5 - 25.5, -3 * 23.5 + 50, Math.PI / 2))


                                .lineToConstantHeading(new Vector2d(1.5 * 23.5 - 2, -3 * 23.5 + 50))
                                .lineToSplineHeading(new Pose2d(1.5 * 23.5 - 2, -3 * 23.5 + 50, Math.PI / 2))


                                //.lineToConstantHeading(new Vector2d(1.5 * 23.5 - 2, -3 * 23.5 + 50))
                                //.lineToSplineHeading(new Pose2d(1.5 * 23.5 + 21.5, -3 * 23.5 + 50, Math.PI / 2))

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}