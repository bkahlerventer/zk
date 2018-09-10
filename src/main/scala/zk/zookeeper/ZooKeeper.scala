package zk.zookeeper

import java.util.concurrent.ConcurrentHashMap

import com.typesafe.scalalogging.Logger
import zk.zookeeper.client.ZooKeeperSaslClient
import org.slf4j.LoggerFactory
import zk.zookeeper.client.{ConnectStringParser, HostProvider, StaticHostProvider, ZKClientConfig}
import zk.zookeeper.Watcher.Event.{EventType, KeeperState, WatcherType}
import zk.zookeeper.KeeperException.Code
import zk.zookeeper.data.{ACL, Stat}
import zk.zookeeper.proto.RequestHeader
import zk.zookeeper.AsyncCallback
import zk.jute.Record

import scala.collection.JavaConverters._
import scala.collection.immutable.HashSet
import scala.collection.mutable



// P. String,Int,Watcher,Boolean,HostProvider,Option[ZKClientConfig]
/**
  * Combination of the Zookeeper 9 Constructors into a Primary Constructor, and reworking the other constructors to only call the primary
  *
  * Primary
  *  0. (String,Int,Watcher,Option[Long],Option[Array[Byte] ],Boolean,HostProvider,Option[ZClientConfig])(8P)
  * ZooKeeper(connectString:String,sessionTimeout:Int,watcher:Watcher,sessionId:Option[Long],sessionPasswd:Option[Array[Byte] ],canBeReadOnly:Boolean,aHostProvider:HostProvider,conf:Option[ZKClientConfig])
  *
  *  1. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher)(3)
  *  this(connectString, sessionTimeout, watcher, None, None, false, createDefaultHostProvider(connectString), None)(8P)
  *  2. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, ZKClientConfig conf)(4)
  *  this(connectString, sessionTimeout, watcher, None, None, false, createDefaultHostProvider(connectString), conf)(8P)
  *  3. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, boolean canBeReadOnly, HostProvider aHostProvider)(5)
  *  this(connectString, sessionTimeout, watcher, None, None, canBeReadOnly, aHostProvider, None)(8P)
  *  4. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, boolean canBeReadOnly, HostProvider aHostProvider, ZKClientConfig conf)(6)
  *  this(connectString, sessionTimeout, watcher, None, None, canBeReadOnly, aHostProvider, conf)(8P)
  * TODO port to primary constructor - ( Creates connection and starts it ) L863
  * TODO createConnection(connectStringParser.getChrootPath(), hostProvider, sessionTimeout, this, watchManager, getClientCnxnSocket(), canBeReadOnly);
  *  5. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, boolean canBeReadOnly)(4)
  * this(connectString, sessionTimeout, watcher, canBeReadOnly, createDefaultHostProvider(connectString))(5)
  *  6. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, boolean canBeReadOnly, ZKClientConfig conf)(5)
  * this(connectString, sessionTimeout, watcher, canBeReadOnly, createDefaultHostProvider(connectString), conf)(6)
  *  7. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd)(5P)
  * this(connectString, sessionTimeout, watcher, sessionId, sessionPasswd, false)(6P)
  *  8. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd, boolean canBeReadOnly, HostProvider aHostProvider)(7P)
  * TODO port to Primary - ( Zookeeper Client Object ) L1135 - Final Primary
  * TODO ClientCnxn(connectStringParser.getChrootPath(), hostProvider, sessionTimeout, this, watchManager, getClientCnxnSocket(), sessionId, sessionPasswd, canBeReadOnly);
  *  9. public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd, boolean canBeReadOnly)(6P)
  * this(connectString, sessionTimeout, watcher, sessionId, sessionPasswd, canBeReadOnly, createDefaultHostProvider(connectString))(7P)
  *
  * TODO - merge ClientCnxn() with createConnection()
  *
  * @param connectString
  * @param sessionTimeout
  * @param watcher
  * @param sessionId
  * @param sessionPasswd
  * @param canBeReadOnly
  * @param aHostProvider
  * @param conf
  */
