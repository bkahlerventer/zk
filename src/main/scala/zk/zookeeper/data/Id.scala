package zk.zookeeper.data

import zk.jute.Record
import zk.zookeeper.Jute

case class Id(scheme:String, id:String) extends Jute with Record {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[Id]
  override def equals(that: scala.Any): Boolean = that match {
    case that: Id => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 35 + scheme.hashCode + id.hashCode
}

