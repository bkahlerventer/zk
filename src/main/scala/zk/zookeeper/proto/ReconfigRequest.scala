package zk.zookeeper.proto

case class ReconfigRequest(joiningServers:String, leavingServers:String, newMembers:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ReconfigRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ReconfigRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 1024 + joiningServers.hashCode + leavingServers.hashCode + newMembers.hashCode
}