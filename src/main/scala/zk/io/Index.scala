package zk.io

trait Index {
  def done():Boolean
  // def incr():Unit // not in scala, we are now stateless
}
