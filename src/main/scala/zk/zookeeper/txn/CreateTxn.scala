package zk.zookeeper.txn

import zk.zookeeper.data.ACL

case class CreateTxn(path:String, data:Array[Byte], acl:Vector[ACL]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + path.hashCode + data.hashCode()
}