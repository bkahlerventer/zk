package zk.zookeeper.proto

import zk.zookeeper.data.ACL

case class CreateTTLRequest(path:String, data:Array[Byte], acl:Vector[ACL], flags:Int, ttl:Long) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTTLRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateTTLRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 113 * path.hashCode + flags + ttl.toInt
}