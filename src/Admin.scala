import akka.actor.{Actor, ActorLogging, Props}

case class Doctor(id: Int, name: String, specialization: String)
case class Receptionist(id: Int, name: String)
class Admin extends Actor with ActorLogging {

  var doctors: List[Doctor] = loadDoctorsFromFile()
  var receptionist: List[Receptionist] = loadReceptionistFromFile()
  def receive: Receive = {
    case GetReceptionist => sender() ! receptionist
    case AddReceptionist(receptionist) => addReceptionist(receptionist)
    case UpdateReceptionist(receptionist) => updateReceptionist(receptionist)
    case RemoveReceptionist(id) => removeReceptionist(id)
    case GetDoctors => sender() ! doctors
    case AddDoctor(doctor) => addDoctor(doctor)
    case UpdateDoctor(doctor) => updateDoctor(doctor)
    case RemoveDoctor(id) => removeDoctor(id)
  }
def loadReceptionistFromFile(): List[Receptionist] = {
    // Implementation to read doctors from a file
    // This can be a simple CSV, JSON, or any format suitable for your application
    // Return a list of Doctor objects
    // Example: List(Doctor(1, "Dr. Smith", "Cardiology"), Doctor(2, "Dr. Johnson", "Pediatrics"))
    // ...
  }

  def loadDoctorsFromFile(): List[Doctor] = {
    // Implementation to read doctors from a file
    // This can be a simple CSV, JSON, or any format suitable for your application
    // Return a list of Doctor objects
    // Example: List(Doctor(1, "Dr. Smith", "Cardiology"), Doctor(2, "Dr. Johnson", "Pediatrics"))
    // ...
  }
def saveReceptionistsToFile(): Unit = {
    // Implementation to save doctors to a file
    // This should write the current state of the 'doctors' list to a file
    // ...
  }
  def saveDoctorsToFile(): Unit = {
    // Implementation to save doctors to a file
    // This should write the current state of the 'doctors' list to a file
    // ...
  }
 def addReceptionist(receptionist: Receptionist): Unit = {
    receptionists = Receptionists :+ receptionist
    saveReceptionistsToFile()
  }
  def addDoctor(doctor: Doctor): Unit = {
    doctors = doctors :+ doctor
    saveDoctorsToFile()
  }
   def updateReceptionist(updatedReceptionist: Receptionist): Unit = {
    receptionists.find(_.id == updatedReceptionist.id) match {
      case Some(existingReceptionist) =>
        receptionists = receptionists.map {
          case `existingReceptionist` => updatedReceptionist
          case otherReceptionist => otherReceptionist
        }
        saveReceptionistsToFile()
      case None => log.warning(s"Receptionist with ID ${updatedReceptionist.id} not found.")
    }
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
  def removeReceptionist(receptionistId: Int): Unit = {
    receptionists.find(_.id == receptionistId) match {
      case Some(_) =>
        receptionists = receptionists.filterNot(_.id == receptionistId)
       saveReceptionistsToFile()
      case None => log.warning("Receptionist with ID $receptionistId not found.")
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
case object GetReceptionist
case class AddReceptionist(receptionist:Receptionist)
case class UpdateReceptionist(receptionist:Receptionist)
case class RemoveReceptionist(id: Int)
