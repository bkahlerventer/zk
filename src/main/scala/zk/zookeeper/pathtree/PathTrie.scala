package zk.zookeeper.pathtree

trait PathTrie[A] extends Traversable[A] {
  def append(key: A)
  def findByPrefix(prefix:A): List[A]
  def contains(path: A):Boolean
  def remove(path:A):Boolean
}
