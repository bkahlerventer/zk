package zk.zookeeper.txn

import zk.zookeeper.data.ACL

case class CreateTTLTxn(path:String, data:Array[Byte], acl:Vector[ACL], parentCVersion:Int, ttl:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTTLTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateTTLTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + path.hashCode + parentCVersion + ttl
}
