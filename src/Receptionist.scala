import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

case class Receptionist(id: Int, name: String, specialization: String)
case class AddReceptionist(receptionist: Receptionist)
case class GetReceptionist(id: Int)
case class UpdateReceptionist(receptionist: Receptionist)
case class RemoveReceptionist(id: Int)

// New Appointment-related messages
case class ScheduleAppointment(patientUsername: String, doctorUsername: String, day: String)
case class CancelAppointment(patientUsername: String, doctorUsername: String, day: String)
case object GetAppointments

class ReceptionistActor(patientActor: ActorRef, appointmentActor: ActorRef) extends Actor with ActorLogging {
  var receptionists: Map[Int, Receptionist] = Map.empty

  override def receive: Receive = {
    case AddPatient(patient) =>
      patientActor ! AddPatient(patient)

    case GetPatient(id) =>
      patientActor ! GetPatient(id)

    case UpdatePatient(patient) =>
      patientActor ! UpdatePatient(patient)

    case RemovePatient(id) =>
      patientActor ! RemovePatient(id)

    case AddReceptionist(receptionist) =>
      receptionists += (receptionist.id -> receptionist)
      log.info(s"Receptionist added: $receptionist")

    case GetReceptionist(id) =>
      sender() ! receptionists.get(id)

    case UpdateReceptionist(updatedReceptionist) =>
      receptionists.get(updatedReceptionist.id) match {
        case Some(_) =>
          receptionists += (updatedReceptionist.id -> updatedReceptionist)
          log.info(s"Receptionist updated: $updatedReceptionist")
        case None =>
          log.warning(s"Receptionist with ID ${updatedReceptionist.id} not found.")
      }
    case RemoveReceptionist(id)
    =>
      receptionists.get(id) match {
        case Some(doctor) =>
          receptionists -= id
          log.info(s"Receptionist removed: $doctor")
        case None =>
          log.warning(s"Receptionsit with ID $id not found.")
      }

    // New Appointment-related cases
    case ScheduleAppointment(patientUsername, doctorUsername, day) =>
      appointmentActor ! AppointmentActor.ScheduleAppointment(Appointment(patientUsername, doctorUsername, day))

    case CancelAppointment(patientUsername, doctorUsername, day) =>
      appointmentActor ! AppointmentActor.CancelAppointment(Appointment(patientUsername, doctorUsername, day))

    case GetAppointments =>
      appointmentActor ! AppointmentActor.GetAppointments
  }
}

object HospitalApp extends App {
  val system = ActorSystem("hospitalSystem")

  // Create PatientActor and AppointmentActor
  val patientActor = system.actorOf(Props[patientActor], "patientRepository")
  val appointmentActor = system.actorOf(Props[AppointmentActor], "appointmentRepository")

  // Create ReceptionistActor and pass PatientActor and AppointmentActor references
  val receptionist = system.actorOf(Props(new ReceptionistActor(patientActor, appointmentActor)), "receptionist")

  // Example CRUD operations
  receptionist ! AddPatient(Patient(1, "Dr. Smith", "Cardiology"))
  receptionist ! AddPatient(Patient(2, "Dr. Johnson", "Orthopedics"))
  receptionist ! GetPatient(1)
  receptionist ! UpdatePatient(Patient(1, "Dr. Smith Jr.", "Cardiology"))
  receptionist ! RemovePatient(2)

  // Example Appointment operations
  receptionist ! ScheduleAppointment("patient1", "doctor1", "2023-01-01")
  receptionist ! CancelAppointment("patient1", "doctor1", "2023-01-01")
  receptionist ! GetAppointments

  // Shut down the actor system
  system.terminate()
}
