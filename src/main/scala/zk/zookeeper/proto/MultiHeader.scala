package zk.zookeeper.proto

case class MultiHeader(ztype:Int, done:Boolean, err:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[MultiHeader]
  override def equals(that: scala.Any): Boolean = that match {
    case that: MultiHeader => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 35 * ztype + err
}