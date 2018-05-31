package zk.zookeeper.common

import com.typesafe.scalalogging.Logger
import javax.net.ssl.{KeyManager, SSLContext, TrustManager}
import org.slf4j.LoggerFactory

object X509Util {
  val LOG = Logger(LoggerFactory.getLogger(X509Util.getClass))

  val SSL_KEYSTORE_LOCATION = "zookeeper.ssl.keyStore.location"

  val SSL_KEYSTORE_PASSWD = "zookeeper.ssl.keyStore.password"

  val SSL_TRUSTSTORE_LOCATION = "zookeeper.ssl.trustStore.location"

  val SSL_TRUSTSTORE_PASSWD = "zookeeper.ssl.trustStore.password"

  val SSL_AUTHPROVIDER = "zookeeper.ssl.authProvider"

  def createSSLContext:SSLContext = {
    createSSLContext(new ZKConfig)
  }

  def createSSLContext(config:ZKConfig) = {
    var keyManagers:Array[KeyManager] = Array[KeyManager]()
    var trustManagers:Array[TrustManager] = Array[TrustManager]()

    val keyStoreLocationProp = config.getProperty(ZKConfig.SSL_KEYSTORE_LOCATION)
  }
}
