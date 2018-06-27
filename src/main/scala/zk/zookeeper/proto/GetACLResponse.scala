package zk.zookeeper.proto

import zk.zookeeper.data.{ACL, Stat}

case class GetACLResponse(acl:Vector[ACL], stat:Stat) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetACLResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetACLResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = acl.toList.map(a => a.hashCode()).sum + stat.hashCode()
}