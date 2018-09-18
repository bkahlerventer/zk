package zk.zookeeper.common

import collection.JavaConverters._
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}

import scala.annotation.tailrec
import scala.collection.concurrent



/**
  * a class that implements prefix matching for
  * components of a filesystem path. the trie
  * looks like a tree with edges mapping to
  * the component of a path.
  * example /ab/bc/cf would map to a trie
  *           /
  *        ab/
  *        (ab)
  *      bc/
  *       /
  *      (bc)
  *   cf/
  *   (cf)
  *
  * Create a TrieNode and set the parent
  * @param oparent - parent of the current TrieNode
  *
  */
class TrieNode(oparent : Option[TrieNode]) {

  def apply(oparent: Option[TrieNode]): TrieNode = new TrieNode(oparent)

  def apply(tparent: TrieNode): TrieNode = new TrieNode(Option(tparent))


  def append(path: List[String])= {

    @tailrec
    def loop(node:TrieNode, idx: Int) = {
      if(idx == path.length) node.property = true   // we are at the last element in the path
      else {
        val k = path(idx)
        val r = node.children.getOrElseUpdate(k, new TrieNode())
      }

    }
  }

  def this(tparent: TrieNode) = this(Option(tparent))



  /**
    * property which marks this node as containing an active TrieNode.
    *
    * set by PathTrie.addPath and
    * cleared by TrieNode.deleteChild if the Node has more than a single child.  "Deleting" the node without truncting the tree
    * it would appear that the end nodes of the PathTrie is marked by this property
    */
  private[this] var _property: Boolean = false

  /**
    * Get the property of the TrieNode
    * @return the property for this node
    */
  def property: Boolean = _property

  /**
    * Set The property of this node
    * @param value to set the property to
    */
  def property_=(value: Boolean): Unit = {
    _property = value
  }

  /**
    * The children of the current TrieNode
    */
  private var children:concurrent.Map[String,TrieNode] = new ConcurrentHashMap[String, TrieNode]().asScala

  /**
    * The parent of this TrieNode.  The rootNode has a None for the parent.
    */
  private[this] var _parent: Option[TrieNode] = oparent

  /**
    * Get the parent of this node
    * @return the parent node
    */
  def parent: Option[TrieNode] = _parent

  /**
    * Set the parent of this Node
    * @param value the value to set parent to
    */
  def parent_=(value: Option[TrieNode]): Unit = _parent = value

  def parent_=(value: TrieNode):Unit = parent_=(Option(value))

  /**
    * Add a child to this node
    * @param childName the string name of the child
    * @param node the node that is the child
    */
  def addChild(childName:String, node:Option[TrieNode]):Unit = node match {
    case Some(n:TrieNode) =>
      children.putIfAbsent(childName,n)
  }

  def addChild(childName:String, node:TrieNode):Unit = addChild(childName,Option(node))

  def hasChild(childName:String):Boolean = children.keySet.contains(childName)
  /**
    * Delete a child from this node
    * @param childName the string name of the child to be deleted
    */
  def deleteChild(childName:String):Unit = {
    val ocn = children.get(childName)
    ocn match {
      case Some(cn: TrieNode) =>
        if(cn.children.size == 1) {
          cn.parent = None
          children.remove(childName)
        } else {
          cn.property = false
        }
    }
  }

  /**
    * Return the child of a node mapping to the input childname
    * @param childName the name of the child
    * @return the child of a node
    */
  def getChild(childName:String):Option[TrieNode] = children.get(childName)

  /**
    * Get the List of the children of this trienode.
    * @return the List of Strings of its children
    */
  def getChildren:List[String] = children.keySet.toList

  /**
    * Get the string representation of this node
    * @return the string representation
    */
  override def toString: String = s"Children of TrieNode: ${children.keySet.mkString(" ")}"


}

