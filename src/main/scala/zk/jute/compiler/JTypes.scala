package zk.jute.compiler

import scala.collection.mutable.ArrayBuffer

trait CodeGenerator {
  private val mFQNmae:String = ""
  private val mName:String = ""
  private val mModule:String = ""
  private val mFields:List[JField] = Nil

  def genGetSet(j:Jute, fIdx:Int):String
  def genCompareBytes(j:Jute):String
  def genCompareTo(j:Jute, other:String):String
  def genCompareToWrapper(j:Jute, other:String):String
  def genDecl(j:Jute):String
  def genEquals(j:Jute, peer:String):String
  def genHashCode(j:Jute):String
  def genReadWrapper(j:Jute, tag:String, decl:Boolean):String
  def genReadMethodName(j:Jute):String
  def genReadMethod(j:Jute, tag:String):String = genReadWrapper(j,tag,decl = false)
  def genWriteWrapper(j:Jute, tag:String):String
  def genWriteMethodName(j:Jute):String
  def genWriteMethod(j:Jute, tag:String):String = genWriteWrapper(j,tag)
  def genSlurpBytes(j:Jute, b:String, s:String, l:String):String
  def genConstructorParam(j:Jute):String
  def genConstructorSet(j:Jute):String
}

sealed abstract class Jute(suffix:String, wrapper:String) {
  def getSignature:String
}
abstract class JType(name:String, suffix:String, wrapper:String, unwrap:String) extends Jute(suffix, wrapper)

abstract class JCompType(_type:String, suffix:String, wrapper:String) extends Jute(suffix, wrapper)

case class JBoolean(name:String, suffix:String, wrapper:String, unwrap:String) extends JType(name,suffix,wrapper,unwrap) {
  override def getSignature:String = "z"
}
case class JBuffer(fname:String, jtype:String, suffix:String, wrapper:String) extends JCompType(jtype, suffix, wrapper) {
  override def getSignature: String = "B"
}
case class JByte(name:String, suffix:String, wrapper:String, unwrap:String) extends JType(name,suffix,wrapper,unwrap) {
  override def getSignature: String = "b"
}
case class JDouble(name:String, suffix:String, wrapper:String, unwrap:String) extends JType(name,suffix,wrapper,unwrap) {
  override def getSignature: String = "d"
}
// TODO - might be able to do this with a Tuple1 or make this a container... or do away with... or just convert to a type, with implicit getSignature...
case class JField(mType:JType, mName:String) {
  def getSignature: String = mType.getSignature
}

case class JFile(mName:String, mInclFiles:List[JFile], mRecords:List[JRecord])
case class JFloat(name:String, suffix:String, wrapper:String, unwrap:String) extends JType(name,suffix,wrapper,unwrap) {
  override def getSignature: String = "f"
}
case class JInt(name:String, suffix:String, wrapper:String, unwrap:String) extends JType(name,suffix,wrapper,unwrap) {
  override def getSignature: String = "i"
}
case class JLong(name:String, suffix:String, wrapper:String, unwrap:String) extends JType(name,suffix,wrapper,unwrap) {
  override def getSignature: String = "l"
}
case class JMap(t1:JType, t2:JType, jtype:String, suffix:String, wrapper:String) extends JCompType(jtype, suffix, wrapper) {
  override def getSignature: String = s"{${t1.getSignature}${t2.getSignature}}"
}
case class JRecord(mFQName:String, mName:String, mModule:String, mFields:List[JField], name:String, flist:ArrayBuffer[JField], jtype:String, suffix:String, wrapper:String) extends JCompType(jtype, suffix, wrapper) {
  override def getSignature: String = s"L$mName(${mFields.map(f => f.getSignature).mkString("")})"
}
case class JString(jtype:String, suffix:String, wrapper:String) extends JCompType(jtype, suffix, wrapper) {
  override def getSignature: String = "s"
}
case class JVector(t:JType, jtype:String, suffix:String, wrapper:String) extends JCompType(jtype, suffix, wrapper) {
  override def getSignature: String = s"[${t.getSignature}]"
}

