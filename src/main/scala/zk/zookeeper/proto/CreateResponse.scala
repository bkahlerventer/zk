package zk.zookeeper.proto

case class CreateResponse(path:String) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: CreateResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 7644 + path.hashCode
}