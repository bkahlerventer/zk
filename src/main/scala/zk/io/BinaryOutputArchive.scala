package zk.io

import java.io.{DataOutput, DataOutputStream, OutputStream}
import java.nio.ByteBuffer


class BinaryOutputArchive(out:DataOutput) extends OutputArchive {
  import BinaryOutputArchive._

  private val bb = ByteBuffer.allocate(1024)

  def writeByte(b: Byte, tag: String):Unit = out.writeByte(b)
  def writeBool(b: Boolean, tag: String): Unit = out.writeBoolean(b)
  def writeInt(i: Int, tag: String): Unit = out.writeInt(i)
  def writeLong(l: Long, tag: String): Unit = out.writeLong(l)
  def writeFloat(f: Float, tag: String): Unit = out.writeFloat(f)
  def writeDouble(d: Double, tag: String): Unit = out.writeDouble(d)

  def writeString(s: String, tag: String): Unit = {
    def charToUTF8Array(c: Char): Array[Byte] = {
      if (c < 0x80) Array(c.toByte)
      else if (c < 0x800) Array((0xc0 | (c >> 6)).toByte, (0x80 | (c & 0x3f)).toByte)
      else Array((0xe0 | (c >> 12)).toByte, (0x80 | (c >> 6)).toByte, (0x80 | (c & 0x3f)).toByte)
    }

    s match {
      case null => writeInt(-1, "len")
      case _ =>
        val a = (for(c <- s) yield charToUTF8Array(c)).flatten.toArray
        writeInt(a.length,"len")
        out.write(a,0,a.length)
    }
  }

  def writeBuffer(buf: Array[Byte], tag: String): Unit = buf match {
    case null => out.writeInt(-1)
    case _ =>
      out.writeInt(buf.length)
      out.write(buf)
  }

  def writeRecord(r: Record, tag: String): Unit = r.serialize(this, tag)


  override def startVector(v: Vector[_], tag: String): Unit = v match {
    case null => writeInt(-1, tag)
    case _ => writeInt(v.length, tag)
  }

  override def endVector(v: Vector[_], tag: String): Unit = {}
  override def startMap(m: Map[_, _], tag: String): Unit = writeInt(m.size, tag)
  override def endMap(m: Map[_, _], tag: String): Unit = {}

  override def startRecord(r: Record, tag: String): Boolean = ???

  override def endRecord(r: Record, tag: String): Boolean = ???
}

object BinaryOutputArchive  {
  def getArchive(strm:OutputStream):BinaryOutputArchive = new BinaryOutputArchive(new DataOutputStream(strm))

}
