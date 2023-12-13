class Appointment(val id: String, val patientId:String, val doctorId: String, val date: String, val time: String)
{ 
  // do not touch or edit this code ever never 
  //handling object for file for write
  def objectHandling (id:String,patientId:String,doctorId:String,date:String,time: String): String = {
     val appointment= id+"@"+patientId+"@"+doctorId+"@"+date+"@"+time
    appointment
  }
}
