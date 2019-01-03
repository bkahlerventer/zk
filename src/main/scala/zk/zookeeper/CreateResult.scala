package zk.zookeeper

import zk.zookeeper.data.Stat

/**
  * A result from a create operation.  This kind of result allows the path to be retrieved since the create
  * might have been a sequential create
  *
  * @param path zookeeper path
  * @param stat Stat structure
  * @param opcode type of create result
  */
case class CreateResult(path: String, stat: Option[Stat], opcode: Int) extends OpResult(opcode) {
  def this(path: String) = this(path, None, ZooDefs.OpCode.create)
  def this(path: String, stat: Option[Stat]) = this (path, stat, ZooDefs.OpCode.create2)

  override def getType: Int = if (stat.isEmpty && (opcode == ZooDefs.OpCode.create2)) ZooDefs.OpCode.create else opcode

  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateResult]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateResult =>
      that.canEqual(this) && this.hashCode == that.hashCode && this.stat == that.stat
    case _ => false
  }

  override def hashCode(): Int = getType * 35 + path.hashCode + (if (stat.isEmpty) 0 else stat.hashCode())
}
