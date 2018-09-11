package zk.zookeeper.proto

import java.io.{ByteArrayOutputStream, DataInput, DataOutput, IOException}

import zk.io._

class AuthPacket(_type:Int, _scheme:String, _auth:Array[Byte]) extends Record {
  import AuthPacket._

  private var ztype:Int = _type
  private var scheme:String = _scheme
  private var auth:Array[Byte] = _auth


  override def serialize(archive: OutputArchive, tag: String): Unit = {
    archive.startRecord(this,tag)
    archive.writeInt(ztype,"type")
    archive.writeString(scheme,"scheme")
    archive.writeBuffer(auth,"auth")
    archive.endRecord(this,tag)
  }

  override def deserialize(archive: InputArchive, tag: String):Unit = {
    archive.startRecord(tag)
    ztype = archive.readInt("type")
    scheme = archive.readString("scheme")
    auth = archive.readBuffer("auth")
    archive.endRecord(tag)
  }

  override def toString: String = {
    try {
      val s = new ByteArrayOutputStream()
      val a = new CsvOutputArchive(s)
      a.startRecord(this, "")
      a.writeInt(ztype, "type")
      a.writeString(scheme, "scheme")
      a.writeBuffer(auth,"auth")
      a.endRecord(this, "")
      return new String(s.toByteArray,"UTF-8")

    } catch {
      case ex:Throwable =>
        ex.printStackTrace()
    }
    "ERROR"
  }

  @throws(classOf[IOException])
  def write(out:DataOutput):Unit = serialize(new BinaryOutputArchive(out), "")


  def readFields(in:DataInput):Unit = deserialize(new BinaryInputArchive(in), "")

  def compareTo(that:Any): Int = that match {
    case t: AuthPacket =>
      val ret1:Int = if(ztype == t.ztype) 0 else if (ztype < t.ztype) -1 else 1
      if (ret1 != 0) return ret1
      val ret2:Int = scheme.compareTo(t.scheme)
      if(ret2 != 0) return ret2
      Utils.compareBytes(auth,0,auth.length,t.auth,0,t.auth.length)
    case _ =>
      throw new ClassCastException("comparing different types of records")
  }

  def canEqual(that: Any): Boolean = that.isInstanceOf[AuthPacket]

  override def equals(that: scala.Any): Boolean = that match {
    case that: AuthPacket => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 17 + (37 * ztype) + (37 * scheme.hashCode) + (37 * auth.toString.hashCode)

  val signature = "LAuthPacket(isB)"

}

object AuthPacket {

}
