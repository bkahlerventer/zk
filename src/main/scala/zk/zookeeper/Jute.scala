package zk.zookeeper

import java.io.{DataInput, DataOutput}

abstract class Jute {
  def write(out:DataOutput):Unit

  def readFields(in:DataInput):Unit

  def compareTo(that:Any):Int

  def signature:String
}




