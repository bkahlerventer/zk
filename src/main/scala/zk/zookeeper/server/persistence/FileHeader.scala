package zk.zookeeper.server.persistence

case class FileHeader(magic:Int, version:Int, dbid:Long) {
  override def canEqual(that: Any): Boolean = that.isInstanceOf[FileHeader]
  override def equals(that: scala.Any): Boolean = that match {
    case that: FileHeader => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode(): Int = 98 * magic + version + dbid.toInt
}
