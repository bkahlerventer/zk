package zk.zookeeper.proto

import zk.zookeeper.data.Stat

case class SetDataResponse(stat:Stat) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetDataResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = stat.hashCode()
}