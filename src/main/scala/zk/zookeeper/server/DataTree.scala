package zk.zookeeper.server

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class DataTree {
  import DataTree._

}

object DataTree {
  val LOG = Logger(LoggerFactory.getLogger(DataTree.getClass))

  val rootZookeeper:String = "/"
  val procZookeeper:String = Quotas.procZookeeper
  val procChildZookeeper:String = procZookeeper.substring(1)

}