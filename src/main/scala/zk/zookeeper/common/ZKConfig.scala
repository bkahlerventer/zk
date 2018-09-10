/**
  * scala
  */

package zk.zookeeper.common

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import zk.zookeeper.Environment


class ZKConfig(configPath:Option[String]=None) {
  import ZKConfig._

  private var properties:Config = ConfigFactory.load(configPath.get)

  def this(configFile:File) = this(Some(configFile.getAbsolutePath))

  def getProperty(key:String):Option[String] = {
    val x = properties.getString(key)
    if(x == null) None else Option(x)
  }

  def getOrElse(key:String, defaultValue:String) :String = {
    val v = properties.getString(key)
    if(!v.isEmpty) v else defaultValue
  }

  def getProperty(key:String, defaultValue:String) :String = getOrElse(key,defaultValue)

  def getJaasConfKey:String = System.getProperty(Environment.JAAS_CONF_KEY)

  // stop doing this, ZKConfig is immutable for thread-safety.  This will stay not implemented
  @Deprecated
  def setProperty(key:String, value:String): Unit = ???
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