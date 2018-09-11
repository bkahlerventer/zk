package zk.zookeeper.common

import java.io.{BufferedWriter, File, IOException, OutputStreamWriter}

class AtomicFileWritingIdiom(targetFile:File, osStmt:OutputStreamStatement, wStmt:WriterStatement) {
  var out:AtomicFileOutputStream = _
  var bw:BufferedWriter = _
  var error:Boolean = false

  doit()

  @throws(classOf[IOException])
  def doit(): Unit = {
    try {
      out = new AtomicFileOutputStream(targetFile)

      if (wStmt == null) osStmt.write(out)
      else {
        bw = new BufferedWriter(new OutputStreamWriter(out))
        wStmt.write(bw)
        bw.flush()
      }
      out.flush()
      error = false
    } finally {
      if (out != null) {
        if (error) {
          out.abort()
        } else {
          IOUtils.closeStream(out)
        }
      }
    }
  }

  @throws(classOf[IOException])
  def this(targetFile:File, osStmt:OutputStreamStatement) = this(targetFile,osStmt,null)

  @throws(classOf[IOException])
  def this(targetFile:File, wStmt:WriterStatement) = this(targetFile,null,wStmt)
}

object AtomicFileWritingIdiom {

}

