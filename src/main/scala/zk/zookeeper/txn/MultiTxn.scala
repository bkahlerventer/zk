package zk.zookeeper.txn

case class MultiTxn(txns:Vector[Txn]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[MultiTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: MultiTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = txns.toList.map(s => s.hashCode()).sum
}