class ZooKeeper(connectString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Option[Long], sessionPasswd: Option[Array[Byte]],
                canBeReadOnly:Boolean, aHostProvider:HostProvider, conf:Option[ZKClientConfig]) extends AutoCloseable {
  import ZooKeeper._

  LOG.info(s"Initiating client connection, connectstring=$connectString sessionTimeout=$sessionTimeout watcher=$watcher")

  @deprecated("Use ZKClientConfig.ZOOKEEPER_CLIENT_CNXN_SOCKET instead.")
  val ZOOKEEPER_CLIENT_CNXN_SOCKET = "zookeeper.clientCnxnSocket"

  @deprecated("Use ZKClientConfig.SECURE_CLIENT instead.")
  val SECURE_CLIENT = "zookeeper.client.secure"

  protected def defaultWatchManager:ZKWatchManager = ZKWatchManager(getClientConfig.getBoolean(ZKClientConfig.DISABLE_AUTO_WATCH_RESET))

  private val watchManager = defaultWatchManager
  private val clientConfig = conf.getOrElse(new ZKClientConfig)
  private val connectStringParser = new ConnectStringParser(connectString)
  watchManager.defaultWatcher = watcher
  protected val cnxn:ClientCnxn = new ClientCnxn(connectStringParser.getChrootPath(),
    aHostProvider, sessionTimeout, this, watchManager,
    getClientCnxnSocket(), canBeReadOnly)

  cnxn.start()


  def updateServerList(connectionString:String):Unit = ???

  def getSaslClient: ZooKeeperSaslClient = cnxn.zooKeeperSaslClient

  def getClientConfig:ZKClientConfig = clientConfig

  def getDataWatches: List[String] = ???

  def getExistWatches: List[String] = ???

  def getChildWatches: List[String] = ???


  // (String, Int, Watcher, Option[Long, Option[Array[Byte]], Boolean, HostProvider, Option[ZKClientConfig])
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, sessionPasswd: Option[Array[Byte]], canBeReadOnly:Boolean, aHostProvider:HostProvider, conf:Option[ZKClientConfig]) = {
    this (connectString, sessionTimeout, watcher, sessionId, sessionPasswd, canBeReadOnly, aHostProvider, conf)
  }
  // (String, Int, Watcher, Option[Array[Byte]], Boolean, HostProvider, Option[ZKClientConfig])
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean, aHostProvider:HostProvider, conf:Option[ZKClientConfig]) = {
    this(connectString, sessionTimeout, watcher, None, canBeReadOnly, aHostProvider, conf)
  }

  // 1. String, Int, Watcher, Boolean, HostProvider
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean, aHostProvider:HostProvider) = {
    // P. String,Int,Watcher,Option[Array[Byte]],Boolean,HostProvider,Option[ZKClientConfig]
    this(connectString,sessionTimeout,watcher,None,canBeReadOnly,aHostProvider,None)
  }
  // 2. String,Int,Watcher,Boolean
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean) = {
    // P. String,Int,Watcher,Option[Array[Byte]],Boolean,HostProvider,Option[ZKClientConfig]
    this(connectString,sessionTimeout,watcher,None,canBeReadOnly,createDefaultHostProvider(connectString),None)
  }
  // 3. String,Int,Watcher,Boolean,ZkClientConfig
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, canBeReadOnly:Boolean, conf:Option[ZKClientConfig]) = {
    // P. String,Int,Watcher,Option[Array[Byte]],Boolean,HostProvider,Option[ZKClientConfig]
    this(connectString,sessionTimeout,watcher,None,canBeReadOnly,createDefaultHostProvider(connectString), conf)
  }
  // 4. String,Int,Watcher
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher) = {
    // P. String,Int,Watcher,Option[Array[Byte]],Boolean,HostProvider,Option[ZKClientConfig]
    this(connectString,sessionTimeout,watcher,None,false,createDefaultHostProvider(connectString),None)
  }
  // 5. String,Int,Watcher,ZKClientConfig
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, conf:Option[ZKClientConfig]) = {
    // P. String,Int,Watcher,Option[Array[Byte]],Boolean,HostProvider,Option[ZKClientConfig]
    this(connectString,sessionTimeout,watcher,None,false,createDefaultHostProvider(connectString),conf)
  }
  // 6. String,Int,Watcher,Long,Array[Byte],Boolean,HostProvider
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Long, sessionPasswd:Option[Array[Byte]], canBeReadOnly:Boolean, aHostProvider:HostProvider) = {
    // P. String,Int,Watcher,Option[Array[Byte]],Boolean,HostProvider,Option[ZKClientConfig]
    this(connectString,sessionTimeout,watcher,None,false,createDefaultHostProvider(connectString),None)
  }
  // 7. String,Int,Watcher,Long,Array[Byte],Boolean
  def this(connectString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Long, sessionPasswd:Array[Byte], canBeReadOnly:Boolean) = {
    this(connectString,sessionTimeout,watcher,sessionId,sessionPasswd,canBeReadOnly,createDefaultHostProvider(connectString))
  }
  // 8. String,Int,Watcher,Long,Array[Byte]
  def this(connectionString:String, sessionTimeout:Int, watcher:Watcher, sessionId:Long, sessionPasswd:Array[Byte]) = {
    // String,Int,Watcher,Long,Array[Byte],Boolean
    this(connectionString,sessionTimeout,watcher,sessionId,sessionPasswd,false)
  }

  def getTestable:Testable = ???

  def getSessionId:Long = ???

  def getSessionPasswd:Vector[Byte] = ???

  def getSessionTimeout:Int = ???

  def addAuthInfo(scheme:String, auth:Vector[Byte]):Unit = ???

  def register(watcher:Watcher):Unit = ???

  override def close(): Unit = ???

  def close(waitForShutdownTimeoutMs:Int):Boolean = ???

  def prependChroot(clientPath:String):String = ???

  def create(path:String, data:Vector[Byte], acl:List[ACL], createMode:CreateMode):String = ???

  def create(path:String, data:Vector[Byte], acl:List[ACL], createMode:CreateMode, stat:Stat):String = ???

  def create(path:String, data:Vector[Byte], acl:List[ACL], createMode:CreateMode, stat:Stat, ttl:Long):String = ???

  def setCreateHeader(createMode:CreateMode, h:RequestHeader):Unit = ???

  def makeCreateRecord(createMode:CreateMode, serverPath:String, data:Vector[Byte], acl:List[ACL], ttl:Long):Record = ???

  def create(path:String, data:Vector[Byte], acl:List[ACL], createMode:CreateMode, cb:StringCallback, ctx:Object):String = ???

  def create(path:String, data:Vector[Byte], acl:List[ACL], createMode:CreateMode, cb:Create2Callback, ctx:Object):String = ???

  def create(path:String, data:Vector[Byte], acl:List[ACL], createMode:CreateMode, cb:Create2Callback, ctx:Object, ttl:Long):String = ???

  def delete(path:String, version:Int):Unit = ???

  def multi(ops:Iterable[Op]):List[OpResult] = ???

  def multi(ops:Iterable[Op], cb:MultiCallback, ctx:Object):Unit = ???

  def validatePath(ops:Iterable[Op]):List[OpResult] = ???

  def generateMultiTransaction(ops:Iterable[Op]):MultiTransactionRecord = ???

  def withRootPrefix(op:Op):Op = ???

  def multiInternal(request:MultiTransactionRecord, cb:MultiCallback, ctx:Object):Unit = ???

  def multiInternal(request:MultiTransactionRecord):List[OpResult]

  def transaction:Transaction = ???

  def delete(path:String, version:Int, cb:VoidCallback, ctx:Object):Unit = ???

  def exists(path:String, watcher:Watcher):Stat = ???

  def exists(path:String, watch:Boolean):Stat = ???

  def exists(path:String, watcher:Watcher, cb:StatCallback, ctx:Object):Stat = ???

  def getData(path:String, watcher:Watcher, stat:Stat):Vector[Byte] = ???

  def getData(path:String, watch:Boolean, stat:Stat):Vector[Byte] = ???

  def getData(path:String, watcher:Watcher, cb:DataCallback, ctx:Object):Unit = ???

  def getData(path:String, watch:Boolean, cb:DataCallback, ctx:Object):Unit = ???

  def getConfig(watcher:Watcher, stat:Stat):Vector[Byte] = ???

  def getConfig(watcher: Watcher, cb:DataCallback, ctx:Object):Unit = ???

  def getConfig(watch:Boolean, stat:Stat):Vector[Byte] = ???

  def getConfig(watch:Boolean, cb:DataCallback, ctx:Object):Unit = ???

  def setData(path:String, data:Vector[Byte], version:Int):Stat = ???

  def setData(path:String, data:Vector[Byte], version:Int, cb:StatCallback, ctx:Object):Unit = ???

  def getACL(path:String, stat:Stat):List[ACL] = ???

  def getACL(path:String, stat:Stat, cb:ACLCallback, ctx:Object):Unit = ???

  def setACL(path:String, acl:List[ACL], version:Int):Stat = ???

  def setACL(path:String, acl:List[ACL], version:Int, cb:StatCallback, ctx:Object):Unit = ???

  def getChildren(path:String, watcher:Watcher):List[String] = ???

  def getChildren(path:String, watch:Boolean):List[String] = ???

  def getChildren(path:String, watcher:Watcher, cb:ChildrenCallback, ctx:Object):Unit = ???

  def getChildren(path:String, watch:Boolean, cb:ChildrenCallback, ctx:Object):Unit = ???

  def getChildren(path:String, watcher:Watcher, stat:Stat):List[String] = ???

  def getChildren(path:String, watch:Boolean, stat:Stat):List[String] = ???

  def getChildren(path:String, watcher:Watcher, stat:Stat, cb:ChildrenCallback, ctx:Object):List[String] = ???

  def getChildren(path:String, watch:Boolean, stat:Stat, cb:ChildrenCallback, ctx:Object):List[String] = ???

  def sync(path:String, cb:VoidCallback, ctx:Object):Unit = ???

  def removeWatches(path:String, watcher: Watcher, watcherType: WatcherType, local:Boolean):Unit = ???

  def removeWatches(path:String, watcher: Watcher, watcherType: WatcherType, local:Boolean, cb:VoidCallback, ctx:Object):Unit = ???

  def removeAllWatches(path:String, watcherType: WatcherType, local:Boolean):Unit = ???

  def removeAllWatches(path:String, watcherType: WatcherType, local:Boolean, cb:VoidCallback, ctx:Object):Unit = ???

  def validateWatcher(watcher: Watcher):Unit = ???

  def removeWatches(opCode:Int, path:String, watcher:Watcher, watcherType: WatcherType, local:Boolean):Unit = ???

  def removeWatches(opCode:Int, path:String, watcher:Watcher, watcherType: WatcherType, local:Boolean, cb:VoidCallback, ctx:Object):Unit = ???

  def getRemoveWatchesRequest(opCode:Int, watcherType: WatcherType, path:String):Record = ???

  def getState:States = ???  //-----

  def materialize(state:KeeperState,etype:EventType,path:String):Set[Watcher] = ???



  class ExistWatchRegistration(watcher:Watcher, clientPath:String) extends WatchRegistration(watcher, clientPath) {
    override protected def getWatches(rc: Int): Map[String, Set[Watcher]] = ???

    override protected def shouldAddWatch(rc:Int):Boolean = ???

  }

}

