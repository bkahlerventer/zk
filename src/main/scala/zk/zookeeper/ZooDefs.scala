package zk.zookeeper

import zk.zookeeper.data.{ACL, Id}

object ZooDefs {
  val CONFIG_NODE = "/zookeeper/config"

  object OpCode {
    val notification:Int = 0
    val create:Int = 1
    val delete:Int = 2
    val exists:Int = 3
    val getData:Int = 4
    val setData:Int = 5
    val getACL:Int = 6
    val setACL:Int = 7
    val getChildren:Int = 8
    val sync:Int = 9
    val ping:Int = 11
    val getChildren2:Int = 12
    val check:Int = 13
    val multi:Int = 14
    val create2:Int = 15
    val reconfig:Int = 16
    val checkWatches:Int = 17
    val removeWatches:Int = 18
    val createContainer:Int = 19
    val deleteContainer:Int = 20
    val createTTL:Int = 21
    val auth:Int = 100
    val setWatches:Int = 101
    val sasl:Int = 102
    val createSession:Int = -10
    val closeSession:Int = -11
    val error:Int = -1
  }

  object Perms {
    val READ:Int = 1 << 0
    val WRITE:Int = 1 << 1
    val CREATE:Int = 1 << 2
    val DELETE:Int = 1 << 3
    val ADMIN:Int = 1 << 4
    val ALL:Int = READ | WRITE | CREATE | DELETE | ADMIN
  }

  object Ids {
    val ANYONE_ID_UNSAFE:Id = Id("world", "anyone")
    val AUTH_IDS:Id = Id("auth", "")
    val OPEN_ACL_UNSAFE:Array[ACL] = Array(ACL(Perms.ALL,ANYONE_ID_UNSAFE))
    val CREATOR_ALL_ACL:Array[ACL] = Array(ACL(Perms.ALL, AUTH_IDS))
    val READ_ACL_UNSAFE:Array[ACL] = Array(ACL(Perms.READ, ANYONE_ID_UNSAFE))
  }

  val opNames:Array[String] = Array("notification", "create", "delete", "exists", "getData",
    "setData", "getACL", "setACL", "getChildren", "getChildren2", "getMaxChildren",
    "setMaxChildren", "ping")
}