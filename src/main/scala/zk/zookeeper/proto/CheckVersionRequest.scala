package zk.zookeeper.proto

case class CheckVersionRequest(path:String, version:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckVersionRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CheckVersionRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 77 * version + path.hashCode
}