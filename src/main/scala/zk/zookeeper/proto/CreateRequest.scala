package zk.zookeeper.proto

import zk.jute.{InputArchive, OutputArchive, Record}
import zk.zookeeper.data.ACL

case class CreateRequest(path:String, data:Array[Byte], acl:Vector[ACL], flags:Int) extends Record {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 33 * path.hashCode + data.hashCode() + acl.hashCode() + flags

  override def serialize(archive: OutputArchive, tag: String): Unit = ???

  override def deserialize(archive: InputArchive, tag: String): Unit = ???
}

