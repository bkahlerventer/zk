package zk.zookeeper.txn

case class ErrorTxn(err:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ErrorTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ErrorTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 6778 * err
}