package zk.zookeeper

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
