package zk.zookeeper.proto

case class GetMaxChildrenResponse(max:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetMaxChildrenResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetMaxChildrenResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 312 * max
}