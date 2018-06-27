package zk.zookeeper.proto

case class GetChildrenRequest(path:String, watch:Boolean) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildrenRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetChildrenRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 7531 + path.hashCode
}