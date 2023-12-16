import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
case class Receptionist(id: Int, name: String, specialization: String)
case class AddReceptionist(receptionist: Receptionist)
case class GetReceptionist(id: Int)
case class UpdateReceptionist(receptionist: Receptionist)
case class RemoveReceptionist(id: Int)
class ReceptionistActor(patientActor: ActorRef) extends Actor with ActorLogging {
  var receptionists: Map[Int, Receptionist] = Map.empty
  override def receive: Receive = {
    case AddPatient(patient) =>
      patientActor ! AddPatient(patient)

    case GetPatient(id) =>
      patientActor ! GetPatient(id)

    case UpdatePatient(patient) =>
      patientActor! UpdatePatient(patient)

    case RemovePatient(id) =>
      patientActor ! RemovePatient(id)
    case AddReceptionist(receptionist)
    =>
      receptionists += (receptionist.id -> receptionist)
      log.info(s"Doctor added: $receptionist")

    case GetReceptionist(id)
    =>
      sender() ! receptionists.get(id)

    case UpdateReceptionist(updatedReceptionist)
    =>
      receptionists.get(updatedReceptionist.id) match {
        case Some(_) =>
          receptionists += (updatedReceptionist.id -> updatedReceptionist)
          log.info(s"Doctor updated: $updatedReceptionist")
        case None =>
          log.warning(s"Doctor with ID ${updatedReceptionist.id} not found.")
      }

    case RemoveReceptionist(id)
    =>
      receptionists.get(id) match {
        case Some(doctor) =>
          receptionists -= id
          log.info(s"Doctor removed: $doctor")
        case None =>
          log.warning(s"Doctor with ID $id not found.")
      }
  }


}

object PatientApp extends App {
  val system = ActorSystem("hospitalSystem")

  // Create DoctorRepository actor
  val patientActor = system.actorOf(Props[patientActor], "patientRepository")

  // Create Admin actor and pass DoctorRepository actor reference
  val admin = system.actorOf(Props(new ReceptionistActor(patientActor)), "admin")

  // Example CRUD operations
  admin ! AddPatient(Patient(1, "Dr. Smith", "Cardiology"))
  admin ! AddPatient(Patient(2, "Dr. Johnson", "Orthopedics"))
  admin ! GetPatient(1)
  admin ! UpdatePatient(Patient(1, "Dr. Smith Jr.", "Cardiology"))
  admin ! RemovePatient(2)

  // Shut down the actor system
  system.terminate()
}

  // Shutdown the system
  system.terminate()
}
