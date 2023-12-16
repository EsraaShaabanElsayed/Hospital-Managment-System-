import akka.actor.{Actor, ActorLogging}

// Doctor class
case class Patient( Fname: String, LName: String, gender: String,email:String,Username:String)
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
    s"${r.Fname},${r.LName},${r.gender},${r.email},${r.Username}"
  }

  override def receive: Receive = {
    case AddPatient(patient) =>
      patients += (patient.Username-> patient)
      log.info(s"Patient added: $patient")

    case GetPatient(username: String) =>
      sender() ! patients.get(username: String)

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
