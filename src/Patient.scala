import akka.actor.{Actor, ActorLogging}

// Doctor class
case class Patient(id: Int, name: String, specialization: String)

// Messages for CRUD operations
case class AddPatient(patient: Patient)
case class GetPatient(id: Int)
case class UpdatePatient(patient: Patient)
case class RemovePatient(id: Int)

// DoctorRepository actor to manage doctors
class patientActor extends Actor with ActorLogging {
  var patients: Map[Int, Patient] = Map.empty

  override def receive: Receive = {
    case AddPatient(patient) =>
      patients += (patient.id -> patient)
      log.info(s"Patient added: $patient")

    case GetPatient(id) =>
      sender() ! patients.get(id)

    case UpdatePatient(updatedPatient) =>
      patients.get(updatedPatient.id) match {
        case Some(_) =>
          patients += (updatedPatient.id ->updatedPatient)
          log.info(s"Patient updated: $updatedPatient")
        case None =>
          log.warning(s"Patient with ID ${updatedPatient.id} not found.")
      }

    case RemovePatient(id) =>
      patients.get(id) match {
        case Some(doctor) =>
          patients -= id
          log.info(s"Patient removed: $doctor")
        case None =>
          log.warning(s"Patient with ID $id not found.")
      }
  }
}
