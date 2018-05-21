package zk.zookeeper.client

import zk.zookeeper.common.ZKConfig

class ZKClientConfig extends ZKConfig {

}

object ZKClientConfig {
  val ZK_SASL_CLIENT_USERNAME = "zookeeper.sasl.client.username"
  val ZK_SASL_CLIENT_USERNAME_DEFAULT = "zookeeper"
  @Deprecated
  val LOGIN_CONTEXT_NAME_KEY = ZooKeeperSaslClient.LOGIN_CONTEXT_NAME_KEY
  val LOGIN_CONTEXT_NAME_KEY_DEFAULT = "Client"
  @Deprecated
  val ENABLE_CLIENT_SASL_KEY = ZooKeeperSaslClient.ENABLE_CLIENT_SASL_KEY
  @Deprecated
  val ENABLE_CLIENT_SASL_DEFAULT = ZooKeeperSaslClient.ENABLE_CLIENT_SASL_DEFAULT
  val ZOOKEEPER_SERVER_REALM = "zookeeper.server.realm"
  val DISABLE_AUTO_WATCH_RESET = "zookeeper.disableAutoWatchReset"
  @Deprecated
  val ZOOKEEPER_CLIENT_CNXN_SOCKET = ZooKeeper.ZOOKEEPER_CLIENT_CNXN_SOCKET
  @Deprecated
  val SECURE_CLIENT = ZooKeeper.SECURE_CLIENT
  val CLIENT_MAX_PACKET_LENGTH_DEFAULT:Int = 4096 * 1024 // 4MB

}