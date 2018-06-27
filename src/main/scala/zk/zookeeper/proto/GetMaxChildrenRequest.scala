package zk.zookeeper.proto

case class GetMaxChildrenRequest(path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetMaxChildrenRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetMaxChildrenRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 27513 + path.hashCode
}