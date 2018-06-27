package zk.zookeeper.server.quorum

case class LearnerInfo(serverId:Long, protocolVersion:Int, configVersion:Long) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[LearnerInfo]
  override def equals(that: scala.Any): Boolean = that match {
    case that: LearnerInfo => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 17 * protocolVersion + serverId.toInt + configVersion.toInt
}
