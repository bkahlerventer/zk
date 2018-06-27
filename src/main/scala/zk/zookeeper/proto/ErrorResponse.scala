package zk.zookeeper.proto

case class ErrorResponse(err:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ErrorResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ErrorResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 31 * err
}