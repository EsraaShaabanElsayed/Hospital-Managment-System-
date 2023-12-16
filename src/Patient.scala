import akka.actor.{Actor, ActorLogging}

// Doctor class
case class Patient( Fname: String, LName: String, gender: String,email:String,Username:String,diagnoses :String)
// Messages for CRUD operations
case class AddPatient(patient: Patient)
case class GetPatient(Username: String)
case class UpdatePatient(patient: Patient)
case class RemovePatient(username: String)

// DoctorRepository actor to manage doctors
class patientActor extends Actor with ActorLogging {
  val filename = "patient.txt"
  var patients: Map[String, Patient] = Map.empty
  private def objectHandling(r: Patient): String = {
    s"${r.Fname},${r.LName},${r.gender},${r.email},${r.Username},${r.diagnoses}"
  }

  override def receive: Receive = {
    case AddPatient(patient) =>
      patients += (patient.Username-> patient)
      //log.info(s"Patient added: $patient")
      file.write(objectHandling(patient), filename, true)

   /* case GetPatient(username: String) =>
      sender() ! patients.get(username: String)*/
    case GetPatient(username: String) =>
      val r = file.read(filename)
      r match {
        case Some(list) =>
          list.collectFirst {
            case patient: Patient if patient.Username == username =>

              println(s"Name: ${patient.Fname} ${patient.LName}, Gender: ${patient.gender},E-mail: ${patient.email},User Name: ${patient.Username},diagnoses: ${patient.diagnoses}")
          }
        case None =>
          println("Option is empty")
          None
      }

    case UpdatePatient(updatedPatient) =>
      patients.get(updatedPatient.Username) match {
        case Some(_) =>
          patients += (updatedPatient.Username ->updatedPatient)
          log.info(s"Patient updated: $updatedPatient")
        case None =>
          log.warning(s"Patient with Username ${updatedPatient.Username} not found.")
      }

    case RemovePatient(username) =>
      patients.get(username) match {
        case Some(doctor) =>
          patients -= username
          log.info(s"Patient removed: $doctor")
        case None =>
          log.warning(s"Patient with Username $username not found.")
      }
  }
}
