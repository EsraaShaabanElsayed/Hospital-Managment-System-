import akka.actor.{Actor, ActorLogging, Props}

case class Doctor(id: Int, name: String, specialization: String)

class Admin extends Actor with ActorLogging {

  var doctors: List[Doctor] = loadDoctorsFromFile()

  def receive: Receive = {
    case GetDoctors => sender() ! doctors
    case AddDoctor(doctor) => addDoctor(doctor)
    case UpdateDoctor(doctor) => updateDoctor(doctor)
    case RemoveDoctor(id) => removeDoctor(id)
  }

  def loadDoctorsFromFile(): List[Doctor] = {
    // Implementation to read doctors from a file
    // This can be a simple CSV, JSON, or any format suitable for your application
    // Return a list of Doctor objects
    // Example: List(Doctor(1, "Dr. Smith", "Cardiology"), Doctor(2, "Dr. Johnson", "Pediatrics"))
    // ...
  }

  def saveDoctorsToFile(): Unit = {
    // Implementation to save doctors to a file
    // This should write the current state of the 'doctors' list to a file
    // ...
  }

  def addDoctor(doctor: Doctor): Unit = {
    doctors = doctors :+ doctor
    saveDoctorsToFile()
  }

  def updateDoctor(updatedDoctor: Doctor): Unit = {
    doctors.find(_.id == updatedDoctor.id) match {
      case Some(existingDoctor) =>
        doctors = doctors.map {
          case `existingDoctor` => updatedDoctor
          case otherDoctor => otherDoctor
        }
        saveDoctorsToFile()
      case None => log.warning(s"Doctor with ID ${updatedDoctor.id} not found.")
    }
  }

  def removeDoctor(doctorId: Int): Unit = {
    doctors.find(_.id == doctorId) match {
      case Some(_) =>
        doctors = doctors.filterNot(_.id == doctorId)
        saveDoctorsToFile()
      case None => log.warning(s"Doctor with ID $doctorId not found.")
    }
  }
}

object Admin {
  def props: Props = Props[Admin]
}

// Messages
case object GetDoctors
case class AddDoctor(doctor: Doctor)
case class UpdateDoctor(doctor: Doctor)
case class RemoveDoctor(id: Int)
