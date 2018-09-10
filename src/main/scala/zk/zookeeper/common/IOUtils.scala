package zk.zookeeper.common

import com.typesafe.scalalogging.Logger
import java.io.{Closeable, InputStream, OutputStream}

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

  def copyBytes(in:InputStream, out:OutputStream, buffSize:Int): Unit = ???


}