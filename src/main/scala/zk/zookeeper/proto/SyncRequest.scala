package zk.zookeeper.proto

case class SyncRequest(path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SyncRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SyncRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 18 * path.hashCode
}