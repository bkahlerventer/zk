package zk.zookeeper

import java.util.concurrent.LinkedBlockingQueue

import com.typesafe.scalalogging.Logger
import zk.zookeeper.server.ZooKeeperThread



class EventThread(tName:String) extends ZooKeeperThread(tName){
  import ZooKeeperThread.makeThreadName

  private val waitingEvents = new LinkedBlockingQueue[Object]()
  private var wasKilled = false
  private var isRunning = false


  def this() = this(makeThreadName("-EventThread"))

  def queueEvent(event:WatchedEvent):Unit = queueEvent(event,None)

  private def queueEvent(event:WatchedEvent,materializedWatchers:Option[Set[Watcher]]):Unit = ???

  def queueCallback(cb:AsyncCallback,rc:Int, path:String, ctx:Any) = waitingEvents.add(new LocalCallback(cb,rc,path,ctx))

  def queuePacket(packet: Packet): Unit = ???

  def queueEventOfDeath(): Unit = ???

  override def run(): Unit = ???

  private def processEvent(event:Any):Unit = ???

}

object EventThread {
  val LOG = Logger(classOf[EventThread])
}