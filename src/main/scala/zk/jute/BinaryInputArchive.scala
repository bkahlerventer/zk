package zk.jute

import java.io.{DataInput, DataInputStream, IOException, InputStream}
import java.lang.Integer.getInteger

class BinaryInputArchive(din:DataInput) extends InputArchive {
  import BinaryInputArchive.BinaryIndex

  final val UNREASONABLE_LENGTH = "Unreasonable length = "
  private val in:DataInput = din

  override def readByte(tag: String): Byte = in.readByte()
  override def readBool(tag: String): Boolean = in.readBoolean()
  override def readInt(tag: String): Int = in.readInt()
  override def readLong(tag: String): Long = in.readLong()
  override def readFloat(tag: String): Float = in.readFloat()
  override def readDouble(tag: String): Double = in.readDouble()
  override def readString(tag: String): String = {
    val len = in.readInt()
    if(len == -1)  null else {
      checkLength(len)
      val b: Array[Byte] = new Array[Byte](len)
      in.readFully(b)
      new String(b, "UTF8")
    }
  }

  val maxBuffer: Int = getInteger("jute.maxbuffer", 0xfffff)

  override def readBuffer(tag: String): Array[Byte] = {
    val len = readInt(tag)
    if(len == -1) null else {
      checkLength(len)
      val arr: Array[Byte] = new Array[Byte](len)
      in.readFully(arr)
      arr
    }
  }

  override def readRecord(r: Record, tag: String): Unit = r.deserialize(this, tag)
  override def startRecord(r: Record, tag: String): Unit = {}
  override def endRecord(tag: String): Unit = {}

  override def startVector(tag: String): Index = {
    val len = readInt(tag)
    if(len == -1) null else BinaryIndex(len)
  }

  override def endVector(tag: String): Unit = {}
  override def startMap(tag: String): Index = BinaryIndex(readInt(tag))
  override def endMap(tag: String): Unit = {}

  private def checkLength(len: Int): Unit =
    if(len < 0 || len > maxBuffer + 1024) throw  new IOException(UNREASONABLE_LENGTH + len)

}

object BinaryInputArchive {
  def getArchive(strm:InputStream):BinaryInputArchive = new BinaryInputArchive(new DataInputStream(strm))

  private case class BinaryIndex(nelems:Int) extends Index {
    private var n: Int = nelems
    override def done(): Boolean = n <= 0
    override def incr(): Unit = n-=1
  }
}

