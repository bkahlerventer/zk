package zk.zookeeper.data

import zk.zookeeper.Jute

case class StatPersisted(czxid:Long, mzxid:Long, ctime:Long, mtime:Long, version:Int, cversion:Int, aversion:Int, ephemeralOwner:Long, pzxid:Long) extends Jute {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[StatPersisted]
  override def equals(that: scala.Any): Boolean = that match {
    case that: StatPersisted => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 35 + mzxid.toInt + czxid.toInt + ctime.toInt
}