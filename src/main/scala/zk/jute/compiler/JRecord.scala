package zk.jute.compiler

import java.io.{File, FileWriter}

import scala.collection.mutable.ArrayBuffer

class JRecord(name:String, flist:ArrayBuffer[JField]) extends JCompType {
  private val mFQName:String = ""
  private val mName = name
  private val mModule:String = ""
  private val mFields = List[JField]()

  def getName:String = mName
  def getCsharpName:String = if(mName == "Id") "ZKId" else mName
  def getJavaFQName:String = mFQName
  def getCppFQName:String = mFQName.replaceAll("\\.", "::")
  def getJavaPackage:String = ???
  def getCppNameSpace:String = ???
  def getCsharpNameSpace:String = ???
  def getFields:List[JField] = ???
  def getSignature:String = ???
  override def genCppDecl(fname:String):String = ???
  def genJavaReadMethod(fname:String, tag:String):String = ???
  def genJavaReadWrapper(fname:String , tag:String, decl:Boolean):String = ???
  def genJavaWriteWrapper(fname:String, tag:String):String = ???
  def genCsharpReadMethod(fname:String, tag:String):String = ???
  def genCsharpReadWrapper(fname:String , tag:String, decl:Boolean):String = ???
  def genCsharpWriteWrapper(fname:String, tag:String):String = ???
  def genCCode(h:FileWriter, c:FileWriter):Unit = ???
  def genSerialize(c:FileWriter, typ:JType, tag:String, name:String):Unit = ???
  def genDeserialize(c:FileWriter, typ:JType, tag:String, name:String):Unit = ???
  def extractMethodSuffix(t:JType):String = ???
  def extractStructName(t:JType):String = ???
  def genCppCode(hh:FileWriter, cc:FileWriter):Unit = ???
  def genJavaCode(outputDirectory:File):Unit = ???
  def genCsharpCode(outputDirectory:File):Unit = ???
  def getCsharpFQName(name:String):String = ???
}

object JRecord {
  var vectorStructs: Map[String, String] = Map[String, String]()

}
