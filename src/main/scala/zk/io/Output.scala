package zk.io

trait Output[T] {
  def writeByte(b: Byte, tag: String): T

  def writeBool(b: Boolean, tag: String): T

  def writeInt(i: Int, tag: String): T

  def writeLong(l: Long, tag: String): T

  def writeFloat(f: Float, tag: String): T

  def writeDouble(d: Double, tag: String): T

  def writeString(s: String, tag: String): T

  def writeBuffer(buf: Array[Byte], tag: String): T

  // do both start and end Record with data inside
  def writeRecord(r: Record, tag: String): T

  // do both start/end Vector with data inside
  def writeVector(v: Vector[_], tag: String): T

  // do both start/end Map with data inside
  def writeMap(m: Map[_, _], tag: String): T

}


