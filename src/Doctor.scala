import akka.actor.{Actor, ActorLogging}

// Doctor class
case class Doctor(username: String,email: String, password: String, specialization: String)

 


// Messages for CRUD operations
case class AddDoctor(doctor: Doctor)
case class GetDoctor(username: String)
case class UpdateDoctor(doctor: Doctor)
case class RemoveDoctor(username: String)

// DoctorRepository actor to manage doctors
class DoctorActor extends Actor with ActorLogging {
  var doctors: Map[Int, Doctor] = Map.empty

  override def receive: Receive = {
    case AddDoctor(doctor) =>
      doctors += (doctor.id -> doctor)
      log.info(s"Doctor added: $doctor")

    case GetDoctor(username) =>
      sender() ! doctors.get(username)

    case UpdateDoctor(updatedDoctor) =>
      doctors.get(updatedDoctor.id) match {
        case Some(_) =>
          doctors += (updatedDoctor.id -> updatedDoctor)
          log.info(s"Doctor updated: $updatedDoctor")
        case None =>
          log.warning(s"Doctor with ID ${updatedDoctor.id} not found.")
      }

    case RemoveDoctor(username) =>
      doctors.get(username) match {
        case Some(doctor) =>
          doctors -= username 
          log.info(s"Doctor removed: $doctor")
        case None =>
          log.warning(s"Doctor with ID $id not found.")
      }

  }
}


