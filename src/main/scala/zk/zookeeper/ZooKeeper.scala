package zk.zookeeper

import com.typesafe.scalalogging.Logger
import zk.zookeeper.client.ZooKeeperSaslClient
import org.slf4j.LoggerFactory
import zk.zookeeper.ZooKeeper.ZKWatchManager
import zk.zookeeper.client.{ConnectStringParser, HostProvider, StaticHostProvider, ZKClientConfig}


// String,Int,Watcher,Boolean,HostProvider,Option[ZKClientConfig]
class ZooKeeper(connectString:String, sessionTimeout:Int, watcher:Watcher,
                canBeReadOnly:Boolean, aHostProvider:HostProvider, conf:Option[ZKClientConfig]) extends AutoCloseable {
  import ZooKeeper.{createDefaultHostProvider,LOG,ZKWatchManager}

  @Deprecated
  val ZOOKEEPER_CLIENT_CNXN_SOCKET = "zookeeper.clientCnxnSocket"
  @Deprecated
  val SECURE_CLIENT = "zookeeper.client.secure"


  private val watchManager = defaultWatchManager
  private val clientConfig = conf.getOrElse(new ZKClientConfig)
  private val connectStringParser = new ConnectStringParser
  watchManager.defaultWatcher = watcher
  protected val cnxn:ClientCnxn = new ClientCnxn(connectStringParser.getChrootPath(),
    aHostProvider, sessionTimeout, this, watchManager,
    getClientCnxnSocket(), canBeReadOnly)

  protected var defaultWatcher:Watcher

  def updateServerList(connectionString:String):Unit = ???

  def getSaslClient: ZooKeeperSaslClient = cnxn.zooKeeperSaslClient

  def getClientConfig:ZKClientConfig = clientConfig

  def getDataWatches: List[String] = ???

  def getExistWatches: List[String] = ???

  def getChildWatches: List[String] = ???




  // 1. String, Int, Watcher, Boolean, HostProvider
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean, aHostProvider:HostProvider) = {
    // 0. String,Int,Watcher,Boolean,HostProvider,*
    this(connectString,sessionTimeout,watcher,canBeReadOnly,aHostProvider,None)
  }
  // 2. String,Int,Watcher,Boolean
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean) = {
    // 1. String,Int,Watcher,Boolean,HostProvider
    this(connectString,sessionTimeout,watcher,canBeReadOnly,createDefaultHostProvider(connectString))
  }
  // 3. String,Int,Watcher,Boolean,ZkClientConfig
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean, conf:Option[ZKClientConfig]) = {
    // String,Int,Watcher,Boolean,HostProvider,ZKClientConf
    this(connectString,sessionTimeout,watcher,canBeReadOnly,createDefaultHostProvider(connectString), conf)
  }
  // 4. String,Int,Watcher
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher) = {
    // String,Int,Watcher,Boolean
    this(connectString,sessionTimeout,watcher,false)
  }
  // 5. String,Int,Watcher,ZKClientConfig
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, conf:Option[ZKClientConfig]) = {
    // String,Int,Watcher,Boolean,ZKClientConfig
    this(connectString,sessionTimeout,watcher,false,conf)
  }
  // 6. String,Int,Watcher,Long,Array[Byte],Boolean,HostProvider
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Long, sessionPasswd:Array[Byte], canBeReadOnly:Boolean, aHostProvider:HostProvider) = ???
  // 7. String,Int,Watcher,Long,Array[Byte],Boolean
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Long, sessionPasswd:Array[Byte], canBeReadOnly:Boolean) = {
    this(connectString,sessionTimeout,watcher,sessionId,sessionPasswd,canBeReadOnly,createDefaultHostProvider(connectString))
  }
  // 8. String,Int,Watcher,Long,Array[Byte]
  def this(connectionString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Long, sessionPasswd:Array[Byte]) = {
    // String,Int,Watcher,Long,Array[Byte],Boolean
    this(connectionString,sessionTimeout,watcher,sessionId,sessionPasswd,false)
  }

  protected def defaultWatchManager:ZKWatchManager = ???

  override def close(): Unit = ???
}

object ZooKeeper {
  val LOG = Logger(LoggerFactory.getLogger(ZooKeeper.getClass))
  Environment.logEnv("Client environment:", LOG)

  // TODO - implement protocols to update variables in this case class
  case class ZKWatchManager(disableAutoWatchReset:Boolean) extends ClientWatchManager {
    type WatchMap =
    private var dataWatches = scala.collection.mutable.Map[String, Set[Watcher]]()
    private var existWatches = scala.collection.mutable.Map[String, Set[Watcher]]()
    private var childWatches = scala.collection.mutable.Map[String, Set[Watcher]]()
    // val disableAutoWatchReset - added by constructor above

    private var watchManager = ZKWatchManager(false)

    // modifier for watchManager
    def watchManager_=(zkwm:ZKWatchManager):Unit = watchManager = zkwm

    private def addTo(from:Set[Watcher], to:Set[Watcher]):Unit = {
      if(from != null) to.addAll(from)
    }

    def removeWatcher

  }

  def createDefaultHostProvider(connectString:String):HostProvider = {
    new StaticHostProvider(new ConnectStringParser(connectString).getServerAddresses)
  }
}
