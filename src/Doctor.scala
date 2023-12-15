import akka.actor.{Actor, ActorLogging, Props}

class DoctorActor(val id: Int, val name: String, val specialty: String) extends Actor with ActorLogging {
  import DoctorActor._

  private var schedule: Map[String, String] = Map.empty
  private var patientHistory: Map[String, String] = Map.empty

  override def receive: Receive = {
    case ScheduleCheckup(date, patientId) =>
      scheduleCheckup(date, patientId)
      sender() ! CheckupScheduled

    case ViewSchedule(date) =>
      sender() ! ScheduleView(viewSchedule(date))

    case ViewPatientHistory(patientId) =>
      sender() ! PatientHistoryView(viewPatientHistory(patientId))

    case ConductCheckup(patientId, diagnosis, medication) =>
      conductCheckup(patientId, diagnosis, medication)
      sender() ! CheckupConducted

    case CancelCheckup(date) =>
      cancelCheckup(date)
      sender() ! CheckupCancelled
  }

  private def scheduleCheckup(date: String, patientId: String): Unit = {
    schedule += (date -> patientId)
  }

  private def viewSchedule(date: String): Option[String] = {
    schedule.get(date)
  }

  private def viewPatientHistory(patientId: String): Option[String] = {
    patientHistory.get(patientId)
  }

  private def conductCheckup(patientId: String, diagnosis: String, medication: String): Unit = {
    val prescription = s"Diagnosis: $diagnosis, Medication: $medication"
    patientHistory += (patientId -> prescription)
  }

  private def cancelCheckup(date: String): Unit = {
    schedule -= date
  }
}

object DoctorActor {
  // Commands
  case class ScheduleCheckup(date: String, patientId: String)
  case class ViewSchedule(date: String)
  case class ViewPatientHistory(patientId: String)
  case class ConductCheckup(patientId: String, diagnosis: String, medication: String)
  case class CancelCheckup(date: String)

  // Responses
  case object CheckupScheduled
  case class ScheduleView(patientId: Option[String])
  case class PatientHistoryView(history: Option[String])
  case object CheckupConducted
  case object CheckupCancelled

  def props(id: Int, name: String, specialty: String): Props =
    Props(new DoctorActor(id, name, specialty))
}
