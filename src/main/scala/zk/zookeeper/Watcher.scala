package zk.zookeeper

trait Watcher {
  trait Event {
    class KeeperState(i:Int) extends Enumeration {
      type KeeperState = Value
      val Unknown, Disconnected, NoSyncConnected, SyncConnected, AuthFailed, ConnectedReadOnly, SaslAuthenticated, Expired = Value
      private var iv:Int = i
      def intValue:Int = iv
      def intValue_=(s:KeeperState):Unit = s match {
        case Unknown => iv = -1
        case Disconnected => iv = 0
        case NoSyncConnected => iv = 1
        case SyncConnected => iv = 3
        case AuthFailed => iv = 4
        case ConnectedReadOnly => iv = 5
        case SaslAuthenticated => iv = 6
        case Expired => iv = -112
      }
      def fromInt(i:Int):KeeperState = i match {
        case -1 => Unknown
        case 0 => Disconnected
        case 1 => NoSyncConnected
        case 3 => SyncConnected
        case 4 => AuthFailed
        case 5 => ConnectedReadOnly
        case 6 => SaslAuthenticated
        case -112 => Expired
      }
    }
    class EventType(i:Int) extends Enumeration {
      type EventType = Value
      val None, NodeCreated, NodeDeleted, NodeDataChanged, NodeChildrenChanged, DataWatchRemoved, ChildWatchRemoved = Value
      private var iv:Int = i
      def intValue:Int = iv
      def intValue_=(s:EventType):Unit = s match {
        case None => iv = -1
        case NodeCreated => iv = 1
        case NodeDeleted => iv = 2
        case NodeDataChanged => iv = 3
        case NodeChildrenChanged => iv = 4
        case DataWatchRemoved => iv = 5
        case ChildWatchRemoved => iv = 6
      }
      def fromInt(i:Int):EventType = i match {
        case -1 => None
        case 1 => NodeCreated
        case 2 => NodeDeleted
        case 3 => NodeDataChanged
        case 4 => NodeChildrenChanged
        case 5 => DataWatchRemoved
        case 6 => ChildWatchRemoved
      }
    }

  }
}
