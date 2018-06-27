package zk.zookeeper.proto

case class SetSASLRequest(token:Array[Byte]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetSASLRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetSASLRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 9 * token.hashCode()
}
