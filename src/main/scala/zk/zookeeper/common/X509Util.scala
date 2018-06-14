package zk.zookeeper.common

import java.io.{File, FileInputStream}
import javax.net.ssl._
import java.security.KeyStore

import com.typesafe.scalalogging.Logger
import resource._
import org.slf4j.LoggerFactory


import scala.util._


object X509Util {
  val LOG = Logger(LoggerFactory.getLogger(X509Util.getClass))

  val SSL_KEYSTORE_LOCATION = "zookeeper.ssl.keyStore.location"

  val SSL_KEYSTORE_PASSWD = "zookeeper.ssl.keyStore.password"

  val SSL_TRUSTSTORE_LOCATION = "zookeeper.ssl.trustStore.location"

  val SSL_TRUSTSTORE_PASSWD = "zookeeper.ssl.trustStore.password"

  val SSL_AUTHPROVIDER = "zookeeper.ssl.authProvider"

  def createSSLContext:Try[SSLContext] = {
    createSSLContext(new ZKConfig)
  }

  def createSSLContext(config:ZKConfig):Try[SSLContext] = {
    //var keyManagers:Try[Array[KeyManager]] = Try(Array[KeyManager]())
    //var trustManagers:Try[Array[TrustManager]] = Try(Array[TrustManager]())

    val keyStoreLocationProp = config.getProperty(ZKConfig.SSL_KEYSTORE_LOCATION)
    val keyStorePasswordProp = config.getProperty(ZKConfig.SSL_KEYSTORE_PASSWD)

    if (keyStoreLocationProp.isEmpty || keyStorePasswordProp.isEmpty) {
      LOG.warn("Keystore not specified for client connection")
      return Failure(SSLContextException("keystore location not specified for client connection"))
    }
    val keyManager:Option[X509KeyManager] = createKeyManager(keyStoreLocationProp.get, keyStorePasswordProp.get)

    keyManager match {
      case None => return Failure(SSLContextException("Failed to create KeyManager"))
    }

    val trustStoreLocationProp: Option[String] = config.getProperty(ZKConfig.SSL_TRUSTSTORE_LOCATION)
    val trustStorePasswordProp: Option[String] = config.getProperty(ZKConfig.SSL_TRUSTSTORE_PASSWD)

    if (trustStoreLocationProp.isEmpty || trustStorePasswordProp.isEmpty) {
      LOG.warn("Truststore not specified for client connection")
      return Failure(SSLContextException("Truststore location not specified for client connection"))
    }

    val trustManager:Option[X509TrustManager]  = createTrustManager(trustStoreLocationProp.get, trustStorePasswordProp.get)

    trustManager match {
      case None => return Failure(SSLContextException("Failed to create TrustManager"))
    }

    val sslContext: Try[SSLContext] = Try(SSLContext.getInstance("TLSv1"))
    sslContext match {
      case Success(ctx) =>
        if (keyManager.isDefined && trustManager.isDefined)
          Try(ctx.init(Array(keyManager.get.asInstanceOf[KeyManager]), Array(trustManager.get.asInstanceOf[TrustManager]), null))
    }
    sslContext
  }

  def createKeyManager(keyStoreLocation:String, keyStorePassword:String):Option[X509KeyManager] = {

      val keyStoreFile = new File(keyStoreLocation)

      for(inputStream <- managed(new FileInputStream(keyStoreFile))) {
        val keyStorePasswordChars = keyStorePassword.toCharArray

        val ks = KeyStore.getInstance("JKS")
        ks.load(inputStream, keyStorePasswordChars)

        val kmf = KeyManagerFactory.getInstance("SunX509")
        kmf.init(ks, keyStorePasswordChars)

        val k:Array[KeyManager] = kmf.getKeyManagers.filter(i => i.isInstanceOf[X509KeyManager])

        if(!k.isEmpty) return Some(k(0).asInstanceOf[X509KeyManager])

      }
      None
  }

  def createTrustManager(trustStoreLocation:String, trustStorePassword:String):Option[X509TrustManager] = {
    val trustStoreFile = new File(trustStoreLocation)

    for(inputStream <- managed(new FileInputStream(trustStoreFile))) {
      val trustStorePasswordChars = trustStorePassword.toCharArray

      val ts = KeyStore.getInstance("JKS")
      ts.load(inputStream, trustStorePasswordChars)

      val tmf = TrustManagerFactory.getInstance("SunX509")
      tmf.init(ts)

      val t:Array[TrustManager] = tmf.getTrustManagers.filter(i => i.isInstanceOf[X509TrustManager])

      if(!t.isEmpty) return Some(t(0).asInstanceOf[X509TrustManager])
    }
    None
  }




}
