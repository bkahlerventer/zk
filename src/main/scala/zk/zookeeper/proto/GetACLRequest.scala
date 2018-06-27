package zk.zookeeper.proto

case class GetACLRequest(path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetACLRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetACLRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 17 * path.hashCode
}