object ZooKeeper {
  val LOG = Logger(classOf[ZooKeeper])
  Environment.logEnv("Client environment:", LOG)

  @Deprecated
  val ZOOKEEPER_CLIENT_CNXN_SOCKET = "zookeeper.clientCnxnSocket"

  @Deprecated
  val SECURE_CLIENT = "zookeeper.client.secure"



  def createDefaultHostProvider(connectString:String):HostProvider = {
    new StaticHostProvider(new ConnectStringParser(connectString).getServerAddresses.toVector)
  }
}

abstract class WatchRegistration(watcher:Watcher, clientPath:String) {
  protected def getWatches(rc:Int):Map[String,Set[Watcher]]

  def register(rc:Int):Unit = {
    if(shouldAddWatch(rc)) {
      val watchMap: Map[String, Set[Watcher]] = getWatches(rc)
      synchronized(watchMap) {
        var watcherSet:Set[Watcher] = new HashSet[Watcher]()
        watchMap.get(clientPath) match {
            // found no watch for the path, create one
          case None => watchMap(clientPath) = new HashSet[Watcher]()
          case Some(w) => watchMap(clientPath) = w
        }
        watcherSet += watcher  // add watcher to Set of watcherSet
      }
    }
  }
  protected def shouldAddWatch(rc: Int):Boolean = rc == 0
}

