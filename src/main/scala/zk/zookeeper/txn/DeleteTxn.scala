package zk.zookeeper.txn

case class DeleteTxn(path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[DeleteTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: DeleteTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 45361 + path.hashCode
}
