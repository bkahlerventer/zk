package zk.io

import java.io._
import java.lang.reflect.InvocationTargetException

import zk.jute.XmlOutputArchive

 /*
  * Front-end for serializers. Also serves as a factory for serializers.
  *
  */

/**
  * Creates a new instance of RecordWriter
  * @param out Output stream where the records will be serialized
  * @param format Serialization format ("binary", "xml", or "csv")
  */
class RecordWriter(out:OutputStream, format:String)  {
  private val archive = RecordWriter.createArchive(out,format)
  /**
    * Serialize a record
    * @param r record to be serialized
    */
  def write(r:Record):Unit = r.serialize(archive, "")
}

object RecordWriter {
  type getArchive = (OutputStream) => OutputArchive

  def constructFactory: Map[String, getArchive] =
    Map(("binary", BinaryOutputArchive.getArchive),
      ("csv", CsvOutputArchive.getArchive),
      ("xml", XmlOutputArchive.getArchive))

  private val archiveFactory = constructFactory

  def createArchive(out:OutputStream, format:String):OutputArchive = {
    val factory:getArchive = archiveFactory(format)
    try {
      if(factory != null) factory(out) else null
    } catch {
      case ex:IllegalArgumentException => ex.printStackTrace()
      case ex:InvocationTargetException => ex.printStackTrace()
      case ex:IllegalAccessException => ex.printStackTrace()
    }
    null
  }
}
