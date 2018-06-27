package zk.zookeeper.proto

case class ExistsRequest(path:String, watch:Boolean) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ExistsRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ExistsRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 21 * path.hashCode
}