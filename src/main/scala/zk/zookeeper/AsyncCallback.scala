package zk.zookeeper

import zk.zookeeper.data.{ACL, Stat}

// TODO - Async in Scala could be better handled with Futures/Promises ???

trait AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object)
}

trait StatCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, stat: Stat): Unit
}

trait DataCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, data: Array[Byte], stat: Stat): Unit
}

trait ACLCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, acl: List[ACL], stat: Stat)
}

trait ChildrenCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, children: List[String])
}

trait Children2Callback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, children: List[String], stat: Stat)
}

trait Create2Callback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, name: String, stat: Stat)
}

trait StringCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, name: String)
}

trait VoidCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object)
}

trait MultiCallback extends AsyncCallback {
  def processResult(rc: Int, path: String, ctx: Object, Opresults: List[OpResult])
}
