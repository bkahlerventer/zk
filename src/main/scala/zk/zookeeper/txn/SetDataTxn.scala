package zk.zookeeper.txn

case class SetDataTxn(path:String, data:Array[Byte], version:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetDataTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 90 * version + path.hashCode + data.hashCode()
}