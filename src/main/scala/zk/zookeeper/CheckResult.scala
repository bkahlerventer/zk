package zk.zookeeper


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

