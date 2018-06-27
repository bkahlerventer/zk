package zk.zookeeper.txn

case class CheckVersionTxn(path:String, version:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckVersionTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CheckVersionTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 99 * version + path.hashCode
}