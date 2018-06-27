package zk.zookeeper.txn

import zk.zookeeper.data.ACL

case class SetACLTxn(path:String, acl:Vector[ACL], version:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetACLTxn]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetACLTxn => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + version + path.hashCode
}
