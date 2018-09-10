package zk.zookeeper

import java.util.concurrent.ConcurrentHashMap

import zk.zookeeper.KeeperException.Code
import zk.zookeeper.Watcher.Event.{EventType, WatcherType}

import scala.collection.mutable

// TODO - implement assignment protocols to update variables in this case class
case class ZKWatchManager(disableAutoWatchReset:Boolean) extends ClientWatchManager {
  type WatchMap = mutable.HashMap[String, mutable.HashSet[Watcher]]
  private var dataWatches = new ConcurrentHashMap[String, mutable.HashSet[Watcher]]
  private var existWatches = new ConcurrentHashMap[String, mutable.HashSet[Watcher]]
  private var childWatches = new ConcurrentHashMap[String, mutable.HashSet[Watcher]]
  // val disableAutoWatchReset - added by constructor above

  private var watchManager = ZKWatchManager(false)

  // modifier for watchManager
  def watchManager_=(zkwm:ZKWatchManager):Unit = watchManager = zkwm

  private def addTo(from:mutable.HashSet[Watcher], to:mutable.HashSet[Watcher]):Unit = {
    if(from != null) to ++= from
  }

  def removeWatcher(clientPath:String, watcher:Watcher, watcherType:WatcherType, local:Boolean, rc:Int): Map[EventType, mutable.HashSet[Watcher]] = {
    // Validate the provided znode path contains the given watcher of watcherType
    containsWatcher(clientPath,watcher,watcherType)
    val childWatchersToRem = mutable.HashSet[Watcher]()
    val removedWatchers = new ConcurrentHashMap[EventType,mutable.HashSet[Watcher]]
    removedWatchers.put(EventType.ChildWatchRemoved, childWatchersToRem)
    val dataWatchersToRem = mutable.HashSet[Watcher]()
    removedWatchers.put(EventType.DataWatchRemoved, dataWatchersToRem)
    var removedWatcher: Boolean = false
    var removedDataWatcher:Boolean = false
    watcherType match {
      case WatcherType.Children =>
        removedWatcher = removeWatches(childWatches,watcher,clientPath,local,rc,childWatchersToRem)
      case WatcherType.Data =>
        removedWatcher = removeWatches(dataWatches,watcher,clientPath,local,rc,dataWatchersToRem)
        removedDataWatcher = removeWatches(existWatches,watcher,clientPath,local,rc,dataWatchersToRem)
        removedWatcher |= removedDataWatcher
      case WatcherType.Any =>
        removedWatcher = removeWatches(childWatches,watcher,clientPath,local,rc,childWatchersToRem)
        removedDataWatcher = removeWatches(dataWatches,watcher,clientPath,local,dataWatchersToRem)
        removedWatcher |= removedDataWatcher
        removedDataWatcher = removeWatches(existWatches,watcher,clientPath,local,rc,dataWatchersToRem)
        removedWatcher |= removedDataWatcher
    }
    if(!removedWatcher) throw NoWatcherException(clientPath)
    removedWatchers.asScala.toMap
  }

  private def contains(path:String, watcherObj:Watcher, pathVsWatchers:ConcurrentHashMap[String, mutable.HashSet[Watcher]]): Boolean = {
    var watcherExists:Boolean = true
    var watchers: mutable.HashSet[Watcher] = null

    if(pathVsWatchers == null || pathVsWatchers.isEmpty) watcherExists = false
    else {
      watchers = pathVsWatchers.get(path)
      if(watchers.isEmpty) watcherExists = false
      else if (watcherObj == null) watcherExists = watchers.nonEmpty
      else watcherExists = watchers.contains(watcherObj)
    }
    watcherExists
  }

  def containsWatcher(path: String, watcher: Watcher, watcherType: Watcher.Event.WatcherType):Unit = {
    var containsWatcher = false
    var containsTemp = false
    watcherType match {
      case WatcherType.Children =>
        containsWatcher = contains(path, watcher,childWatches)
      case WatcherType.Data =>
        containsWatcher = contains(path, watcher,dataWatches)
        containsTemp = contains(path,watcher,existWatches)
        containsWatcher |= containsTemp
      case WatcherType.Any =>
        containsWatcher = contains(path,watcher,childWatches)
        containsTemp = contains(path,watcher,dataWatches)
        containsWatcher |= containsTemp
        containsTemp = contains(path,watcher,existWatches)
        containsWatcher |= containsTemp
    }
    if(!containsWatcher) throw NoWatcherException(path)
  }

  protected def removeWatches(pathVsWatcher: ConcurrentHashMap[String, mutable.HashSet[Watcher]], watcher: Watcher, path: String, local: Boolean, rc: Int, removedWatchers: mutable.HashSet[Watcher]):Boolean = {
    if(!local && rc != Code.OK.value) throw KeeperException(Some(KeeperException.Code.withValue(rc)),Option(path))
    var success = false
    if(rc == Code.OK.value || (local && rc != Code.OK.value)) {
      if(watcher == null) {
        var pathWatchers = pathVsWatcher.remove(path)
        if(pathWatchers != null) {
          removedWatchers ++= pathWatchers
          success = true
        }
      } else {

      }
    }
  }
}
object ZKWatchManager {
  def defaultWatcher:Watcher = ???
}
