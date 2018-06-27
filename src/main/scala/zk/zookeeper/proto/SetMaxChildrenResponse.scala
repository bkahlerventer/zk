package zk.zookeeper.proto

case class SetMaxChildrenResponse(path:String, max:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetMaxChildrenResponse]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetMaxChildrenResponse => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 27 * max + path.hashCode
}
