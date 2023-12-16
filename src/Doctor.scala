import akka.actor.{Actor, ActorLogging}

// Doctor class

case class Doctor( Fname: String, LName: String, gender: String,email:String,password:String,Username:String,specialization: String)
// Messages for CRUD operations
case class AddDoctor(doctor: Doctor)
case class GetDoctor(username: String)
case class UpdateDoctor(doctor: Doctor)
case class RemoveDoctor(username: String)

// DoctorRepository actor to manage doctors
class DoctorActor extends Actor with ActorLogging {
  var doctors: Map[ String, Doctor] = Map.empty
  val filename = "doctor.txt"
  private def objectHandling(r: Doctor): String = {
    s"${r.Fname},${r.LName},${r.gender},${r.email},${r.password},${r.Username},${r.specialization}"
  }
  override def receive: Receive = {
    case AddDoctor(doctor) =>
      doctors += (doctor.Username -> doctor)
      log.info(s"Doctor added: $doctor")

    case GetDoctor(username) =>
      sender() ! doctors.get(username)

    case UpdateDoctor(updatedDoctor) =>
      doctors.get(updatedDoctor.Username) match {
        case Some(_) =>
          doctors += (updatedDoctor.Username-> updatedDoctor)
          log.info(s"Doctor updated: $updatedDoctor")
        case None =>
          log.warning(s"Doctor with Username ${updatedDoctor.Username} not found.")
      }

    case RemoveDoctor(username) =>
      doctors.get(username) match {
        case Some(doctor) =>
          doctors -= username
          log.info(s"Doctor removed: $doctor")
        case None =>
          log.warning(s"Doctor with Username $username not found.")
      }

  }
}
