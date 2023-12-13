import scala.io.Source
import scala.util.{Success, Try}

abstract private class EntityHandler[T <: Product](filePath: String) {
  def readEntities(parseLine: Array[String] => T): List[T] = {
    Try {
      Source.fromFile(filePath).getLines().toList.map { line =>
        val attributes = line.split(",").map(_.trim)
        parseLine(attributes)
      }
    } match {
      case Success(entities) => entities
      case _ => List.empty
    }
  }

  def writeEntities(entities: List[T]): Unit = {
    val lines = entities.map(entity => entity.productIterator.mkString(","))
    java.nio.file.Files write java.nio.file.Paths.get(filePath)
  }

  def createEntity(entity: T): Unit = {
    val entities = readEntities(parseLine)
    writeEntities(entities :+ entity)
  }

  def updateEntity(updatedEntity: T, entityId: String): Unit = {
    val entities = readEntities(parseLine)
    val updatedList = entities.map {
      case e if e.productElement(0).toString == entityId => updatedEntity
      case other => other
    }
    writeEntities(updatedList)
  }

  def deleteEntity(entityId: String): Unit = {
    val entities = readEntities(parseLine).filterNot(e => e.productElement(0).toString == entityId)
    writeEntities(entities)
  }

  def retrieveEntity(entityId: String): Option[T] = {
    readEntities(parseLine).find(e => e.productElement(0).toString == entityId)
  }

  def parseLine: Array[String] => T
}