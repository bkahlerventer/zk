package zk.zookeeper

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

import com.typesafe.scalalogging.Logger
import zk.io.{BinaryOutputArchive, Record}
import zk.zookeeper.proto.{ReplyHeader, RequestHeader}

class Packet(reqh:Option[RequestHeader], replyh:ReplyHeader, req:Record, resp:Record, watchReg:WatchRegistration, ro:Boolean) {
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
    val baos = new ByteArrayOutputStream()
    val boa:BinaryOutputArchive = BinaryOutputArchive(baos)
    boa.writeInt(-1, "len")
    if(requestHeader.isDefined) requestHeader.get.
  }
}

object Packet {
  val LOG = Logger(classOf[ZooKeeper])


}