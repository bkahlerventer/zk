package zk.zookeeper.common

import com.typesafe.scalalogging.Logger

class PathTrie {
  import PathTrie._

  private var rootNode:TrieNode = new TrieNode(null)

  def addPath(path:String): Unit = path match {
    case null =>
      LOG.info("Tried to add null path to PathTrie")
    case s:String =>
      var pathComponents: Array[String] = path.split("/")
      var parent: TrieNode = rootNode
      if(pathComponents.length <= 1) throw new IllegalArgumentException(s"Invalid path $path")
      for(part <- pathComponents) {
        if(parent.getChild(part) == null) parent.addChild(part,new TrieNode(parent))
        parent = parent.getChild(part)
      }
      parent.property = true
  }

  def deletePath(path:String):Unit = path match {
    case null =>
      LOG.info("Tried to delete null path on PathTrie")
    case s:String =>
      var pathComponents: Array[String] = path.split("/")
      var p:String = ""
      var parent: TrieNode = rootNode
      if(pathComponents.length <= 1) throw new IllegalArgumentException(s"Invalid path $path")
      for(part <- pathComponents) {
        if(parent.getChild(part) != null)
        parent = parent.getChild(part)
        p = part
      }
      parent.parent.deleteChild(p)
  }

  def findMaxPrefix(path:String):String = ???

  def clear(): Unit = for(child <- rootNode.getChildren) rootNode.deleteChild(child)

}

object PathTrie {
  val LOG = Logger(classOf[PathTrie])
}

