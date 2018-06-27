package zk.zookeeper.proto

import zk.zookeeper.data.Stat

case class ExistsResponse(stat:Stat) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ExistsResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ExistsResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 89898 + stat.hashCode
}