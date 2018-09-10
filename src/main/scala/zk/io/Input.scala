package zk.io

trait Input[T] {
  def readByte(tag:String):Byte
  def readBool(tag:String):Boolean
  def readInt(tag:String):Int
  def readLong(tag:String):Long
  def readFloat(tag:String):Float
  def readDouble(tag:String):Double
  def readString(tag:String):String
  def readBuffer(tag:String):Array[Byte]
  def readRecord(r:Record, tag:String):T
  def readVector(tag:String):Vector[_]
  def readMap(tag:String):Map[_,_]
}

