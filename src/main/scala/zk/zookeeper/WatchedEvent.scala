package zk.zookeeper

import zk.zookeeper.Watcher.Event._
import zk.zookeeper.proto.WatcherEvent

/**
  *  A WatchedEvent represents a change on the ZooKeeper that a Watcher
  *  is able to respond to.  The WatchedEvent includes exactly what happened,
  *  the current state of the ZooKeeper, and the path of the znode that
  *  was involved in the event.
  *
  *  Create a WatchedEvent with specified type, state and path
  *
  */
class WatchedEvent(eventType:EventType, keeperState:KeeperState, path:String) {

  /**
    * Convert a WatcherEvent sent over the wire into a full-fledged WatcherEvent
    */
  def this(eventMessage:WatcherEvent) {
    this(KeeperState.withValue(eventMessage.state), EventType.withValue(eventMessage.ztype), eventMessage.path)
  }

  def getState:Watcher.Event.KeeperState = keeperState

  def getType:Watcher.Event.EventType = eventType

  def getPath:String = path

  override def toString: String = s"WatchedEvent state:$keeperState type:$eventType path:$path"

  /**
    *  Convert WatchedEvent to type that can be sent over network
    */
  def getWrapper:WatcherEvent = WatcherEvent(eventType.value, keeperState.value,path)

}
