package zk.zookeeper.common

import com.typesafe.scalalogging.Logger
import java.io._

import scala.util.control.NonFatal

class IOUtils {

}

object IOUtils {
  val LOG = Logger(classOf[IOUtils])

  def closeStream(stream:Closeable): Unit = cleanup(null, stream)

  def cleanup(log:Logger, closeables:Closeable*): Unit = {
    for(c <- closeables) {
      try {
        c.close()
      } catch {
        case NonFatal(e) =>
          LOG.warn(s"Exception in closing $c", e)
      }
    }
  }

  def copyBytes(in:InputStream, out:OutputStream, buffSize:Int, close:Boolean):Unit = {
    try {
      copyBytes(in,out,buffSize)
      in.close()
      out.close()
    }
  }

  def copyBytes(in:InputStream, out:OutputStream, buffSize:Int): Unit = {
    val ps: PrintStream = out match {
      case p: PrintStream => p
      case _ => null
    }
    val buf: Array[Byte] = new Array[Byte](buffSize)
    var bytesRead:Int = in.read(buf)

    while(bytesRead >= 0) {
      out.write(buf, 0, bytesRead)
      if((ps != null) && ps.checkError()) {
        throw new IOException("Unable to write to output stream.")
      }
      bytesRead = in.read(buf)
    }
  }
}

