package zk.zookeeper.client

import java.net.InetSocketAddress

import scala.collection.mutable

import zk.zookeeper.common.PathUtils

class ConnectStringParser(connectString:String) {
  import ConnectStringParser.DEFAULT_PORT

  private var chrootPath: String = _
  private var serverAddresses = new mutable.ArrayBuffer[InetSocketAddress]()
  private var cp:String = _
  private val hostList: List[String] = connectString.split(',').toList

  private val off = connectString.indexOf('/')
  if (off >= 0) {
    cp = connectString.substring(off)
    if(cp.length == 1) chrootPath = null
    else {
      PathUtils.validatePath(cp)
      chrootPath = cp
    }
    cp = connectString.substring(0,off)
  } else {
    chrootPath = null
  }
  for(host <- hostList) {
    val pidx = host.lastIndexOf(':')
    if(pidx >= 0 && pidx < host.length - 1) serverAddresses += InetSocketAddress.createUnresolved(host.substring(0,pidx), host.substring(pidx+1).toInt)
    serverAddresses += InetSocketAddress.createUnresolved(host.substring(0,pidx), DEFAULT_PORT)
  }

  def getChrootPath:String = chrootPath

  def getServerAddresses:mutable.ArrayBuffer[InetSocketAddress] = serverAddresses

}


object ConnectStringParser {
  val DEFAULT_PORT = 2181
}