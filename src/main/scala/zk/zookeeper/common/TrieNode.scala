package zk.zookeeper.common

import collection.JavaConverters._
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import scala.collection.concurrent



class TrieNode(_parent_ : TrieNode) {
  private[this] var _property: Boolean = false

   def property: Boolean = _property

   def property_=(value: Boolean): Unit = {
    _property = value
  }

  private var children:concurrent.Map[String,TrieNode] = new ConcurrentHashMap[String, TrieNode]().asScala
  private[this] var _parent: TrieNode = _parent_

  def parent: TrieNode = _parent

  private def parent_=(value: TrieNode): Unit = {
    _parent = value
  }

  def addChild(childName:String, node:TrieNode):Unit = children.putIfAbsent(childName, node)

  def deleteChild(childName:String):Unit = {
    val ocn = children.get(childName)
    if(ocn.isDefined) {
      if(ocn.get.children.size == 1) {
        ocn.get.parent = null
        children.remove(childName)
      } else {
        ocn.get.property = false
      }
    }
  }

  def getChild(childName:String):TrieNode = {
    if(children.contains(childName))  children(childName)
    else null
  }

  def getChildren:Vector[String] = children.keySet.toVector

  override def toString: String = {
    var sb = new StringBuilder
    sb.append("Children of trienode: ")
    for(str <- children.keySet) {
      sb.append(s" $str")
    }
    sb.toString()
  }


}
