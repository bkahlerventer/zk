package zk.zookeeper.client

import com.typesafe.scalalogging.Logger

class ZooKeeperSaslClient {

}

object ZooKeeperSaslClient {
  val LOG = Logger(classOf[ZooKeeperSaslClient])
  @deprecated
  val LOGIN_CONTEXT_NAME_KEY = "zookeeper.sasl.clientconfig"
  @deprecated
  val ENABLE_CLIENT_SASL_KEY = "zookeeper.sasl.client"
  @deprecated
  val ENABLE_CLIENT_SASL_DEFAULT = "true"

}