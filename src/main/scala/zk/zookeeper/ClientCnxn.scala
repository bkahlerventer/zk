package zk.zookeeper

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.concurrent.{CopyOnWriteArraySet, LinkedBlockingQueue}

import com.typesafe.scalalogging.Logger
import zk.io.Record
import zk.zookeeper.Watcher.Event.EventType
import zk.zookeeper.client.{HostProvider, ZKClientConfig, ZooKeeperSaslClient}
import zk.zookeeper.proto.{ReplyHeader, RequestHeader}

import scala.collection.mutable

class ClientCnxn(chroot_path:String, host_provider: HostProvider, session_timeout:Int, zoo_keeper:ZooKeeper,
                 _watcher:ClientWatchManager, client_cnxn_socket: ClientCnxnSocket, session_id:Long, session_passwd:Array[Byte], can_be_read_only:Boolean) {
  import ClientCnxn._

  private[this] var _chrootPath: String = chroot_path
  private var hostProvider = host_provider
  private[this] var _sessionTimeout: Int = session_timeout

  private def sessionTimeout: Int = _sessionTimeout

  private def sessionTimeout_=(value: Int): Unit = {
    _sessionTimeout = value
  }

  private var zooKeeper = zoo_keeper
  private var watcher = _watcher
  private var clientCnxnSocket = client_cnxn_socket
  private val readOnly = can_be_read_only

  private var clientConfig: ZKClientConfig = zoo_keeper.getClientConfig

  private val sendThread = new SendThread(clientCnxnSocket)
  private val eventThread = new EventThread()


  private val authInfo = new CopyOnWriteArraySet[AuthData]()
  private val pendingQueue = new mutable.LinkedList[Packet]()
  private val outgoingQueue = new LinkedBlockingQueue[Packet]()


  private def connectTimeout: Int = sessionTimeout / hostProvider.size
  private var negotiatedSessionTimeout: Int = 0


  private def readTimeout: Int = sessionTimeout * 2 / 3

  private[this] var _sessionId: Long = session_id

  private def sessionId: Long = _sessionId

  private def sessionId_=(value: Long): Unit = _sessionId = value


  private[this] var _sessionPasswd: Option[Array[Byte]] = Option(new Array[Byte](16))

  private def sessionPasswd: Option[Array[Byte]] = _sessionPasswd

  private def sessionPasswd_=(value: Option[Array[Byte]]): Unit = _sessionPasswd = value


  private def chrootPath: String = _chrootPath

  private def chrootPath_=(value: String): Unit = _chrootPath = value

  private var closing: Boolean = false


  private var seenRwServerBefore = false

  private var zooKeeperSaslClient: Option[ZooKeeperSaslClient] = None
  private var requestTimeout: Long = initRequestTimeout



  def initRequestTimeout:Long = {
    val rt = Option(clientConfig.getLong(ZKClientConfig.ZOOKEEPER_REQUEST_TIMEOUT, ZKClientConfig.ZOOKEEPER_REQUEST_TIMEOUT_DEFAULT))
    if(rt.isDefined) {
      LOG.info(s"${ZKClientConfig.ZOOKEEPER_REQUEST_TIMEOUT} value is $rt. feature enabled=${rt.get > 0}")
      rt.get
    } else {
      val e = s"Configured value ${clientConfig.getProperty(ZKClientConfig.ZOOKEEPER_REQUEST_TIMEOUT)} for property ${ZKClientConfig.ZOOKEEPER_REQUEST_TIMEOUT} can not be parsed to long"
      LOG.error(e)
      throw new NumberFormatException(e)
    }
  }

  def finishPacket(p:Packet):Unit = ???

  def queueEvent(clientPath:String, err:Int, materializedWatchers:mutable.Set[Watcher], eventType:EventType):Unit = ???

  def queueCallBack(cb:AsyncCallback, rc:Int, path:String, ctx:Any):Unit = ???

  private def connLossPacket(p:Packet):Unit = ???

  private val lastZxid = 0

  private[this] var _xid: Int = 1

  private def xid: Int = {
    _xid += 1
    _xid - 1
  }

  private[this] var _state: States = States.NOT_CONNECTED

  private def state: States = _state

  private def state_=(value: States): Unit =_state = value


  def start(): Unit = {
    sendThread.start()
    eventThread.start()
  }

  private var eventOfDeath = new Object

  override def toString: String = ???

  this(chroot_path:String, host_provider: HostProvider, session_timeout:Int, zoo_keeper:ZooKeeper,
    _watcher:ClientWatchManager, client_cnxn_socket: ClientCnxnSocket, can_be_read_only:Boolean) =
    this(chroot_path, host_provider,session_timeout,zoo_keeper,_watcher,client_cnxn_socket,0,new Array[Byte](16),can_be_read_only)

  def disconnect():Unit = ???

  def close():Unit = ???

  def submitRequest(h:RequestHeader, request:Record, response:Record, watchRegistration: WatchRegistration):ReplyHeader = ???

  def waitForPacketFinish(r:ReplyHeader, packet: Packet):Unit = ???

  def saslCompleted() = sendThread.clientCnxnSocket.saslCompleted

  def sendPacket(request:Record, response:Record, cb:AsyncCallback, opCode:Int):Unit = ???

  def queuePacket(h:RequestHeader, r:ReplyHeader, request:Record, response:Record, cb:AsyncCallback, clientPath:String,
                  serverPath:String, ctx:Object, watchRegistration: WatchRegistration):Packet =
    queuePacket(h,r,request,response,cb,clientPath,serverPath,ctx,watchRegistration,null)

  def queuePacket(h:RequestHeader, r:ReplyHeader, request:Record, response:Record, cb:AsyncCallback, clientPath:String,
                  serverPath:String, ctx:Object, watchRegistration: WatchRegistration, watchDeregistration: WatchDeregistration):Packet = ???

  def addAuthInfo(scheme:String, auth:Array[Byte]):Unit = ???

  


}

object ClientCnxn {
  val LOG = Logger(classOf[ClientCnxn])

  val SET_WATCHES_MAX_LENGTH:Int = 128 * 1024


}

@SerialVersionUID(-5438877188796231422L)
final case class EndOfStreamException(msg:String) extends IOException(msg) {
  override def toString: String = s"EndOfStreamException: $getMessage"
}

@SerialVersionUID(824482094072071178L)
final case class SessionTimeoutException(msg:String) extends IOException(msg)

@SerialVersionUID(-1388816932076193249L)
final case class SessionExpiredException(msg:String) extends IOException(msg)

@SerialVersionUID(90431199887158758L)
final case class RWServerFoundException(msg:String) extends IOException(msg)




case class AuthData(scheme:String, data:Array[Byte])
case class WatcherSetEventPair(watchers:mutable.Set[Watcher], event:WatchedEvent)
case class LocalCallback(cb:AsyncCallback,rc:Int, path:String, ctx:Any)

