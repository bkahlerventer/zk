package zk.zookeeper.proto

case class RequestHeader(xid:Int, ztype:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[RequestHeader]
  override def equals(that: scala.Any): Boolean = that match {
    case that: RequestHeader => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int =  31 * ztype + xid
}