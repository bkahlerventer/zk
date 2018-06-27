package zk.zookeeper.proto

case class GetDataRequest(path:String, watch:Boolean) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[GetDataRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: GetDataRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 177 + path.hashCode
}
