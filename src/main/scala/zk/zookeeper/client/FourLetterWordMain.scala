package zk.zookeeper.client

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.net.{InetAddress, InetSocketAddress, Socket}

import com.typesafe.scalalogging.Logger
import javax.net.ssl.SSLSocket
import org.slf4j.LoggerFactory
import zk.zookeeper.common.X509Util

import scala.util.control.NonFatal

class FourLetterWordMain {

}

object FourLetterWordMain {
  val DEFAULT_SOCKET_TIMEOUT = 5000
  val LOG = Logger(LoggerFactory.getLogger(FourLetterWordMain.getClass))

  def send4LetterWord(host:String,port:Int,cmd:String):String = send4LetterWord(host,port,cmd,secure = false,DEFAULT_SOCKET_TIMEOUT)

  def send4LetterWord(host:String,port:Int,cmd:String,secure:Boolean):String = send4LetterWord(host,port,cmd,secure,DEFAULT_SOCKET_TIMEOUT)

  def send4LetterWord(host:String,port:Int,cmd:String,secure:Boolean,timeout:Int):String = {
    LOG.info(s"connecting to $host:$port")
    var sock:Socket = new Socket()
    val hostAddress = if(host != null) new InetSocketAddress(host,port) else new InetSocketAddress(InetAddress.getByName(null),port)

    if(secure) {
      LOG.info("using secure socket")
      val sslContext = X509Util.createSSLContext
      val socketFactory = sslContext.get.getSocketFactory
      val sslSock:SSLSocket = socketFactory.createSocket().asInstanceOf[SSLSocket]
      sslSock.connect(hostAddress,timeout)
      sslSock.startHandshake()
      sock=sslSock
    } else sock.connect(hostAddress, timeout)
    sock.setSoTimeout(timeout)
    val reader: BufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream))
    try {
      val outstream = sock.getOutputStream
      outstream.write(cmd.getBytes)
      outstream.flush()
      if(!secure) sock.shutdownOutput()
      val sb = new StringBuilder
      for(line <- reader.readLine()) sb.append(s"$line\n")
      sb.toString()
    } catch {
      case NonFatal(e) => throw new IOException("Exception while executing four letter word: " + cmd, e)
    } finally {
      sock.close()
      reader.close()
    }
  }

  def main(args: Array[String]): Unit = args.length match {
    case 3 => println(send4LetterWord(args(0), args(1).toInt, args(2)))
    case 4 => println(send4LetterWord(args(0), args(1).toInt, args(2), args(3).toBoolean))
    case _ => println("Usage: FourLetterWordMain <host> <port> <cmd> <secure(optional)>")
  }
}