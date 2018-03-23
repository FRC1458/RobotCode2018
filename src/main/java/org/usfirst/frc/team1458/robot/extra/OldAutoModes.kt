package org.usfirst.frc.team1458.robot.extra

import org.usfirst.frc.team1458.lib.pathfinding.SplineFollower
import org.usfirst.frc.team1458.lib.util.flow.delay
import org.usfirst.frc.team1458.lib.util.flow.systemTimeMillis
import org.usfirst.frc.team1458.robot.GameData2018

/**
 * TODO: Add Comment
 *
 * @author asinghani
 */
/*addAutoMode(AutoMode.create {
    robot.drivetrain.tankDrive(0.7, 0.7)
    delay(200)
    robot.drivetrain.tankDrive(-0.7, -0.7)
    delay(200)
    robot.drivetrain.tankDrive(0.0, 0.0)
    delay(50)
    SplineFollower (
            leftCSV = "/home/admin/auton1_left_detailed.csv",
            rightCSV = "/home/admin/auton1_right_detailed.csv",
            drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
            name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
            everyIterationFunc = { robot.intake.update(true, false) }
    ).auto()
    robot.intake.update(false, false)

    elev1.speed = 0.8
    elev2.speed = 0.8
    delay(350)
    elev1.speed = 0.0
    elev2.speed = 0.0


    SplineFollower (
            leftCSV = "/home/admin/auton2_left_detailed.csv",
            rightCSV = "/home/admin/auton2_right_detailed.csv",
            drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
            name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
            reversed = true
    ).auto()


    val startTime = systemTimeMillis
    elev1.speed = 0.6
    elev2.speed = 0.6

    if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
        SplineFollower (
                leftCSV = "/home/admin/finalleft_left_detailed.csv",
                rightCSV = "/home/admin/finalleft_right_detailed.csv",
                drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
                name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                everyIterationFunc = {
                    if(systemTimeMillis - startTime < 1900) {
                        elev1.speed = 0.6
                        elev2.speed = 0.6
                    } else {
                        elev1.speed = 0.0
                        elev2.speed = 0.0
                    }
                }
        ).auto()
    } else {
        SplineFollower (
                leftCSV = "/home/admin/finalright_left_detailed.csv",
                rightCSV = "/home/admin/finalright_right_detailed.csv",
                drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
                name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
                everyIterationFunc = {
                    if(systemTimeMillis - startTime < 1900) {
                        elev1.speed = 0.6
                        elev2.speed = 0.6
                    } else {
                        elev1.speed = 0.0
                        elev2.speed = 0.0
                    }
                }
        ).auto()
    }

    val startTime2 = systemTimeMillis
    while(systemTimeMillis - startTime2 < 800) {
        robot.intake.update(false, true)
    }
    robot.intake.update(false, false)

    SplineFollower (
            leftCSV = "/home/admin/auton2_left_detailed.csv",
            rightCSV = "/home/admin/auton2_right_detailed.csv",
            drivetrain = robot.drivetrain, gyro = robot.navX.yaw, gyro_kP = 0.0,
            name = "OwnSwitch", stopFunc = { !(isAutonomous && isEnabled) },
            reversed = true
    ).auto()
})
*/