package zk.zookeeper.common

class X509Exception(message:String = "", cause:Throwable = None.orNull) extends Exception(message,cause)

case class KeyManagerException(message:String = "", cause:Throwable = None.orNull) extends X509Exception(message,cause)
case class TrustManagerException(message:String = "", cause:Throwable = None.orNull) extends X509Exception(message,cause)
case class SSLContextException(message:String = "", cause:Throwable = None.orNull) extends X509Exception(message,cause)
