package zk.zookeeper.common


import scala.language.implicitConversions
import scala.util.control.Breaks._
import com.typesafe.scalalogging.Logger


class PathTrie {
  import PathTrie._

  private val rootNode:TrieNode = new TrieNode(None)

  def apply: PathTrie = new PathTrie()


  
  def addPath(path:String): Unit = path match {
    case null =>
      LOG.info("Tried to add null path to PathTrie")
    case s:String =>
      val pathComponents: List[String] = path split "/"
      var parent: Option[TrieNode] = Some(rootNode)
      if(pathComponents.length <= 1) throw new IllegalArgumentException(s"Invalid path $path")
      for(part <- pathComponents) {
        parent match {
          case Some(p:TrieNode) =>
            if (p.getChild(part).isEmpty) p.addChild(part, new TrieNode(p))
            parent = p.getChild(part)
        }
      }
      if(parent.isDefined) parent.get.property = true
  }

  def deletePath(path:String):Unit = path match {
    case null =>
      LOG.info("Tried to delete null path on PathTrie")
    case s: String =>
      var pathComponents: Array[String] = path.split("/")
      var pstr: String = ""

      var parent: Option[TrieNode] = Some(rootNode)
      if (pathComponents.length <= 1) throw new IllegalArgumentException(s"Invalid path $path")
      for (part <- pathComponents) {
        parent match {
          case Some(p: TrieNode) =>
            val parnt: Option[TrieNode] = p.getChild(part)
            if (parnt.isEmpty) return
            parent = parnt
            pstr = part
            LOG.info(s"$parent")
        }
      }
      parent match {
        case Some(p: TrieNode) =>
          p.parent match {
            case Some(realParent: TrieNode) =>
              realParent deleteChild pstr
          }
      }

  }

  def findMaxPrefix(path:String):Option[String] = {
    if(path == null) return None
    if(path equals "/") return Option(path)
    val pathComponents: List[String] = path split "/"
    var parent:TrieNode = rootNode
    var components: List[String] = Nil
    var n: TrieNode = new TrieNode(None)
    var property:Boolean = false

    if(pathComponents.length <= 1) throw new IllegalArgumentException(s"Invalid path $path")


    for(pc <- pathComponents) {
      if(parent.hasChild(pc)) {
        parent = parent.getChild(pc).get
        components = pc :: components
        property = property || parent.property
      } else break
    }
    Option(components.mkString("/"))
  }



  def clear(): Unit = for(child <- rootNode.getChildren) rootNode.deleteChild(child)

}

object PathTrie {
  val LOG = Logger(classOf[PathTrie])

  implicit def arrayToList[A](a: Array[A]):List[A] = a.toList
}

