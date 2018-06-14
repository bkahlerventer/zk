package zk.zookeeper.client

import java.net.InetSocketAddress



trait HostProvider {
  def size:Int

  def next(spinDelay:Long):InetSocketAddress

  def onConnected(:Unit

  def updateServerList(serverAddresses: Vector[InetSocketAddress], currentHost:InetSocketAddress)
}
