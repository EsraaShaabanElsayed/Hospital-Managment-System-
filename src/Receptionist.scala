import akka.actor.{Actor, ActorSystem, Props}
import scala.io.Source
import scala.util.{Failure, Success, Try}
import java.nio.file.{Files, Paths, StandardOpenOption}

// Define messages for communication between actors
case class AddPatient(patient: String)
case class UpdatePatient(oldPatient: String, newPatient: String)
case class DeletePatient(patient: String)
case class AddAppointment(appointment: String)
case object RetrieveAppointments

// Define Receptionist actor
class Receptionist(patientsFile: String, appointmentsFile: String) extends Actor {
  private var patients: List[String] = readFromFile(patientsFile)
  private var appointments: List[String] = readFromFile(appointmentsFile)

  override def receive: Receive = {
    case AddPatient(patient) =>
      patients = patients :+ patient
      writeToFile(patientsFile, patients)

    case UpdatePatient(oldPatient, newPatient) =>
      patients = patients.map(p => if (p == oldPatient) newPatient else p)
      writeToFile(patientsFile, patients)

    case DeletePatient(patient) =>
      patients = patients.filterNot(_ == patient)
      writeToFile(patientsFile, patients)

    case AddAppointment(appointment) =>
      appointments = appointments :+ appointment
      writeToFile(appointmentsFile, appointments)

    case RetrieveAppointments =>
      sender() ! appointments
  }

  private def readFromFile(filePath: String): List[String] = {
    Try(Source.fromFile(filePath).getLines().toList) match {
      case Success(lines) => lines
      case Failure(_)     => List()
    }
  }

  private def writeToFile(filePath: String, data: List[String]): Unit = {
    Files.write(Paths.get(filePath), data.mkString("\n").getBytes, StandardOpenOption.CREATE)
  }
}

object ReceptionistApp extends App {
  // Specify file paths
  val patientsFile = "patients.txt"
  val appointmentsFile = "appointments.txt"

  // Create Actor System
  val system = ActorSystem("ReceptionistSystem")

  // Create Receptionist actor
  val receptionist = system.actorOf(Props(new Receptionist(patientsFile, appointmentsFile)), "receptionist")

  // Example usage
  receptionist ! AddPatient("John Doe")
  receptionist ! AddAppointment("Appointment with John Doe at 10 AM")

  // Retrieve appointments
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.duration._
  implicit val timeout: Timeout = Timeout(5.seconds)
  val future = (receptionist ? RetrieveAppointments).mapTo[List[String]]
  val result = future.value.get.get
  println(s"Appointments: $result")

  // Shutdown the system
  system.terminate()
}
