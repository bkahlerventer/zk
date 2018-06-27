package zk.zookeeper.proto

case class WatcherEvent(ztype:Int, state:Int, path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[WatcherEvent]
  override def equals(that: scala.Any): Boolean = that match {
    case that: WatcherEvent => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = path.hashCode + ztype + state
}