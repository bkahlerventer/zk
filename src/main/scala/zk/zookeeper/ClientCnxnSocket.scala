package zk.zookeeper

import java.io.IOException
import java.nio.ByteBuffer
import java.util.concurrent.LinkedBlockingDeque

import com.typesafe.scalalogging.Logger
import zk.io.BinaryInputArchive
import zk.zookeeper.client.ZKClientConfig
import zk.zookeeper.common.Time
import zk.zookeeper.proto.ConnectResponse
import zk.zookeeper.server.ByteBufferInputStream

class ClientCnxnSocket {
  import ClientCnxnSocket._

  private var initialized:Boolean = false

  private var lenBuffer = ByteBuffer.allocateDirect(4)

  private var incomingBuffer = lenBuffer

  private[this] var _sentCount: Long = 0L

  private def sentCount: Long = _sentCount

  private[this] var _recvCount: Long = 0L

  private def recvCount: Long = _recvCount

  private var lastHeard: Long = 0L

  private var lastSend: Long = 0L

  private def now:Long =  Time.currentElapsedTime

  private var sendThread: SendThread

  private var outgoingQueue: LinkedBlockingDeque[Packet]

  private var clientConfig: ZKClientConfig

  private var packetLen = ZKClientConfig.CLIENT_MAX_PACKET_LENGTH_DEFAULT

  private var sessionId: Long

  def introduce(send_thread:SendThread, session_id:Long, outgoing_queue:LinkedBlockingDeque[Packet]):Unit = {
    sendThread = send_thread
    sessionId = session_id
    outgoingQueue = outgoing_queue
  }

  // no longer needed to call this... now is a def for Time.currentElapsedTime()
  @Deprecated
  def updateNow():Unit = ???

  def getIdleRecv: Int = (now - lastHeard).toInt

  def getIdleSend: Int = (now - lastSend).toInt

  def updateLastHeard():Unit = lastHeard = now

  def updateLastSend():Unit = lastSend = now

  def updateLastSendAndHeard():Unit = {
    lastSend = now
    lastHeard = now
  }

  def readLength():Unit = {
    val len = incomingBuffer.getInt()
    if(len < 0 || len >= packetLen) {
      LOG.error(s"Packet length $len is out of range!")
      throw new IOException(s"Packet length $len is out of range!")
    }
    incomingBuffer = ByteBuffer.allocate(len)
  }

  def readConnectResult():Unit = {
    LOG.whenTraceEnabled({
      val buf:StringBuilder = new StringBuilder("0x[")
      for(b <- incomingBuffer.array()) buf.append(b.toHexString + ",")
      buf.append("]")
      LOG.trace(s"readConnectResult ${incomingBuffer.remaining()} ${buf.toString()}")
    })
    var bbis: ByteBufferInputStream = new ByteBufferInputStream(incomingBuffer)
    var bbia: BinaryInputArchive = BinaryInputArchive.getArchive(bbis)
    var conRsp = new ConnectResponse()
    conRsp.deserialize(bbia, "connect")
  }

}

object ClientCnxnSocket {
  val LOG = Logger(classOf[ClientCnxnSocket])
}