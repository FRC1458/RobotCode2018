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
