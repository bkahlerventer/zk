package zk.zookeeper

import zk.jute.Record
import zk.zookeeper.data.ACL
import zk.zookeeper.proto.CreateRequest

abstract class Op(otype:Int, path:String) {
  def getType:Int = otype
  def getPath:String = path

  def toRequestRecord: Record
  def withChroot(addRootPrefix:String):Op

  // TODO - convert to Boolean method
  def validate:Boolean
}

// factory methods
object Op {
  import Create._

  def create(path:String, data:Array[Byte], acl:List[ACL], flags:Int):Op = ???

  def create(path:String, data:Array[Byte], acl:List[ACL], flags:Int, ttl:Long):Op = ???

  def create(path:String, data:Array[Byte], acl:List[ACL], createMode:CreateMode):Op = ???

  def create(path:String, data:Array[Byte], acl:List[ACL], createMode:CreateMode, ttl:Long):Op = ???

  def delete(path:String, version:Int):Op = ???

  def setData(path:String, data:Array[Byte], version:Int):Op = ???

  def check(path:String, version:Int):Op = ???

  case class Create(path:String, data:Array[Byte], acl:Vector[ACL], createMode:CreateMode=CreateMode.PERSISTENT, flags:Int = CreateMode.PERSISTENT.value) extends Op(getOpCode(CreateMode.withValueOrElse(flags,CreateMode.PERSISTENT)),path) {
    // CreateRequest investigate missing Record mixin in CreateRequest in zookeeper.jute.scala ...
    override def toRequestRecord: Record = CreateRequest(path,data,acl,flags)

    override def withChroot(rpath: String): Op = Create(rpath,data,acl,CreateMode.PERSISTENT,flags)

    override def validate: Boolean = ???

    override def canEqual(that: Any): Boolean = that.isInstanceOf[Create]

    override def equals(that: scala.Any): Boolean = that match {
      case that:Create => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 135 + path.hashCode + data.hashCode + acl.hashCode() + createMode.hashCode() + flags


  }
  object Create {
    def getOpCode(createMode: CreateMode):Int = createMode match {
      case c if c.isTTL => ZooDefs.OpCode.createTTL
      case c if c.isContainer => ZooDefs.OpCode.createContainer
      case _ => ZooDefs.OpCode.create
    }
  }

}

