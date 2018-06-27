package zk.zookeeper.proto

case class GetSASLRequest(token:Array[Byte]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetSASLRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetSASLRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 7 * token.hashCode()
}