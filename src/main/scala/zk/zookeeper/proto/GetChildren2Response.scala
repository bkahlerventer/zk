package zk.zookeeper.proto

case class GetChildren2Response(children:Vector[String]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildren2Response]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetChildren2Response => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = children.toList.map(s => s.hashCode).sum
}