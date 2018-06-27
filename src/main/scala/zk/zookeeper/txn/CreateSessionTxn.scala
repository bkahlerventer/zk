package zk.zookeeper.txn

case class CreateSessionTxn(timeOut:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateSessionTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateSessionTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 16457 * timeOut
}