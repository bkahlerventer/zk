package zk.zookeeper.common

import java.io.{IOException, Writer}

trait WriterStatement {
  @throws(classOf[IOException])
  def write(os:Writer):Unit
}
