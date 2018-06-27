package zk.zookeeper.txn

case class Txn(ztype:Int, data:Array[Byte]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[Txn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: Txn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 6678 * ztype + data.hashCode()
}