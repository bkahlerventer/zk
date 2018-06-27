package zk.zookeeper.proto

case class CheckWatchesRequest(path:String, ztype:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckWatchesRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CheckWatchesRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 55 * ztype + path.hashCode
}