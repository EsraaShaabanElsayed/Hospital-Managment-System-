import akka.actor.{Actor, ActorLogging}

// Doctor class
case class Patient(username: String, email: String,specialization: String)

// Messages for CRUD operations
case class AddPatient(patient: Patient)
case class GetPatient(username: String)
case class UpdatePatient(patient: Patient)
case class RemovePatient(username:String)

// DoctorRepository actor to manage doctors
class patientActor extends Actor with ActorLogging {
  var patients: Map[Int, Patient] = Map.empty

  override def receive: Receive = {
    case AddPatient(patient) =>
      patients += (patient.id -> patient)
      log.info(s"Patient added: $patient")

    case GetPatient(username) =>
      sender() ! patients.get(username)

    case UpdatePatient(updatedPatient) =>
      patients.get(updatedPatient.id) match {
        case Some(_) =>
          patients += (updatedPatient.id ->updatedPatient)
          log.info(s"Patient updated: $updatedPatient")
        case None =>
          log.warning(s"Patient with ID ${updatedPatient.id} not found.")
      }

    case RemovePatient(username) =>
      patients.get(username) match {
        case Some(doctor) =>
          patients -= username 
          log.info(s"Patient removed: $doctor")
        case None =>
          log.warning(s"Patient with ID $id not found.")
      }
  }
}
