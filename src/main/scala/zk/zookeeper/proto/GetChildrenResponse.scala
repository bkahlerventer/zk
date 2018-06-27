package zk.zookeeper.proto

case class GetChildrenResponse(children:Vector[String]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildrenResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetChildrenResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = children.toList.map(s => s.hashCode).sum
}
