package zk.zookeeper.common

import java.net.{Inet6Address, InetAddress, InetSocketAddress}


object Netutils {
  def formatInetAddr(addr:InetSocketAddress):String = {
    val ia: InetAddress = addr.getAddress
    ia match {
      case null =>
        s"${addr.getHostString}:${addr.getPort}"
      case a:Inet6Address =>
        s"[${a.getHostAddress}]:${addr.getPort}"
      case a:InetAddress =>
        s"${a.getHostAddress}:${addr.getPort}"
    }
  }

}
