package zk.io

trait InputArchive {
  def readByte(tag:String):Byte
  def readBool(tag:String):Boolean
  def readInt(tag:String):Int
  def readLong(tag:String):Long
  def readFloat(tag:String):Float
  def readDouble(tag:String):Double
  def readString(tag:String):String
  def readBuffer(tag:String):Array[Byte]
  def readRecord(r:Record, tag:String):Unit
  def startRecord(tag:String):Unit
  def endRecord(tag:String):Unit
  def startVector(tag:String):Index
  def endVector(tag:String):Unit
  def startMap(tag:String):Index
  def endMap(tag:String):Unit
}
