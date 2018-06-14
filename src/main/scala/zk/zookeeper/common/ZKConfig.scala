/**
  * scala
  */

package zk.zookeeper.common


import scala.collection.JavaConverters._
import java.io.File

import com.typesafe.scalalogging.Logger
import org.apache.commons.configuration2._
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.slf4j.LoggerFactory
import zk.zookeeper.Environment

import scala.util.{Try,Success,Failure}



class ZKConfig(configPath:Option[String]=None) {
  import ZKConfig._

  val properties:Configuration = getFileConfigBuilder(configPath.get) match {
    case Success(fcb) => fcb
    case Failure(_) => new PropertiesConfiguration()
  }

  def getFileConfigBuilder(fileName:String):Try[Configuration] = {
    val params: Parameters = new Parameters()
    val builder = new FileBasedConfigurationBuilder[FileBasedConfiguration](classOf[PropertiesConfiguration])
      .configure(params.properties()
        .setFileName(configPath.get))
    Try(builder.getConfiguration)
  }

  def this(configFile:File) = this(Some(configFile.getAbsolutePath))

  def getProperty(key:String):Option[String] = {
    val x = properties.getString(key)
    if(x == null) None else Option(x)
  }

  def getOrElse(key:String, defaultValue:String) :String = properties.getString(key,defaultValue)

  def getProperty(key:String, defaultValue:String) :String = getOrElse(key,defaultValue)

  def getJaasConfKey:String = System.getProperty(Environment.JAAS_CONF_KEY)

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