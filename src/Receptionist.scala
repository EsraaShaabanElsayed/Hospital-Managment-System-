import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

case class Receptionist( username: String,email: String, password: String ,specialization: String)
case class AddReceptionist(receptionist: Receptionist)
case class GetReceptionist(username: String)
case class UpdateReceptionist(receptionist: Receptionist)
case class RemoveReceptionist(username:String)

// New Appointment-related messages
case class ScheduleAppointment(patientUsername: String, doctorUsername: String, day: String)
case class CancelAppointment(patientUsername: String, doctorUsername: String, day: String)
case object GetAppointments

class ReceptionistActor(patientActor: ActorRef, appointmentActor: ActorRef) extends Actor with ActorLogging {
  var receptionists: Map[Int, Receptionist] = Map.empty

  override def receive: Receive = {
    case AddPatient(patient) =>
      patientActor ! AddPatient(patient)

    case GetPatient(username) =>
      patientActor ! GetPatient(username)

    case UpdatePatient(patient) =>
      patientActor ! UpdatePatient(patient)

    case RemovePatient(username) =>
      patientActor ! RemovePatient(username)

    case AddReceptionist(receptionist) =>
      receptionists += (receptionist.username -> receptionist)
      log.info(s"Receptionist added: $receptionist")

    case GetReceptionist(username) =>
      sender() ! receptionists.get(username)

    case UpdateReceptionist(updatedReceptionist) =>
      receptionists.get(updatedReceptionist.username) match {
        case Some(_) =>
          receptionists += (updatedReceptionist.username-> updatedReceptionist)
          log.info(s"Receptionist updated: $updatedReceptionist")
        case None =>
          log.warning(s"Receptionist with ID ${updatedReceptionist.id} not found.")
      }
    case RemoveReceptionist(username)
    =>
      receptionists.get(username) match {
        case Some(doctor) =>
          receptionists -= username 
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
