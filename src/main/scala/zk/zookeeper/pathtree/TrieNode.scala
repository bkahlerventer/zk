package zk.zookeeper.pathtree

import java.util

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.collection.mutable

class TrieNode(val name: Option[String] = None, var property: Boolean = false) extends PathTrie[String] {
  private val children: mutable.Map[String, TrieNode] = new util.TreeMap[String,TrieNode]().asScala

  override def append(path: String):Unit = {
    val pathComponents: List[String] = path.split("/").toList
    @tailrec
    def appendHelper(node: TrieNode, pathComponents: List[String]): Unit = pathComponents match {
      case Nil =>
        node.property = true
      case p :: tail =>
        val r = node.children.getOrElseUpdate(p,new TrieNode(p))
        appendHelper(r, tail)
    }
    appendHelper(this, pathComponents)
  }

  def this(s:String) = this(Some(s))

  override def findByPrefix(prefix: String): List[String] = {
    val pathComponents: List[String] = prefix.split("/").toList

    @tailrec
    def helper(pathComponents: List[String], node: TrieNode, items: List[String]): List[String] = pathComponents match {
      case Nil =>
        node.name.get :: items
      case pprefix :: tail =>
        node.children.get(pprefix) match {
          case Some(child:TrieNode) => helper(tail, child, node.name.get :: items)
          case None => node.name.get :: items
        }
    }

    helper(pathComponents, this, Nil)
  }

  def contains(path: String):Boolean = {
    val pathComponents: List[String] = path.split("/").toList

    @tailrec
    def helper(pathComponents: List[String], node: TrieNode): Boolean = pathComponents match {
      case Nil =>
        node.property
      case pcomp :: tail =>
        node.children.get(pcomp) match {
          case Some(child) => helper(tail, child)
          case None => false
        }
    }

    helper(pathComponents, this)
  }

  override def remove(path : String) : Boolean = {
    val pathComponents: List[String] = path.split("/").toList

    def remove_helper(pathComponents: List[String], parent: Option[TrieNode], node: Option[TrieNode]): Boolean = pathComponents match {
      case Nil =>
        if (node.isEmpty) return false
        val _node = node.get
        _node.property = false

        if(_node.children.isEmpty) for{n <- node; name <-n.name; p <- parent}  p.children.remove(name)

        true

      case p :: tail =>
        if (node.isEmpty) return false
        remove_helper(tail, node, node.get.children.get(p))
    }
    remove_helper(pathComponents, None, Option(this))
  }


  override def foreach[U](f: String => U): Unit = {

    @tailrec def foreachHelper(nodes: TrieNode*): Unit = {
      if (nodes.nonEmpty) {
        nodes.flatten.foreach(node => f(node))
        foreachHelper(nodes.flatMap(node => node.children.values): _*)
      }
    }

    foreachHelper(this)
  }

}
