package zk.zookeeper.common

import java.io.{IOException, OutputStream}


trait OutputStreamStatement {
  @throws(classOf[IOException])
  def write(os:OutputStream)
}
