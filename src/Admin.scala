class Admin(val id: String, val Fname: String,val Lname: String, val gender: String)
  extends  Person {
    //do not touch or edit this code 
    //handling object for text file 
  def objectHandling(id: String, Fname: String, Lname: String, gender: String): String = {
    val admin = id + "@" + Fname + "@" + Lname + "@" + gender
    admin
  }

  //var doctors: List[Doctor] = List()
  //
  //def addDoctor(doctor: Doctor): Unit = {
  //
  //}
  //
  //def findDoctorByName(name: String): //Option[Doctor] =
  //  {
  //
  //}
  //
  //
  //def updateDoctor(oldName: String, newDoctor: Doctor): Unit = {
  //
  //}
  //
  //// Delete operation
  //def deleteDoctor(name: String): Unit = {
  //
  //
  //}
}
