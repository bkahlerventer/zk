package zk.zookeeper

import zk.zookeeper.data.Stat

abstract class OpResult(opCode:Int) {
  def getType: Int = opCode
}

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

}

/**
  * A result from a delete operation, No special values are available
  *
  */
case class DeleteResult() extends OpResult(ZooDefs.OpCode.delete)

/**
  * A result from a setData operation.  This kind of result provides access to the Stat structure from
  * the update.
  *
  * @param stat Stat Structure
  */
case class SetDataResult(stat: Stat) extends OpResult(ZooDefs.OpCode.setData) {
  def getStat: Stat = stat
}

/**
  * A result from a version check operation.  No special values are available
  */
case class CheckResult() extends OpResult(ZooDefs.OpCode.check)

/**
  * An error result from any kind of operation.  The point of error results is that they contain an error
  * code which helps understand what happened.
  * @see KeeperException.Code
  *
  * @param err KeeperException.Code
  */
case class ErrorResult(err:Int) extends OpResult(ZooDefs.OpCode.error)