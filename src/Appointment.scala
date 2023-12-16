case class Appointment(patientUsername: String, doctorUsername: String, day: String)
import akka.actor.{Actor, ActorLogging, Props}

class AppointmentActor extends Actor with ActorLogging {
  import AppointmentActor._

  var appointments: List[Appointment] = List()
  override def receive: Receive = {
    case ScheduleAppointment(appointment) =>
      if (isAvailable(appointment)) {
        appointments = appointments :+ appointment
        log.info(s"Appointment scheduled: $appointment")
        sender() ! AppointmentScheduled(appointment)
      } else {
        log.warning(s"Doctor not available for the appointment: $appointment")
        sender() ! AppointmentNotScheduled(appointment)
      }

    case CancelAppointment(appointment) =>
      appointments = appointments.filterNot(_ == appointment)
      log.info(s"Appointment canceled: $appointment")
      sender() ! AppointmentCanceled(appointment)


    case GetAppointments =>
      sender() ! AppointmentsList(appointments)
    case UpdateAppointment(oldAppointment, newAppointment) =>
      if (appointments.contains(oldAppointment) && isAvailable(newAppointment)) {
        appointments = appointments.filterNot(_ == oldAppointment) :+ newAppointment
        log.info(s"Appointment updated: $oldAppointment -> $newAppointment")
        sender() ! AppointmentUpdated(oldAppointment, newAppointment)
      } else {
        log.warning(s"Unable to update appointment: $oldAppointment -> $newAppointment")
        sender() ! AppointmentNotUpdated(oldAppointment, newAppointment)
      }
  }

  // Helper method to check doctor availability
  private def isAvailable(appointment: Appointment): Boolean = {
    // Implement logic to check doctor's availability for the specified day
    // For simplicity, assume the doctor is always available
    true
  }
}

// Step 2: Companion object for AppointmentActor
object AppointmentActor {
  case class ScheduleAppointment(appointment: Appointment)
  case class CancelAppointment(appointment: Appointment)
  case object GetAppointments
  case class AppointmentScheduled(appointment: Appointment)
  case class AppointmentUpdated(oldAppointment: Appointment, newAppointment: Appointment)
  case class AppointmentNotUpdated(oldAppointment: Appointment, newAppointment: Appointment)

  case class AppointmentNotScheduled(appointment: Appointment)
  case class AppointmentCanceled(appointment: Appointment)
  case class AppointmentsList(appointments: List[Appointment])
  case class UpdateAppointment(oldAppointment: Appointment, newAppointment: Appointment)
  def props: Props = Props[AppointmentActor]

}
import akka.actor.{ActorRef, ActorSystem, Props}






/*
override def receive: Receive = {
    case ScheduleAppointment(appointment) =>
      if (isAvailable(appointment)) {
        appointments = appointments :+ appointment
        log.info(s"Appointment scheduled: $appointment")
        sender() ! AppointmentScheduled(appointment)
      } else {
        log.warning(s"Doctor not available for the appointment: $appointment")
        sender() ! AppointmentNotScheduled(appointment)
      }

    case CancelAppointment(appointment) =>
      appointments = appointments.filterNot(_ == appointment)
      log.info(s"Appointment canceled: $appointment")
      sender() ! AppointmentCanceled(appointment)
 */
