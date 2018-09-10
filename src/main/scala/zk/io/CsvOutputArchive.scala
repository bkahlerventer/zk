package zk.io

import java.io.{OutputStream, PrintStream}

import com.typesafe.scalalogging.Logger


// will use Option/None insead of exceptions where possible
// output CSV as String...
class CsvOutputArchive(out:OutputStream) extends OutputArchive {
  type CSV = String

  private var stream = new PrintStream(out, true, "UTF-8")
  private var isFirst: Boolean = true

  private def printCommaUnlessFirst():Unit = if(!isFirst) stream.print(",") else isFirst = false

  override def writeByte(b: Byte, tag: String): CSV = writeLong(b.asInstanceOf[Long], tag)

  override def writeBool(b: Boolean, tag: String): CSV = if (b) "T" else "F"

  override def writeInt(i: Int, tag: String): CSV = writeLong(i.asInstanceOf[Long], tag)

  override def writeLong(l: Long, tag: String): CSV = writeLong(l, tag)

  override def writeFloat(f: Float, tag: String): CSV = writeDouble(f.asInstanceOf[Double], tag)

  override def writeDouble(d: Double, tag: String): CSV = s"$d"

  override def writeString(s: String, tag: String): CSV = Utils.toCSVString(s)

  override def writeBuffer(buf: Array[Byte], tag: String): CSV = Utils.toCSVBuffer(buf)

  // all work is done here...
  override def writeRecord(r: Record, tag: String): CSV = s"s{${r.serialize(this,tag)}}"

  override def writeVector(v: Vector[_], tag: String): String = s"v{$v}"

  override def writeMap(m: Map[_, _], tag: String): CSV = s"m{$m}"

}

object CsvOutputArchive {
  val LOG = Logger(classOf[CsvOutputArchive])

  def getArchive(strm:OutputStream): CsvOutputArchive = new CsvOutputArchive(strm)


}
