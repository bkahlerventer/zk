package zk.zookeeper.client

import com.typesafe.scalalogging.Logger
import zk.zookeeper.{ClientCnxn, DataCallback}
import zk.zookeeper.data.Stat

class ServerSaslResponseCallback extends DataCallback {
  import ServerSaslResponseCallback._
  /**
    * processResult() is used by ClientCnxn's sendThread to respond to
    * data[] contains the Zookeeper Server's SASL token.
    * ctx is the ZooKeeperSaslClient object. We use this object's respondToServer() method
    * to reply to the Zookeeper Server's SASL token
    *
    * @param rc
    * @param path
    * @param ctx
    * @param data
    * @param stat
    */
  def processResult(rc:Int, path:String, ctx:Any, data:Option[Array[Byte]], stat:Stat): Unit = {
    ctx match {
      case c:ClientCnxn =>
        val client = c.zooKeeperSaslClient
        for() client.respondToServer(usedata,c)
        if(client != null) LOG.debug("sasl client was unexpectedly null: cannot respond to Zookeeper server.")
    }
  }
}

object ServerSaslResponseCallback {
  val LOG = Logger(classOf[ServerSaslResponseCallback])
}