package zk.zookeeper.admin

import com.typesafe.scalalogging.Logger
import zk.zookeeper.client.ZKClientConfig
import zk.zookeeper.{MultiTransactionRecord, OpResult, Watcher, ZooKeeper}

class ZooKeeperAdmin(connectionString:String, sessionTimeout:Int, watcher:Watcher) extends ZooKeeper(connectionString,sessionTimeout,watcher) {
  override def multiInternal(request: MultiTransactionRecord): List[OpResult] = ???

  this(connectionString:String, sessionTimeout:Int, watcher:Watcher, conf:ZKClientConfig) = ???
}

object ZooKeeperAdmin {
  val LOG  = Logger(classOf[ZooKeeperAdmin])

}