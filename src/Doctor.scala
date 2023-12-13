import java.time.LocalDateTime
import scala.collection.mutable.ListBuffer

class Doctor(val name: String,  val gender: String,val id :String , val availability: ListBuffer[LocalDateTime], val specialization: String)
  extends Person {

}
