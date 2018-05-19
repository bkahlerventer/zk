package zk.zookeeper

import zk.zookeeper.data.Stat

abstract class OpResult(opCode:Int) {
  val getType: Int = opCode

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
    def this(path: String, stat: Option[Stat]) =

    if (stat.isEmpty) {
      this (path, None, ZooDefs.OpCode.create)
    } else {
      this (path, stat, ZooDefs.OpCode.create2)
    }

    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateResult]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateResult =>
        that.canEqual(this) && this.hashCode == that.hashCode && this.stat == that.stat
      case _ => false
    }

    override def hashCode(): Int = getType * 35 + path.hashCode + (if (stat.isEmpty) 0 else stat.hashCode())
  }

  /**
    * A result from a delete operation, No special values are available
    *
    */
  case class DeleteResult() extends OpResult(ZooDefs.OpCode.delete) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[DeleteResult]
    override def equals(that: scala.Any): Boolean = that match {
      case that: DeleteResult =>
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

    override def hashCode(): Int = getType
  }

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

  /**
    * A result from a version check operation.  No special values are available
    */
  case class CheckResult() extends OpResult(ZooDefs.OpCode.check) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckResult]

    override def equals(that: scala.Any): Boolean = that match {
      case that: CheckResult =>
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

    override def hashCode(): Int = getType
  }

  /**
    * An error result from any kind of operation.  The point of error results is that they contain an error
    * code which helps understand what happened.
    * @see KeeperException.Code
    *
    * @param err KeeperException.Code
    */
  case class ErrorResult(err:Int) extends OpResult(ZooDefs.OpCode.error) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ErrorResult]

    override def equals(that: scala.Any): Boolean = that match {
      case that:ErrorResult =>
        that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

    override def hashCode(): Int = getType * 35 + err
  }

}
