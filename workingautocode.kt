/*addAutoMode(AutoMode.create {
            val left = (modifier!!.getLeftTrajectory())
            val right = (modifier!!.getRightTrajectory())

            val startTime = systemTimeMillis
            while(true) {
                val i = Math.floor((systemTimeMillis - startTime)/25.0).toInt()
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
