package zk.zookeeper.proto

case class ConnectResponse(protocolVersion:Int, timeOut:Int, sessionId:Long, passwd:Array[Byte]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[ConnectResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: ConnectResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 35 * timeOut + sessionId.toInt
}
