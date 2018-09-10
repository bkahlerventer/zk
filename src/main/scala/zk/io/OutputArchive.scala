package zk.io

/**
  * Interface all the serializers have to implement
  */
trait OutputArchive {
  def writeByte(b:Byte, tag:String):Unit
  def writeBool(b:Boolean, tag:String):Unit
  def writeInt(i:Int, tag:String):Unit
  def writeLong(l:Long, tag:String):Unit
  def writeFloat(f:Float, tag:String):Unit
  def writeDouble(d:Double, tag:String):Unit
  def writeString(s:String, tag:String):Unit
  def writeBuffer(buf:Array[Byte], tag:String):Unit
  def writeRecord(r:Record, tag:String):Unit
  def startRecord(r:Record, tag:String):Boolean
  def endRecord(r:Record, tag:String):Boolean
  def startVector(v:Vector[_], tag:String):Unit
  def endVector(v:Vector[_], tag:String):Unit
  def startMap(m:Map[_,_], tag:String):Unit
  def endMap(m:Map[_,_], tag:String):Unit
}
