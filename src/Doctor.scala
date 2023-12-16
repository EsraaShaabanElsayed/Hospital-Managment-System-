import akka.actor.{Actor, ActorLogging}

// Doctor class
case class Doctor(username: String,email: String, password: String, specialization: String)

 


// Messages for CRUD operations
case class AddDoctor(doctor: Doctor)
case class GetDoctor(id: Int)
case class UpdateDoctor(doctor: Doctor)
case class RemoveDoctor(id: Int)

// DoctorRepository actor to manage doctors
class DoctorActor extends Actor with ActorLogging {
  var doctors: Map[Int, Doctor] = Map.empty

  override def receive: Receive = {
    case AddDoctor(doctor) =>
      doctors += (doctor.id -> doctor)
      log.info(s"Doctor added: $doctor")

    case GetDoctor(id) =>
      sender() ! doctors.get(id)

    case UpdateDoctor(updatedDoctor) =>
      doctors.get(updatedDoctor.id) match {
        case Some(_) =>
          doctors += (updatedDoctor.id -> updatedDoctor)
          log.info(s"Doctor updated: $updatedDoctor")
        case None =>
          log.warning(s"Doctor with ID ${updatedDoctor.id} not found.")
      }

    case RemoveDoctor(id) =>
      doctors.get(id) match {
        case Some(doctor) =>
          doctors -= id
          log.info(s"Doctor removed: $doctor")
        case None =>
          log.warning(s"Doctor with ID $id not found.")
      }

  }
}


