package zk.zookeeper.client
import java.net.{InetAddress, InetSocketAddress, UnknownHostException}

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.util._
/**
  * Most simple HostProvider, resolves only on instantiation.
  *
  */
class StaticHostProvider(srvrAddresses:Vector[InetSocketAddress], randomnessSeed:Long = System.currentTimeMillis() ^ hashCode()) extends HostProvider {
  import StaticHostProvider._

  private val serverAddresses = resolveAndShuffle(srvrAddresses)
  private val sourceOfRandomness = new Random(System.currentTimeMillis() ^ hashCode())
  private val servers = Vector()

  // access the serverAddresses via the instance name
  def apply(i:Int):Option[InetSocketAddress] = if (serverAddresses.isDefinedAt(i)) Some(serverAddresses(i)) else None

  override def size: Int = serverAddresses.size

  // iterate over addresses Java Style... Deprecated
  override def next(spinDelay: Long): InetSocketAddress = ???

  override def onConnected(): Unit = ???

  // check if currentConnection is in the serverAddresses, return true if changing connections is necessary for load-balancing, false otherwise
  def checkConnection(currentHost:InetSocketAddress): Boolean = ???

  def getServerAtIndex(i:Int):Option[InetSocketAddress] =
    if(serverAddresses.isDefinedAt(i)) Some(serverAddresses(i)) else None

  def getServerAtCurrentIndex:Option[InetSocketAddress] = ???

  private def nextHostInReconfigMode:InetSocketAddress = ???

  override def updateServerList(serverAddresses: Vector[InetSocketAddress], currentHost: InetSocketAddress): HostProvider = {
    val shp = new StaticHostProvider(srvrAddresses)
  }
}

/**
  * No state any longer in StaticHostProvider.  Old Statefull functions
  * return a new instance of StaticHostProvider with needed changes.
  */


object StaticHostProvider {
  val LOG = Logger(LoggerFactory.getLogger(StaticHostProvider.getClass))

  private def addr(address: InetSocketAddress): Try[String] = {
    val r = Try(if (address.getAddress != null) address.getAddress.getHostAddress else address.getHostString)
    r match {
      case Success(_) => r
      case Failure(exc) =>
        LOG.warn(s"No IP Address found for a server $address, $exc")
        Failure(exc)
    }
  }

  // only resolve Success addr, ignore Failures
  private def resolve(addr: Try[String]): Try[Vector[InetAddress]] = addr match {
    case Success(adr) => Try(InetAddress.getAllByName(adr).toVector)
    case Failure(exc) => Failure(exc)
  }

  // only process successfull records, ignore failures
  private def taddress(address: InetSocketAddress, adr: Try[Vector[InetAddress]]): Try[Vector[InetAddress]] = adr match {
    case Success(_adr:Vector[InetAddress]) => Try(_adr.map(a => InetAddress.getByAddress(address.getHostString, a.getAddress)))
    case Failure(exc) => Failure(exc)
  }

  private def resolveAndShuffle(serverAddresses:Vector[InetSocketAddress]):Vector[InetSocketAddress] = {
      val xList = serverAddresses.zip(serverAddresses.map(address => addr(address)).map(adr => resolve(adr)))
      for (x <- xList; taddr <- taddress(x._1, x._2); tadr <- taddr) yield new InetSocketAddress(tadr, x._1.getPort)
  }

  def
}