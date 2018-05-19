package zk.jute

trait Index {
  def done():Boolean
  def incr():Unit
}
