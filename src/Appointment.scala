// class Appointment(val id: String, val patientId:String, val doctorId: String, val date: String, val time: String)
// { 
//   // do not touch or edit this code ever never 
//   //handling object for file for write
//   def objectHandling (id:String,patientId:String,doctorId:String,date:String,time: String): String = {
//      val appointment= id+"@"+patientId+"@"+doctorId+"@"+date+"@"+time
//     appointment
//   }
// }
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
