package zk.zookeeper

object Quotas {
  val procZookeeper = "/zookeeper"

  val quotaZookeeper = "/zookeeper/quota"

  val limitNode = "zookeeper_limits"

  val statNode = "zookeeper_stats"

  def quotaPath(path:String):String = s"$quotaZookeeper$path/$limitNode"

  def statPath(path:String):String = s"$quotaZookeeper$path/$statNode"

}
