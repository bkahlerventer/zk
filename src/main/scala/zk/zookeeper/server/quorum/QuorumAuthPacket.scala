package zk.zookeeper.server.quorum

case class QuorumAuthPacket(magic:Int, version:Int, dbid:Long) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[QuorumAuthPacket]
  override def equals(that: scala.Any): Boolean = that match {
    case that: QuorumAuthPacket => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 81 * magic + version + dbid.toInt
}
