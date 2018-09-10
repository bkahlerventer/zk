package zk.zookeeper.proto

import java.io.{ByteArrayOutputStream, DataInput, DataOutput}

import com.typesafe.scalalogging.Logger
import zk.io._

class RequestHeader(_Xid:Int, _Ztype:Int) extends Record {

  import RequestHeader._

  private[this] var _xid: Int = _Xid

  private def xid: Int = _xid

  private def xid_=(value: Int): Unit = {
    _xid = value
  }

  private[this] var _ztype: Int = _Ztype

  private def ztype: Int = _ztype

  private def ztype_=(value: Int): Unit = {
    _ztype = value
  }

  def canEqual(that: Any): Boolean = that.isInstanceOf[RequestHeader]

  override def equals(that: scala.Any): Boolean = that match {
    case that: RequestHeader => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int =  31 * ztype + xid

  override def serialize(archive: OutputArchive, tag: String): Unit = {
    archive.startRecord(this,tag)
    archive.writeInt(xid,"xid")
    archive.writeInt(ztype, "type")
    archive.endRecord(this,tag)
  }

  override def deserialize(archive: InputArchive, tag: String): Unit = {
    archive.startRecord(tag)
    xid = archive.readInt("xid")
    ztype = archive.readInt("type")

  }

  override def toString: Option[String] = {
    val s = new ByteArrayOutputStream()
    var a = new CsvOutputArchive(s)
    a.startRecord(this, "")
    a.writeInt(xid,"xid")
    a.writeInt(ztype,"type")
    a.endRecord(this,"")
    Some(new String(s.toByteArray, "UTF-8"))
  }

  def write(out: DataOutput):Unit = serialize(new BinaryOutputArchive(out), "")

  def readFields(in: DataInput): Unit = deserialize(new BinaryInputArchive(in), "")

  def compareTo(that: Any): Option[Int] = that match {
    case t: RequestHeader =>
        LOG.info("Comparing different types of records")
        if(!canEqual(that)) return None
        val r1: Int = if(xid == t.xid) 0 else if(xid < t.xid) -1 else 1
        if(r1 != 0) return Option(r1)
        Option(if(ztype == t.ztype) 0 else if(ztype < t.ztype) -1 else 1)
    case _ => None
  }

  def signature = "LRequestHeader(ii)"
}

object RequestHeader {
  val LOG = Logger(classOf[RequestHeader])
}

