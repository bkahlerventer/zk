package zk.zookeeper.proto

case class GetChildren2Request(path:String, watch:Boolean) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildren2Request]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetChildren2Request => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 11234 + path.hashCode
}