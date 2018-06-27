package zk.zookeeper.data

  case class Stat(czxid:Long, mzxid:Long, ctime:Long, mtime:Long, version:Int, cversion:Int, aversion:Int, ephemeralOwner:Long, dataLength:Int, numChildren:Int, pzxid:Long) extends Jute {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[Stat]
    override def equals(that: scala.Any): Boolean = that match {
      case that: Stat => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 35  + mzxid.toInt + czxid.toInt + ctime.toInt
  }