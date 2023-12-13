import java.time.LocalDateTime
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class Receptionist (val id: String, val Fname: String, val Lname: String, val gender: String)
  extends Person {
  //handling object for text file 
    // do not touch this code 
      def objectHandling(id: String, Fname: String,Lname:String, gender: String): String = {
    val receptionist = id + "@" + Fname +"@"+Lname+ "@" + gender
    receptionist
  }
//  def generateUniqueAppointmentId() = ???

  def addPatient(patient: Patient): Unit = {

    def addPatient(name: String, gender: String,id:String, diagnoses: ListBuffer[String], medicalHistory: ListBuffer[String]): Patient = {
      new Patient(name, gender, id,ListBuffer(), ListBuffer())

    }
    def addAppointment(patient: Patient, doctor: Doctor, date: String, time: String): Appointment = {

      val appointmentId = id//generateUniqueAppointmentId() // You need a function to generate a unique ID
      val patientId = patient.id
      val doctorId = doctor.id

      // Assuming date and time are passed as parameters
      new Appointment(appointmentId, patientId, doctorId, date, time)
    }

  }
    // Add more methods for CRUD operations on appointments and patient

}
