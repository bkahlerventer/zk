package zk.io

import java.io.InputStream

import zk.jute.XmlInputArchive


class RecordReader(in:InputStream, format:String) {
  private val archive: InputArchive = RecordReader.createArchive(in, format)

  def read(r: Record):Unit = r.deserialize(archive, "")
}

object RecordReader {
  type getArchive = InputStream => InputArchive
  val archiveFactory: Map[String, getArchive] =
    Map(("binary", BinaryInputArchive.getArchive),
        ("csv", CsvInputArchive.getArchive),
        ("xml", XmlInputArchive.getArchive))

  def createArchive(in:InputStream, format:String): InputArchive = {
    val factory = archiveFactory(format)
    if(factory != null) factory(in) else null
  }


}