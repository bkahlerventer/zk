package zk.zookeeper.proto

import zk.zookeeper.data.Stat

case class GetDataResponse(data:Array[Byte], stat:Stat) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetDataResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetDataResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 71 * data.length + stat.hashCode
}