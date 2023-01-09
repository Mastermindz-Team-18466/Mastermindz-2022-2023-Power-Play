package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;

import sun.text.ComposedCharIter;

public class MeepMeepTesting {
    public static double INCHES_PER_TILE = 23.5;
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(59.98047895234888, 59, Math.toRadians(298.8372432298136), Math.toRadians(280), 11.4)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(1.5 * INCHES_PER_TILE, -3 * INCHES_PER_TILE, Math.toRadians(90)))

                                // Cycle
                                .forward(INCHES_PER_TILE * 2.5)

                                /* Strafe:
                                .strafeRight(INCHES_PER_TILE * 0.5)
                                .strafeLeft(INCHES_PER_TILE * 0.5)
                                */

                                // Parking:
                                .splineTo(new Vector2d(INCHES_PER_TILE * 2.5, INCHES_PER_TILE * 2.5), Math.toRadians(45))

                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}