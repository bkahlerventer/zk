package zk.zookeeper.proto

import zk.io.{InputArchive, OutputArchive, Record}

case class ConnectResponse(protocolVersion:Int, timeOut:Int, sessionId:Long, passwd:Array[Byte]) extends Record {




  override def canEqual(that: Any): Boolean = that.isInstanceOf[ConnectResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ConnectResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 35 * timeOut + sessionId.toInt

  override def serialize(archive: OutputArchive, tag: String): Unit = ???

  override def deserialize(archive: InputArchive, tag: String): Unit = ???
}
