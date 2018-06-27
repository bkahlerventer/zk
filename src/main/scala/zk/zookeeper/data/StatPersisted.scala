package zk.zookeeper.data

import java.io.{DataInput, DataOutput}

import zk.jute.{InputArchive, OutputArchive, Record}
import zk.zookeeper.Jute

case class StatPersisted(czxid:Long, mzxid:Long, ctime:Long, mtime:Long, version:Int, cversion:Int, aversion:Int, ephemeralOwner:Long, pzxid:Long) extends Jute with Record {
  override def write(out: DataOutput): Unit = ???

  override def readFields(in: DataInput): Unit = ???

  override def compareTo(that: Any): Int = ???

  override def signature: String = ???

  override def serialize(archive: OutputArchive, tag: String): Unit = ???

  override def deserialize(archive: InputArchive, tag: String): Unit = ???
}