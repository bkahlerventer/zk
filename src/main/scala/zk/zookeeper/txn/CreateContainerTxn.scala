package zk.zookeeper.txn

import zk.zookeeper.data.ACL

case class CreateContainerTxn(path:String, data:Array[Byte], acl:Vector[ACL], parentCVersion:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateContainerTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateContainerTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + parentCVersion
}
