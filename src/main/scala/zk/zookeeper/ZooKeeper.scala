package zk.zookeeper

import java.util.concurrent.ConcurrentHashMap

import com.typesafe.scalalogging.Logger
import zk.zookeeper.client.ZooKeeperSaslClient
import org.slf4j.LoggerFactory
import zk.zookeeper.client.{ConnectStringParser, HostProvider, StaticHostProvider, ZKClientConfig}
import zk.zookeeper.Watcher.Event.{EventType, WatcherType}
import zk.zookeeper.KeeperException.Code

import scala.collection.JavaConverters._
import scala.collection.mutable.{HashMap => mHashMap, HashSet => mHashSet}

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
    type WatchMap = mHashMap[String, mHashSet[Watcher]]
    private var dataWatches = new ConcurrentHashMap[String, mHashSet[Watcher]]
    private var existWatches = new ConcurrentHashMap[String, mHashSet[Watcher]]
    private var childWatches = new ConcurrentHashMap[String, mHashSet[Watcher]]
    // val disableAutoWatchReset - added by constructor above

    private var watchManager = ZKWatchManager(false)

    // modifier for watchManager
    def watchManager_=(zkwm:ZKWatchManager):Unit = watchManager = zkwm

    private def addTo(from:mHashSet[Watcher], to:mHashSet[Watcher]):Unit = {
      if(from != null) to ++= from
    }

    def removeWatcher(clientPath:String, watcher:Watcher, watcherType:WatcherType, local:Boolean, rc:Int): Map[EventType, mHashSet[Watcher]] = {
      // Validate the provided znode path contains the given watcher of watcherType
      containsWatcher(clientPath,watcher,watcherType)
      val childWatchersToRem = mHashSet[Watcher]()
      val removedWatchers = new ConcurrentHashMap[EventType,mHashSet[Watcher]]
      removedWatchers.put(EventType.ChildWatchRemoved, childWatchersToRem)
      val dataWatchersToRem = mHashSet[Watcher]()
      removedWatchers.put(EventType.DataWatchRemoved, dataWatchersToRem)
      var removedWatcher: Boolean = false
      var removedDataWatcher:Boolean = false
      watcherType match {
        case WatcherType.Children =>
          removedWatcher = removeWatches(childWatches,watcher,clientPath,local,rc,childWatchersToRem)
        case WatcherType.Data =>
          removedWatcher = removeWatches(dataWatches,watcher,clientPath,local,rc,dataWatchersToRem)
          removedDataWatcher = removeWatches(existWatches,watcher,clientPath,local,rc,dataWatchersToRem)
          removedWatcher |= removedDataWatcher
        case WatcherType.Any =>
          removedWatcher = removeWatches(childWatches,watcher,clientPath,local,rc,childWatchersToRem)
          removedDataWatcher = removeWatches(dataWatches,watcher,clientPath,local,dataWatchersToRem)
          removedWatcher |= removedDataWatcher
          removedDataWatcher = removeWatches(existWatches,watcher,clientPath,local,rc,dataWatchersToRem)
          removedWatcher |= removedDataWatcher
      }
      if(!removedWatcher) throw new KeeperException.NoWatcherException(clientPath)
      removedWatchers.asScala.toMap
    }

    private def contains(path:String, watcherObj:Watcher, pathVsWatchers:ConcurrentHashMap[String, mHashSet[Watcher]]): Boolean = {
      var watcherExists:Boolean = true
      var watchers: Option[Set[Watcher]] = None

      if(pathVsWatchers == null || pathVsWatchers.isEmpty) watcherExists = false
      else {
        watchers = pathVsWatchers.get(path)
        if(watchers.isEmpty) watcherExists = false
        else if (watcherObj == null) watcherExists = watchers.isDefined
        else watcherExists = watchers.contains(watcherObj)
      }
      watcherExists
    }

    def containsWatcher(path: String, watcher: Watcher, watcherType: Watcher.Event.WatcherType):Unit = {
      var containsWatcher = false
      var containsTemp = false
      watcherType match {
        case WatcherType.Children =>
          containsWatcher = contains(path, watcher,childWatches)
        case WatcherType.Data =>
          containsWatcher = contains(path, watcher,dataWatches)
          containsTemp = contains(path,watcher,existWatches)
          containsWatcher |= containsTemp
        case WatcherType.Any =>
          containsWatcher = contains(path,watcher,childWatches)
          containsTemp = contains(path,watcher,dataWatches)
          containsWatcher |= containsTemp
          containsTemp = contains(path,watcher,existWatches)
          containsWatcher |= containsTemp
      }
      if(!containsWatcher) throw new KeeperException.NoWatcherException(path)
    }

    protected def removeWatches(pathVsWatcher: ConcurrentHashMap[String, mHashSet[Watcher]], watcher: Watcher, path: String, local: Boolean, rc: Int, removedWatchers: mHashSet[Watcher]):Boolean = {
      if(!local && rc != Code.OK.value)
    }
  }
  def createDefaultHostProvider(connectString:String):HostProvider = {
    new StaticHostProvider(new ConnectStringParser(connectString).getServerAddresses)
  }
}
