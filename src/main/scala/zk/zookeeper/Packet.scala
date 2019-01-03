package zk.zookeeper

import java.io.{ByteArrayOutputStream, IOException}
import java.nio.ByteBuffer

import com.typesafe.scalalogging.Logger
import zk.io.{BinaryOutputArchive, Record}
import zk.zookeeper.proto.{ConnectRequest, ReplyHeader, RequestHeader}

class Packet(reqh:Option[RequestHeader], replyh:ReplyHeader, req:Record, resp:Record, watchReg:WatchRegistration, ro:Boolean) {
  import Packet._

  val requestHeader:Option[RequestHeader] = reqh
  val replyHeader:ReplyHeader = replyh
  val request:Record = req
  val response:Record = resp
  val readOnly:Boolean = ro
  val watchRegistration:WatchRegistration = watchReg

  var bb:ByteBuffer

  var clientPath:String
  var serverPath:String
  var finished:Boolean
  var cb:AsyncCallback
  var ctx:Object
  var watchDeregistration: WatchDeregistration


  this(reqh:Option[RequestHeader], replyh:ReplyHeader, req:Record, resp:Record, watchReg:WatchRegistration) =
    this(reqh, replyh, req, resp, watchReg, false)

  def createBB():Unit = {
    try {
      val baos = new ByteArrayOutputStream()
      val boa: BinaryOutputArchive = BinaryOutputArchive(baos)
      boa.writeInt(-1, "len") // We'll fill this in later
      if (requestHeader.isDefined) requestHeader.get.serialize(boa, "header")
      if (request.isInstanceOf[ConnectRequest]) {
        request.serialize(boa, "connect")
        // append "am-I-allowed-to-be-readonly flag
        boa.writeBool(readOnly, "readOnly")
      } else if (request != null) request.serialize(boa, "request")
      baos.close()
      bb = ByteBuffer.wrap(baos.toByteArray)
      bb.putInt(bb.capacity() - 4)
      bb.rewind()
    } catch {
      case e: IOException => LOG.warn("Ignoring unexpected exception", e)
    }
  }

  override def toString: String = {
    val s = s"clientPath: $clientPath serverPath: $serverPath finished: $finished header: $requestHeader replyHeader: $replyHeader request $request response: $response"
    s.replaceAll("\r*\n+", " ")
  }


}

object Packet {
  val LOG = Logger(classOf[ZooKeeper])


}