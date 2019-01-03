package zk.zookeeper

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
