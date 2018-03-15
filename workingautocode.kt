/*addAutoMode(AutoMode.create {
            val left = (modifier!!.getLeftTrajectory())
            val right = (modifier!!.getRightTrajectory())

            val startTime = systemTimeMillis
            while(true) {
                val i = Math.floor((systemTimeMillis - startTime)/50.0).toInt()
                drivetrain.setDriveVelocity(left[i].velocity, right[i].velocity)
                delay(1)
                System.out.println("${systemTimeMillis - startTime},${left[i].velocity},${right[i].velocity}," +
                        "${left[i].x},${left[i].y},${right[i].x},${right[i].y},${left[i].acceleration},${right[i].acceleration}")
            }

        })*/

        /*addAutoMode(AutoMode.create {
            //val left = (modifier!!.getLeftTrajectory())
            //val right = (modifier!!.getRightTrajectory())

            val left = Pathfinder.readFromCSV(File("~/left.csv"))
            val right = Pathfinder.readFromCSV(File("~/right.csv"))

            val startTime = systemTimeMillis
            while(true) {
                val i = Math.floor((systemTimeMillis - startTime)/25.0).toInt()
                drivetrain.setDriveVelocity(left[i].velocity, right[i].velocity)
                delay(1)
                System.out.println("${systemTimeMillis - startTime},${left[i].velocity},${right[i].velocity}," +
                        "${left[i].x},${left[i].y},${right[i].x},${right[i].y},${left[i].acceleration},${right[i].acceleration}")
            }

        })*/

/*dataListRight = ArrayList<Double>(5000)
        dataListLeft = ArrayList<Double>(5000)

        var i = 0.0
        while(i < 3000.0) {
            i += 100.0
            delay(30)
            talonLeft.PIDsetpoint = i
            talonRight.PIDsetpoint = i
        }

        talonLeft.PIDsetpoint = 3000.0
        talonRight.PIDsetpoint = 3000.0

        delay(3200)

        i = 3000.0
        while(i > 0) {
            i -= 100.0
            delay(30)
            talonLeft.PIDsetpoint = i
            talonRight.PIDsetpoint = i
        }

        talonLeft.PIDsetpoint = 0.0
        talonRight.PIDsetpoint = 0.0

        delay(2000)

        i = 0.0
        while(i < 3000.0) {
            i += 100.0
            delay(30)
            talonLeft.PIDsetpoint = -i
            talonRight.PIDsetpoint = -i
        }

        talonLeft.PIDsetpoint = -3000.0
        talonRight.PIDsetpoint = -3000.0

        delay(3200)

        i = 3000.0
        while(i > 0) {
            i -= 100.0
            delay(30)
            talonLeft.PIDsetpoint = -i
            talonRight.PIDsetpoint = -i
        }

        talonLeft.PIDsetpoint = 0.0
        talonRight.PIDsetpoint = 0.0*/



/*addAutoMode(AutoMode.create {
    if(first) {
        first = false
        println("motor_speed,right_velocity,right_stddev,left_velocity,left_stddev,direction")
    }

    val speed = SmartDashboard.getNumber("MotorSpeed", 0.0)

    if(directionFwd) {
        robot.drivetrain.tankDrive(speed, speed)
    } else {
        robot.drivetrain.tankDrive(-speed, -speed)
    }
    directionFwd = !directionFwd

    delay(500)
    val start = systemTimeMillis
    val rightArr = ArrayList<Double>()
    val leftArr = ArrayList<Double>()
    while(systemTimeMillis <= start + 500) {
        rightArr.add(robot.drivetrain.rightMaster._talonInstance!!.getSelectedSensorVelocity(0).toDouble())
        leftArr.add(robot.drivetrain.leftMaster._talonInstance!!.getSelectedSensorVelocity(0).toDouble())

        delay(1)
    }
    robot.drivetrain.tankDrive(0.0, 0.0)

    println("$speed,${rightArr.average()},${TurtleMaths.calculateSD(rightArr)}," +
            "${leftArr.average()},${TurtleMaths.calculateSD(leftArr)}," +
            "${(if(directionFwd) {"FORWARD"} else {"REVERSE"})}")
})*/

/*leftCSV = if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
    "/home/admin/pathleft_left_detailed.csv"
} else {
    "/home/admin/pathright_left_detailed.csv"
},
rightCSV = if(GameData2018.getOwnSwitch() == GameData2018.Side.LEFT) {
    "/home/admin/pathleft_right_detailed.csv"
} else {
    "/home/admin/pathright_right_detailed.csv"
},*/

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

    SplineFollower (
            leftCSV = "/home/admin/auton3_left_detailed.csv",
            rightCSV = "/home/admin/auton3_right_detailed.csv",
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

    // TODO: REEEEMOVE THIS LATER
    while(!mag2.triggered && isAutonomous && isEnabled) {
        elev1.speed = -0.5
        elev2.speed = -0.5
    }
    elev1.speed = 0.0
    elev2.speed = 0.0
})*/

// Go Left Auton