package zk.zookeeper.server

import java.lang.Thread.UncaughtExceptionHandler

import com.typesafe.scalalogging.Logger

class ZooKeeperThread(threadName:String) extends Thread(threadName) {
  import ZooKeeperThread._
  setUncaughtExceptionHandler(uncaughtExceptionHandler)

  private def uncaughtExceptionHandler:UncaughtExceptionHandler = (t: Thread, e: Throwable) => handleException(t.getName, e)

  protected def handleException(thName:String, e:Throwable):Unit = {
    LOG.warn(s"Exception occurred from thread $thName", e)
  }

}

object ZooKeeperThread {
  val LOG = Logger(classOf[ZooKeeperThread])
  def makeThreadName(n:String):String = ???
}