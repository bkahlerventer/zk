package zk.zookeeper.proto

case class DeleteRequest(path:String, version:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[DeleteRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: DeleteRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 441 * version + path.hashCode
}