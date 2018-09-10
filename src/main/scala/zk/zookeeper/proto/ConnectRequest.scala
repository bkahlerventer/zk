package zk.zookeeper.proto

import java.io.{DataInput, DataOutput}

import zk.io.{InputArchive, OutputArchive, Record}
import zk.zookeeper.Jute

case class ConnectRequest(protocolVersion:Int, lastZxidSeen:Long, timeOut:Int, sessionId:Long, passwd:Array[Byte]) extends Jute with Record {
  override def write(out: DataOutput): Unit = ???

  override def readFields(in: DataInput): Unit = ???

  override def compareTo(that: Any): Int = ???

  override def signature: String = ???

  override def serialize(archive: OutputArchive, tag: String): Unit = ???

  override def deserialize(archive: InputArchive, tag: String): Unit = ???
}

