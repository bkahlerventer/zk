package zk.zookeeper.txn

case class TxnHeader(clientId:Long, cxid:Int, zxid:Long, time:Long, ztype:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[TxnHeader]
  override def equals(that: scala.Any): Boolean = that match {
    case that: TxnHeader => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 67 * clientId.toInt + cxid + zxid.toInt + time.toInt + ztype
}
