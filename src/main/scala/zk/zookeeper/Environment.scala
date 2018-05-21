package zk.zookeeper

import java.net.{InetAddress, UnknownHostException}

import com.typesafe.scalalogging.Logger

object Environment {
  val JAAS_CONF_KEY = "java.security.auth.login.config"
  case class Entry(k:String, v:String) {
    override def toString: String = k + "=" + v
  }

  def list:List[Entry] = {
    // TODO - move this call to Util function
    var hn:String = ""
    try {
      hn = InetAddress.getLocalHost.getCanonicalHostName
    } catch {
      case e:UnknownHostException => hn = "<NA>"
    }
    Entry("zookeeper.version", Version.getFullVersion) ::
    Entry("host.name", hn) ::
    Entry("java.version", System.getProperty("java.version", "<NA>")) ::
    Entry("java.vendor", System.getProperty("java.vendor", "<NA>")) ::
    Entry("java.home", System.getProperty("java.home", "<NA>")) ::
    Entry("java.class.path", System.getProperty("java.class.path", "<NA>")) ::
    Entry("java.library.path", System.getProperty("", "<NA>")) ::
    Entry("java.io.tmpdir", System.getProperty("", "<NA>")) ::
    Entry("java.compiler", System.getProperty("", "<NA>")) ::
    Entry("os.name", System.getProperty("", "<NA>")) ::
    Entry("os.arch", System.getProperty("", "<NA>")) ::
    Entry("os.version", System.getProperty("", "<NA>")) ::
    Entry("user.name", System.getProperty("", "<NA>")) ::
    Entry("user.home", System.getProperty("", "<NA>")) ::
    Entry("user.dir", System.getProperty("", "<NA>")) ::
    Entry("os.memory.free", (Runtime.getRuntime.freeMemory()/(1024*1024)).toString) ::
    Entry("os.memory.max", (Runtime.getRuntime.maxMemory()/(1024*1024)).toString) ::
    Entry("os.memory.total", (Runtime.getRuntime.totalMemory()/(1024*1024)).toString) ::
    Nil
  }

  def logEnv(msg:String, log:Logger):Unit = for(e <- Environment.list) log.info(msg + e.toString)

}