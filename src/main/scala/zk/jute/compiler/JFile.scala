package zk.jute.compiler

import java.io.{File, IOException}

class JFile(name:String, inclFiles:List[JFile], recList:List[JRecord]) {
  private val mName = name
  private val mInclFiles = inclFiles
  private val mRecords = recList

  def getName:String = {
    val idx = mName.lastIndexOf('/')
    if(idx > 0) mName.substring(idx) else mName
  }

  def genCode(language:String, outputDirectory:File):Unit = language match {
    case "c++" => new CppGenerator(mName, mInclFiles, mRecords, outputDirectory).genCode()
    case "java" => new JavaGenerator(mName, mInclFiles, mRecords, outputDirectory).genCode()
    case "c" => new CGenerator(mName, mInclFiles, mRecords, outputDirectory).genCode()
    case "csharp" => new CSharpGenerator(mName, mInclFiles, mRecords, outputDirectory).genCode()
    case _ => throw new IOException(s"Cannot recognize language: $language")
  }
}
