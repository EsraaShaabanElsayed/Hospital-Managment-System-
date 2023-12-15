object file {
  //write 
    def write(Query:String,FilePath:String,appendType:Boolean):Boolean={
    var writer:PrintWriter=null
    try{
      println("\n Writing in !" + FilePath)
      writer = new PrintWriter(new FileWriter(new File(FilePath), appendType))
      writer.println(Query)
      println("...Done !")
      true
    }
    catch {
      case e:IOException =>
        println(e)
        false
    }finally {
    if(writer!=null)
      writer.close()
  }

  }

  //read 
    case class Receptionist(ID: Int, firstName: String, lastName: String, gender: String)
  case class Appointment(id:String,patientId:String,doctorId:String,date:String,time: String)
    def read(FilePath: String): Option[List[Any]] = {
    if (FilePath == "receptionist.txt") {
      try {
        val f = Source.fromFile(FilePath)
        val receptionists = f.getLines().map { line =>
          val Array(id, firstName, lastName, gender) = line.split("@")
          Receptionist(id.toInt, firstName, lastName, gender)
        }.toList
        f.close()

        // Now you have a list of Receptionist objects
        receptionists.foreach { receptionist =>
          println(s"ID: ${receptionist.ID}, Name: ${receptionist.firstName} ${receptionist.lastName}, Gender: ${receptionist.gender}")
        }

        Some(receptionists)
      } catch {
        case e: Exception =>
          println("An error occurred: " + e.getMessage)
          None
      }
    } else if (FilePath == "Appointment.txt") {
      try {
        val f = Source.fromFile(FilePath)
        val appointments = f.getLines().map { line =>
          val Array(id, patientId, doctorId, date, time) = line.split("@")
          Appointment(id, patientId, doctorId, date, time)
        }.toList
        f.close()

        // Now you have a list of Appointment objects
        appointments.foreach { appointment =>
          println(s"ID: ${appointment.id}, Patient: ${appointment.patientId}, Doctor: ${appointment.doctorId}, Date: ${appointment.date}, Time: ${appointment.time}")
        }

        Some(appointments)
      } catch {
        case e: Exception =>
          println("An error occurred: " + e.getMessage)
          None
      }
    } else {
      println("Invalid file name or path")
      None
    }
  }
}
