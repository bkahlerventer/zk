package zk.zookeeper.proto

case class AuthPacket(ztype:Int, scheme:String, auth:Array[Byte]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[AuthPacket]
  override def equals(that: scala.Any): Boolean = that match {
    case that: AuthPacket => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 33 * ztype + scheme.hashCode
}