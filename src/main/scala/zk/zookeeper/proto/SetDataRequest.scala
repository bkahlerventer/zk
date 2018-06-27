package zk.zookeeper.proto

case class SetDataRequest(path:String, data:Array[Byte], version:Int) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataRequest]
  override def equals(that: scala.Any): Boolean = that match {
    case that: SetDataRequest => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 172 + path.hashCode + version
}