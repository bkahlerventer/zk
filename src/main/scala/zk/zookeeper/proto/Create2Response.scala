package zk.zookeeper.proto

import zk.zookeeper.data.Stat

case class Create2Response(path:String, stat:Stat) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[Create2Response]
  override def equals(that: scala.Any): Boolean = that match {
    case that: Create2Response => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 3131 + path.hashCode + stat.hashCode()
}
