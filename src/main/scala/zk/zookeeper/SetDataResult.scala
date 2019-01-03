package zk.zookeeper

import zk.zookeeper.data.Stat

/**
  * A result from a setData operation.  This kind of result provides access to the Stat structure from
  * the update.
  *
  * @param stat Stat Structure
  */
case class SetDataResult(stat: Stat) extends OpResult(ZooDefs.OpCode.setData) {
  def getStat: Stat = stat
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataResult]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetDataResult =>
      that.canEqual(this) && this.hashCode == that.hashCode && this.stat == that.stat
    case _ => false
  }
  override def hashCode(): Int = getType * 35 + stat.mzxid.toInt
}
