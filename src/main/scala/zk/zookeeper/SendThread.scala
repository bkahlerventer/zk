package zk.zookeeper

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import zk.zookeeper.client.ZKClientConfig
import zk.zookeeper.server.ZooKeeperThread

import scala.util.Random

class SendThread(client_cnxn_socket:ClientCnxnSocket) extends ZooKeeperThread(ZooKeeperThread.makeThreadName("-SendThread()")) {
  import SendThread._

  private var lastPingSentNs: Long = 0

  private[this] var _clientCnxnSocket: ClientCnxnSocket = client_cnxn_socket

  def clientCnxnSocket: ClientCnxnSocket = _clientCnxnSocket

  def clientCnxnSocket_=(value: ClientCnxnSocket): Unit = _clientCnxnSocket = value

  private val r = new Random(System.nanoTime())

  private var isFirstConnect = true

  private[this] var _state: States.CONNECTING.type = States.CONNECTING

  def state: States.CONNECTING.type = _state

  def state_=(value: States.CONNECTING.type): Unit = _state = value

  def readResponse(incomingBuffer:ByteBuffer):Unit = ???

  def primeConnection(): Unit = ???

  private def prependChroot(paths:List[String]):List[String] = ???

  private def sendPing():Unit = ???

  private var rwServerAddress:Option[InetSocketAddress] = None

  private var PingRwTimeout = minPingRwTimeout

  var saslLoginFailed = false

  private def startConnect(addr:InetSocketAddress):Unit = ???

  private def getServerPrinciple(addr:InetSocketAddress): String = ???

  private def logStartConnect(addr:InetSocketAddress):Unit = ???

  override def run(): Unit = ???

  private def cleanAndNotifyState():Unit = ???

  private def pingRwServer(): Unit = ???

  private def cleanup(): Unit = ???

  def onConnected(negotiated_session_timeout:Int, session_id:Long, session_passwd:Array[Byte], isRO:Boolean):Unit = ???

  def close():Unit = ???

  def testableCloseSocket(): Unit = ???

  def tunnelAuthInProgress():Boolean = ???

  def sendPacket(p:Packet):Unit = ???

}

object SendThread {
  val minPingRwTimeout = 100
  val maxPingRwTimeout = 60000

  val RETRY_CONN_MSG = ", closing socket connection and attempting reconnect"

  def getServerPrincipal(addr: InetSocketAddress,clientConfig: ZKClientConfig):String =
    s"${clientConfig.getProperty(ZKClientConfig.ZK_SASL_CLIENT_USERNAME, ZKClientConfig.ZK_SASL_CLIENT_USERNAME_DEFAULT)}/${addr.getHostString}"

}

