package zk.zookeeper.server.quorum

import zk.zookeeper.data.Id

case class QuorumPacket(ztype:Int, zxid:Long, data:Array[Byte], authinfo:Vector[Id]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[QuorumPacket]
  override def equals(that: scala.Any): Boolean = that match {
    case that: QuorumPacket => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 71 * ztype + data.hashCode()
}