package zk.zookeeper.proto

case class RemoveWatchesRequest(path:String, ztype:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[RemoveWatchesRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: RemoveWatchesRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 77 * ztype + path.hashCode
}