package zk.zookeeper.common

import java.io.{File, FileOutputStream, FilterOutputStream, IOException}

import com.typesafe.scalalogging.Logger

import scala.util.control.NonFatal


class AtomicFileOutputStream(f:File) extends FilterOutputStream(new FileOutputStream(new File(f.getParentFile, f.getName + AtomicFileOutputStream.TMP_EXTENSION))) {
import AtomicFileOutputStream._

  private val origFile = f.getAbsoluteFile
  private val tmpFile = new File(f.getParentFile, f.getName + TMP_EXTENSION).getAbsoluteFile

  override def write(b: Array[Byte], off: Int, len: Int): Unit = out.write(b,off,len)

  override def close(): Unit = {
    var triedToClose:Boolean = false
    var success:Boolean = false
    try {
      flush()
      out.asInstanceOf[FileOutputStream].getFD.sync()

      triedToClose = true
      super.close()
      success = true
    } finally {
      if(success) {
        var renamed: Boolean = tmpFile.renameTo(origFile)
        if(!renamed) {
          // On windows, renameTo does not take place
          if (!origFile.delete() || !tmpFile.renameTo(origFile)) throw new IOException(s"Could not rename temporary file $tmpFile to $origFile")
        }
      } else {
        if(!triedToClose) {
          IOUtils.closeStream(out)
        }
        if(!tmpFile.delete()) {
          LOG.warn(s"Unable to delete temp file $tmpFile")
        }
      }
    }
  }

  def abort():Unit = {
    try {
      super.close()
    } catch {
      case NonFatal(e) => LOG.warn(s"Unable to abort file $tmpFile", e)
    }
    if(!tmpFile.delete()) {
      LOG.warn(s"Unable to delete tmp file during abort $tmpFile")
    }
  }
}

object AtomicFileOutputStream {
  val TMP_EXTENSION = ".tmp"

  val LOG = Logger(classOf[AtomicFileOutputStream])
}
