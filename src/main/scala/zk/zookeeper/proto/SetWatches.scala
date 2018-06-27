package zk.zookeeper.proto

case class SetWatches(relativeZxid:Long, dataWatches:Vector[String], existWatches:Vector[String], childWatches:Vector[String]) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetWatches]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetWatches => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int =  31 * relativeZxid.toInt
}