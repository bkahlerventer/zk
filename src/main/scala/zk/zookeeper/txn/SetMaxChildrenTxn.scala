package zk.zookeeper.txn

case class SetMaxChildrenTxn(path:String, max:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetMaxChildrenTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetMaxChildrenTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 13 * max + path.hashCode
}