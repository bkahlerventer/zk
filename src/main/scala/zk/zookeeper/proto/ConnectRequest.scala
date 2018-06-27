package zk.zookeeper.proto

case class ConnectRequest(protocolVersion:Int, lastZxidSeen:Long, timeOut:Int, sessionId:Long, passwd:Array[Byte]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ConnectRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ConnectRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 31 * timeOut + sessionId.toInt
}
