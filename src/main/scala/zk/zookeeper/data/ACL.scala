package zk.zookeeper.data

import java.io.{DataInput, DataOutput}

import zk.jute.{InputArchive, OutputArchive, Record}
import zk.zookeeper.Jute

case class ACL(perms:Int, id:Id) extends Jute with Record {

  override def serialize(archive: OutputArchive, tag: String): Unit = ???

  override def deserialize(archive: InputArchive, tag: String): Unit = ???

  override def toString: String = ???

  override def write(out: DataOutput): Unit = ???

  override def readFields(in: DataInput): Unit = ???

  override def compareTo(that: Any): Int = ???

  override def signature: String = ???
}
