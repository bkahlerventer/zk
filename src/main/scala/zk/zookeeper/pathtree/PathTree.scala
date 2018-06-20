package zk.zookeeper.pathtree

import zk.jute.Record

class PathTree {
}

case class Node(data, children:Map[String,Node]) extends PathTree with Record