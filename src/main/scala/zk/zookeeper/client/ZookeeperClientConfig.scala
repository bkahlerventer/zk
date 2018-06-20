package zk.zookeeper.client


trait ZookeeperClientConfig  {
  var hostList = ""
  var sessionTimeout = 3000
  var basePath = ""

  def apply = {
    new ZooKeeperClient(this)
  }
}
