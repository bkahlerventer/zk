package zk.jute

import java.io._

class CsvOutputArchive(out:OutputStream) extends OutputArchive {
  val stream:PrintStream = new PrintStream(out, true, "UTF-8")
  var isFirst: Boolean = true

  private def throwExceptionOnError(tag:String): Unit =
    if(stream.checkError()) throw new IOException(s"Error serializing $tag")

  private def printCommaUnlessFirst(): Unit = {
    if(!isFirst) stream.print(',')
    isFirst = false
  }

  override def writeByte(b: Byte, tag: String): Unit = writeLong(b.asInstanceOf[Long], tag)

  override def writeBool(b: Boolean, tag: String): Unit = {
    printCommaUnlessFirst()
    stream.print(if(b) "T" else "F")
    throwExceptionOnError(tag)
  }

  override def writeInt(i: Int, tag: String): Unit = writeLong(i.asInstanceOf[Long], tag)

  override def writeLong(l: Long, tag: String): Unit = writeLong(l, tag)

  override def writeFloat(f: Float, tag: String): Unit = writeDouble(f.asInstanceOf[Double], tag)

  override def writeDouble(d: Double, tag: String): Unit = {
    printCommaUnlessFirst()
    stream.print(d)
    throwExceptionOnError(tag)
  }

  override def writeString(s: String, tag: String): Unit = {
    printCommaUnlessFirst()
    stream.print(Utils.toCSVString(s))
    throwExceptionOnError(tag)
  }

  override def writeBuffer(buf: Array[Byte], tag: String): Unit = {
    printCommaUnlessFirst()
    stream.print(Utils.toCSVBuffer(buf))
    throwExceptionOnError(tag)
  }

  override def writeRecord(r: Record, tag: String): Unit = r match {
    case null =>
    case _ => r.serialize(this, tag)
  }

  override def startRecord(r: Record, tag: String): Unit =
    if (!(tag == null || tag == ""))  printCommaUnlessFirst(); stream.print("s{"); isFirst = true


  override def endRecord(r: Record, tag: String): Unit = tag match {
    case t if !((t == null) || (t == "")) => stream.print("\n"); isFirst = true
    case _ => stream.print("}"); isFirst = false
  }

  override def startVector(v: Vector[_], tag: String): Unit = {
    printCommaUnlessFirst()
    stream.print("v{")
    isFirst = true
  }

  override def endVector(v: Vector[_], tag: String): Unit = stream.print("}"); isFirst = false


  override def startMap(m: Map[_, _], tag: String): Unit = {
    printCommaUnlessFirst()
    stream.print("m{")
    isFirst = true
  }

  override def endMap(m: Map[_, _], tag: String): Unit = stream.print("}"); isFirst = false

}

object CsvOutputArchive {
  def getArchive(strm:OutputStream):CsvOutputArchive = new CsvOutputArchive(strm)
}
