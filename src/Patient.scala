import scala.collection.mutable.ListBuffer
class Patient(val id: String,  val name: String,gender: String, val diagnoses: ListBuffer[String], val medicalHistory: ListBuffer[String])
    extends Person{
  def this(id: String, name: String, gender: String) = this(id, name, gender, ListBuffer(), ListBuffer())

}
