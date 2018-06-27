package zk.zookeeper.proto

case class SyncResponse(path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SyncResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SyncResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 15 * path.hashCode
}
