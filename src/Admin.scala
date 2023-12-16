import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

// Admin actor to interact with DoctorRepository
class Admin(doctorActor: ActorRef,receptionistActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case AddDoctor(doctor) =>
      doctorActor ! AddDoctor(doctor)

    case GetDoctor(id) =>
      doctorActor ! GetDoctor(id)

    case UpdateDoctor(doctor) =>
      doctorActor ! UpdateDoctor(doctor)

    case RemoveDoctor(id) =>
      doctorActor ! RemoveDoctor(id)
    case AddReceptionist(receptionist)
    =>
      doctorActor ! AddReceptionist(receptionist)

    case GetReceptionist(id)
    =>
      receptionistActor ! GetReceptionist(id)

    case UpdateReceptionist(receptionist)
    =>
      receptionistActor ! UpdateReceptionist(receptionist)

    case RemoveReceptionist(id)
    =>
      receptionistActor ! RemoveReceptionist(id)
  }



}

object AdminApp extends App {
  val system = ActorSystem("hospitalSystem")

  // Create DoctorRepository actor
  val doctorActor = system.actorOf(Props[DoctorActor], "doctorRepository")
  val receptionistActor = system.actorOf(Props[ReceptionistActor], "receptionistRepository")
  // Create Admin actor and pass DoctorRepository actor reference
  val admin = system.actorOf(Props(new Admin(doctorActor,receptionistActor)), "admin")
  // Example CRUD operations
  admin ! AddDoctor(Doctor(1, "Dr. Smith", "Cardiology"))
  admin! AddDoctor(Doctor(2, "Dr. Johnson", "Orthopedics"))
  admin! GetDoctor(1)
  admin ! UpdateDoctor(Doctor(1, "Dr. Smith Jr.", "Cardiology"))
  admin! RemoveDoctor(2)
  admin ! AddReceptionist(Receptionist(1, "Dr. Smith", "Cardiology"))
  admin ! AddReceptionist(Receptionist(2, "Dr. Johnson", "Orthopedics"))
  admin ! GetReceptionist(1)
  admin ! UpdateReceptionist(Receptionist(1, "Dr. Smith Jr.", "Cardiology"))
  admin ! RemoveReceptionist(2)
  // Shut down the actor system
  system.terminate()
}
