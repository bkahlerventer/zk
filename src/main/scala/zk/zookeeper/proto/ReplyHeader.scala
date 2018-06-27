package zk.zookeeper.proto

case class ReplyHeader(xid:Int, zxid:Long, err:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ReplyHeader]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ReplyHeader => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 11 * zxid.toInt + xid + err
}
