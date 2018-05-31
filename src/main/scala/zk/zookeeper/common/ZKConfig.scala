/**
  * scala
  */

package zk.zookeeper.common


import scala.collection.JavaConverters._

import java.io.File

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import zk.zookeeper.Environment



class ZKConfig(configPath:Option[String]=None) {
  import ZKConfig._

  //private val properties:mutable.HashMap[String, String] = mutable.HashMap[String, String]()
  private val properties:Config = if(configPath.isDefined) ConfigFactory.load(configPath.get) else ConfigFactory.load()

  //init()

  //if(configFile.isDefined) addConfiguration(configFile)

  def this(configFile:File) = this(Some(configFile.getAbsolutePath))

  private def init():Unit = handleBackwardCompatibility()

  // no need to parse properties from System Properties
  protected def handleBackwardCompatibility():Unit = {}

  def getProperty(key:String):Option[String] = {
    val x = properties.getString(key)
    if(x == null) None else Option(x)
  }

  def getOrElse(key:String, defaultValue:String) :String = properties.che

  def getProperty(key:String, defaultValue:String):String = properties.getOrElse(key,defaultValue)

  def getJaasConfKey:String = System.getProperty(Environment.JAAS_CONF_KEY)

  def addConfiguration(maybeFile: Option[File]):Unit = {
    LOG.info(s"Reading configuration from: ${configFile.get.getAbsolutePath}")

    val cfg:Config = ConfigFactory.load(maybeFile.get.getAbsolutePath)

    parseProperties(cfg)
  }

  // no need to parse properties, already processed
  def parseProperties(config: Config):Unit = {}
}

object ZKConfig {
  val LOG = Logger(LoggerFactory.getLogger(ZKConfig.getClass))
  val SSL_KEYSTORE_LOCATION = "zookeeper.ssl.keyStore.location"
  val SSL_KEYSTORE_PASSWD = "zookeeper.ssl.keyStore.password"
  val SSL_TRUSTSTORE_LOCATION = "zookeeper.ssl.trustStore.location"
  val SSL_TRUSTSTORE_PASSWD = "zookeeper.ssl.trustStore.password"
  val SSL_AUTHPROVIDER = "zookeeper.ssl.authProvider"
  val JUTE_MAXBUFFER = "jute.maxbuffer"
  val KINIT_COMMAND = "zookeeper.kinit"
  val JGSS_NATIVE = "sun.security.jgss.native"

}