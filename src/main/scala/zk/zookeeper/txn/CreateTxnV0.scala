package zk.zookeeper.txn

import zk.zookeeper.data.ACL

case class CreateTxnV0(path:String, data:Array[Byte], acl:Vector[ACL]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTxnV0]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateTxnV0 => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + path.hashCode + data.hashCode()
}
