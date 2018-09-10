package zk.zookeeper.client

import com.typesafe.scalalogging.Logger
import zk.zookeeper.ClientCnxn
import zk.zookeeper.data.Stat

class ServerSaslResponseCallback {
  def processResult(rc:Int, path:String, ctx:Any, data:Option[Array[Byte]], stat:Stat): Unit = {
    ctx match {
      case c:ClientCnxn =>
        val client = x.
    }
  }
}

object ServerSaslResponseCallback {
  val LOG = Logger(classOf[ServerSaslResponseCallback])
}